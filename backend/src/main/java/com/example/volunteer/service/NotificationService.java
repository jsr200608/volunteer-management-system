package com.example.volunteer.service;

import com.example.volunteer.dto.request.NotificationCreateRequest;
import com.example.volunteer.dto.response.NotificationResponse;
import com.example.volunteer.entity.Notification;
import com.example.volunteer.entity.User;
import com.example.volunteer.repository.NotificationRepository;
import com.example.volunteer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
    }

    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        Notification notification = Notification.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .userId(request.getUserId())
                .read(false)
                .build();
        return toResponse(notificationRepository.save(notification));
    }

    @Transactional
    public void broadcastNotification(NotificationCreateRequest request) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Notification notification = Notification.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .type(request.getType())
                    .userId(user.getId())
                    .read(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        notificationRepository.deleteByIdIn(ids);
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}