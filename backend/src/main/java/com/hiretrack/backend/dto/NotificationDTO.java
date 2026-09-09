package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDTO {
    private UUID id;
    private UUID userId;
    private String type;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
}
