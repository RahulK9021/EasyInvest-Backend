package com.investkaro.service.Impl;

import com.investkaro.dto.DocumentResponse;
import com.investkaro.entity.Document;
import com.investkaro.entity.Role;
import com.investkaro.entity.Startup;
import com.investkaro.entity.User;
import com.investkaro.repository.DocumentRepository;
import com.investkaro.repository.StartupRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.service.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final StartupRepository startupRepository;
    private final UserRepository userRepository;
    private final Path uploadPath;

    public DocumentServiceImpl(DocumentRepository documentRepository, StartupRepository startupRepository, UserRepository userRepository, @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.documentRepository = documentRepository;
        this.startupRepository = startupRepository;
        this.userRepository = userRepository;
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    public DocumentResponse uploadDocument(Long startupId, String fileType, MultipartFile file, String email) {
        User user = getUser(email);
        Startup startup = getStartup(startupId);
        if (!startup.getFounder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only the founder can upload startup documents");
        }
        if (file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
        String storedName = UUID.randomUUID() + "-" + originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        try {
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(storedName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store document");
        }

        Document document = new Document();
        document.setStartup(startup);
        document.setFileName(originalName);
        document.setStoredFileName(storedName);
        document.setFileType(fileType);
        return toResponse(documentRepository.save(document));
    }

    @Override
    public List<DocumentResponse> getDocuments(Long startupId) {
        Startup startup = getStartup(startupId);
        return documentRepository.findByStartupOrderByUploadedAtDesc(startup).stream().map(this::toResponse).toList();
    }

    @Override
    public Resource downloadDocument(Long id, String email) {
        getUser(email);
        Document document = getDocument(id);
        try {
            Resource resource = new UrlResource(uploadPath.resolve(document.getStoredFileName()).toUri());
            if (!resource.exists()) {
                throw new EntityNotFoundException("Document file not found");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Could not read document");
        }
    }

    @Override
    public String getFileName(Long id) {
        return getDocument(id).getFileName();
    }

    @Override
    public void deleteDocument(Long id, String email) {
        User user = getUser(email);
        Document document = getDocument(id);
        if (user.getRole() != Role.ADMIN && !document.getStartup().getFounder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only the founder can delete startup documents");
        }
        try {
            Files.deleteIfExists(uploadPath.resolve(document.getStoredFileName()));
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete document file");
        }
        documentRepository.delete(document);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Startup getStartup(Long startupId) {
        return startupRepository.findById(startupId).orElseThrow(() -> new EntityNotFoundException("Startup not found"));
    }

    private Document getDocument(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Document not found"));
    }

    private DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getStartup().getId(),
                document.getFileName(),
                document.getFileType(),
                document.getUploadedAt()
        );
    }
}
