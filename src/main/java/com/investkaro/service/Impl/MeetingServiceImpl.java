package com.investkaro.service.Impl;

import com.investkaro.dto.MeetingRequest;
import com.investkaro.dto.MeetingResponse;
import com.investkaro.dto.MeetingStatusRequest;
import com.investkaro.entity.*;
import com.investkaro.repository.*;
import com.investkaro.service.MeetingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;
    private final FounderRepository founderRepository;
    private final UserRepository userRepository;

    public MeetingServiceImpl(MeetingRepository meetingRepository, StartupRepository startupRepository, InvestorRepository investorRepository, FounderRepository founderRepository, UserRepository userRepository) {
        this.meetingRepository = meetingRepository;
        this.startupRepository = startupRepository;
        this.investorRepository = investorRepository;
        this.founderRepository = founderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MeetingResponse requestMeeting(MeetingRequest request, String email) {
        User user = getUser(email);
        InvestorProfile investor = investorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Investor not found"));
        Startup startup = startupRepository.findById(request.getStartupId())
                .orElseThrow(() -> new EntityNotFoundException("Startup not found"));
        Meeting meeting = new Meeting();
        meeting.setInvestor(investor);
        meeting.setFounder(startup.getFounder());
        meeting.setStartup(startup);
        meeting.setMeetingDate(request.getMeetingDate());
        meeting.setStatus(MeetingStatus.PENDING);
        return toResponse(meetingRepository.save(meeting));
    }

    @Override
    public List<MeetingResponse> getMeetings(String email) {
        User user = getUser(email);
        if (user.getRole() == Role.INVESTOR) {
            InvestorProfile investor = investorRepository.findByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException("Investor not found"));
            return meetingRepository.findByInvestorOrderByMeetingDateDesc(investor).stream().map(this::toResponse).toList();
        }
        if (user.getRole() == Role.FOUNDER) {
            FounderProfile founder = founderRepository.findByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException("Founder not found"));
            return meetingRepository.findByFounderOrderByMeetingDateDesc(founder).stream().map(this::toResponse).toList();
        }
        throw new RuntimeException("Only founders and investors can view meetings");
    }

    @Override
    public MeetingResponse updateMeeting(Long id, MeetingStatusRequest request, String email) {
        User user = getUser(email);
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found"));
        if (!meeting.getFounder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only the founder can update meeting status");
        }
        meeting.setStatus(request.getStatus());
        return toResponse(meetingRepository.save(meeting));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private MeetingResponse toResponse(Meeting meeting) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getInvestor().getId(),
                meeting.getInvestor().getUser().getFullName(),
                meeting.getFounder().getId(),
                meeting.getFounder().getUser().getFullName(),
                meeting.getStartup().getId(),
                meeting.getStartup().getCompanyName(),
                meeting.getMeetingDate(),
                meeting.getStatus()
        );
    }
}
