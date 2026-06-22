package com.investkaro.repository;

import com.investkaro.entity.Bookmark;
import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByInvestorOrderByCreatedAtDesc(InvestorProfile investor);
    Optional<Bookmark> findByInvestorAndStartup(InvestorProfile investor, Startup startup);
    boolean existsByInvestorAndStartup(InvestorProfile investor, Startup startup);
    void deleteByStartupId(Long startupId);
}
