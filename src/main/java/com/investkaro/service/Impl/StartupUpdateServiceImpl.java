package com.investkaro.service.Impl;

import com.investkaro.dto.StartupUpdateRequest;
import com.investkaro.dto.StartupUpdateResponse;
import com.investkaro.entity.*;
import com.investkaro.repository.FollowRepository;
import com.investkaro.repository.StartupRepository;
import com.investkaro.repository.StartupUpdateRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.service.NotificationService;
import com.investkaro.service.StartupUpdateService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartupUpdateServiceImpl implements StartupUpdateService {
    private final StartupUpdateRepository startupUpdateRepository;
    private final StartupRepository startupRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationService notificationService;

    public StartupUpdateServiceImpl(StartupUpdateRepository startupUpdateRepository, StartupRepository startupRepository, UserRepository userRepository, FollowRepository followRepository, NotificationService notificationService) {
        this.startupUpdateRepository = startupUpdateRepository;
        this.startupRepository = startupRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.notificationService = notificationService;
    }

    @Override
    public StartupUpdateResponse createUpdate(StartupUpdateRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Startup startup = startupRepository.findById(request.getStartupId())
                .orElseThrow(() -> new EntityNotFoundException("Startup not found"));
        if (!startup.getFounder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to publish updates for this startup");
        }
        StartupUpdate update = new StartupUpdate();
        update.setStartup(startup);
        update.setTitle(request.getTitle());
        update.setDescription(request.getDescription());
        StartupUpdate saved = startupUpdateRepository.save(update);

        followRepository.findByStartup(startup).forEach(follow ->
                notificationService.createNotification(
                        follow.getInvestor().getUser(),
                        "Startup update",
                        startup.getCompanyName() + " published: " + request.getTitle(),
                        NotificationType.STARTUP_UPDATE
                )
        );

        return toResponse(saved);
    }

    @Override
    public List<StartupUpdateResponse> getUpdates(Long startupId) {
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new EntityNotFoundException("Startup not found"));
        return startupUpdateRepository.findByStartupOrderByCreatedAtDesc(startup).stream().map(this::toResponse).toList();
    }

    private StartupUpdateResponse toResponse(StartupUpdate update) {
        return new StartupUpdateResponse(
                update.getId(),
                update.getStartup().getId(),
                update.getStartup().getCompanyName(),
                update.getTitle(),
                update.getDescription(),
                update.getCreatedAt()
        );
    }
}
