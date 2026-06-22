package com.investkaro.service;

import com.investkaro.dto.DocumentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentResponse uploadDocument(Long startupId, String fileType, MultipartFile file, String email);
    List<DocumentResponse> getDocuments(Long startupId);
    Resource downloadDocument(Long id, String email);
    String getFileName(Long id);
    void deleteDocument(Long id, String email);
}
