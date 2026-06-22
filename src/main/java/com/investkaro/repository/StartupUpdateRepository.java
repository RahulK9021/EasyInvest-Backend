package com.investkaro.repository;

import com.investkaro.entity.Startup;
import com.investkaro.entity.StartupUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StartupUpdateRepository extends JpaRepository<StartupUpdate, Long> {
    List<StartupUpdate> findByStartupOrderByCreatedAtDesc(Startup startup);
    void deleteByStartupId(Long startupId);
}
