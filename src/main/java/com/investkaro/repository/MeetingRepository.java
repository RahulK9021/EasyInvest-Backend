package com.investkaro.repository;

import com.investkaro.entity.FounderProfile;
import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByInvestorOrderByMeetingDateDesc(InvestorProfile investor);
    List<Meeting> findByFounderOrderByMeetingDateDesc(FounderProfile founder);
    void deleteByStartupId(Long startupId);
}
