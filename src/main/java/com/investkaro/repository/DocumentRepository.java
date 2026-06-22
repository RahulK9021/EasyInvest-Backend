package com.investkaro.repository;

import com.investkaro.entity.Document;
import com.investkaro.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStartupOrderByUploadedAtDesc(Startup startup);
    void deleteByStartupId(Long startupId);
}
