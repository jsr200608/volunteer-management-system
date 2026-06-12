package com.example.volunteer.dto.request;

import lombok.Data;

@Data
public class NotificationCreateRequest {
    private String title;
    private String content;
    private String type;
    private Long userId;
}