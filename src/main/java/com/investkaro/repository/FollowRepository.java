package com.investkaro.repository;

import com.investkaro.entity.Follow;
import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByInvestor(InvestorProfile investor);
    List<Follow> findByStartup(Startup startup);
    Optional<Follow> findByInvestorAndStartup(InvestorProfile investor, Startup startup);
    boolean existsByInvestorAndStartup(InvestorProfile investor, Startup startup);
    void deleteByStartupId(Long startupId);
}
