package com.example.volunteer.controller;

import com.example.volunteer.dto.request.NotificationCreateRequest;
import com.example.volunteer.dto.response.NotificationResponse;
import com.example.volunteer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        Long userId = 1L;
        List<NotificationResponse> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(Map.of("code", 200, "message", "success", "data", notifications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        NotificationResponse notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(Map.of("code", 200, "message", "success", "data", notification));
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody NotificationCreateRequest request) {
        if (request.getUserId() == null) {
            request.setUserId(1L);
        }
        NotificationResponse notification = notificationService.createNotification(request);
        return ResponseEntity.ok(Map.of("code", 200, "message", "创建成功", "data", notification));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<?> broadcastNotification(@RequestBody NotificationCreateRequest request) {
        notificationService.broadcastNotification(request);
        return ResponseEntity.ok(Map.of("code", 200, "message", "广播成功"));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("code", 200, "message", "已标记为已读"));
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead() {
        Long userId = 1L;
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("code", 200, "message", "已全部标记为已读"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(Map.of("code", 200, "message", "删除成功"));
    }

    @DeleteMapping("/batch")
    public ResponseEntity<?> deleteNotifications(@RequestBody List<Long> ids) {
        notificationService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("code", 200, "message", "批量删除成功"));
    }
}