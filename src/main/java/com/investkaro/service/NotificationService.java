package com.investkaro.service;

import com.investkaro.dto.NotificationResponse;
import com.investkaro.entity.NotificationType;
import com.investkaro.entity.User;

import java.util.List;

public interface NotificationService {
    void createNotification(User user, String title, String message, NotificationType type);
    List<NotificationResponse> getNotifications(String email);
    NotificationResponse markAsRead(Long id, String email);
    void deleteNotification(Long id, String email);
}
