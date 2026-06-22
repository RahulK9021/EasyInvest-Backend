package com.investkaro.controller;

import com.investkaro.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/{startupId}")
    public ResponseEntity<?> uploadDocument(@PathVariable Long startupId, @RequestParam String fileType, @RequestParam MultipartFile file, Authentication authentication) {
        return ResponseEntity.ok(documentService.uploadDocument(startupId, fileType, file, authentication.getName()));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<?> getDocuments(@PathVariable Long startupId) {
        return ResponseEntity.ok(documentService.getDocuments(startupId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id, Authentication authentication) {
        Resource resource = documentService.downloadDocument(id, authentication.getName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentService.getFileName(id) + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id, Authentication authentication) {
        documentService.deleteDocument(id, authentication.getName());
        return ResponseEntity.ok("Document deleted");
    }
}
