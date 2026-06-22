package com.investkaro.repository;

import com.investkaro.entity.Conversation;
import com.investkaro.entity.FounderProfile;
import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByInvestorOrderByCreatedAtDesc(InvestorProfile investor);
    List<Conversation> findByFounderOrderByCreatedAtDesc(FounderProfile founder);
    Optional<Conversation> findByInvestorAndFounderAndStartup(InvestorProfile investor, FounderProfile founder, Startup startup);
    void deleteByStartupId(Long startupId);
}
