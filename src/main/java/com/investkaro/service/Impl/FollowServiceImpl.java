package com.investkaro.service.Impl;

import com.investkaro.dto.FollowResponse;
import com.investkaro.entity.*;
import com.investkaro.repository.*;
import com.investkaro.service.FollowService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, StartupRepository startupRepository, InvestorRepository investorRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.startupRepository = startupRepository;
        this.investorRepository = investorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FollowResponse followStartup(Long startupId, String email) {
        InvestorProfile investor = getInvestor(email);
        Startup startup = getStartup(startupId);
        Follow follow = followRepository.findByInvestorAndStartup(investor, startup).orElseGet(() -> {
            Follow item = new Follow();
            item.setInvestor(investor);
            item.setStartup(startup);
            return followRepository.save(item);
        });
        return toResponse(follow);
    }

    @Override
    public List<FollowResponse> getFollows(String email) {
        InvestorProfile investor = getInvestor(email);
        return followRepository.findByInvestor(investor).stream().map(this::toResponse).toList();
    }

    @Transactional
    @Override
    public void unfollowStartup(Long startupId, String email) {
        InvestorProfile investor = getInvestor(email);
        Startup startup = getStartup(startupId);
        Follow follow = followRepository.findByInvestorAndStartup(investor, startup)
                .orElseThrow(() -> new EntityNotFoundException("Follow not found"));
        followRepository.delete(follow);
    }

    private InvestorProfile getInvestor(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return investorRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("Investor not found"));
    }

    private Startup getStartup(Long startupId) {
        return startupRepository.findById(startupId).orElseThrow(() -> new EntityNotFoundException("Startup not found"));
    }

    private FollowResponse toResponse(Follow follow) {
        Startup startup = follow.getStartup();
        return new FollowResponse(
                follow.getId(),
                startup.getId(),
                startup.getCompanyName(),
                startup.getIndustry() != null ? startup.getIndustry().getName() : null,
                startup.getAmountRequired()
        );
    }
}
