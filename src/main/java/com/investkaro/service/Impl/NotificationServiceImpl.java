package com.investkaro.service.Impl;

import com.investkaro.dto.NotificationResponse;
import com.investkaro.entity.Notification;
import com.investkaro.entity.NotificationType;
import com.investkaro.entity.User;
import com.investkaro.repository.NotificationRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createNotification(User user, String title, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getNotifications(String email) {
        User user = getUser(email);
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::toResponse).toList();
    }

    @Override
    public NotificationResponse markAsRead(Long id, String email) {
        User user = getUser(email);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this notification");
        }
        notification.setIsRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public void deleteNotification(Long id, String email) {
        User user = getUser(email);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this notification");
        }
        notificationRepository.delete(notification);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
