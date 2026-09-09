package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Notification;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public void createNotification(User user, String title, String message, String type, boolean sendEmail) {
        // Prevent exact unread duplicates
        List<Notification> existing = notificationRepository.findAll().stream()
            .filter(n -> n.getUser() != null && n.getUser().getId().equals(user.getId()))
            .filter(n -> n.getTitle().equals(title) && (n.getIsRead() == null || !n.getIsRead()))
            .toList();
            
        if (!existing.isEmpty()) {
            return; 
        }

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        
        notificationRepository.save(notification);

        if (sendEmail && user.getEmail() != null) {
            emailService.sendEmail(user.getEmail(), title, message);
        }
    }
    
    public List<Notification> getUserNotifications(UUID userId) {
        return notificationRepository.findAll().stream()
            .filter(n -> n.getUser() != null && n.getUser().getId().equals(userId))
            .sorted((n1, n2) -> {
                if (n1.getCreatedAt() == null || n2.getCreatedAt() == null) return 0;
                return n2.getCreatedAt().compareTo(n1.getCreatedAt());
            })
            .toList();
    }
    
    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }
    
    public void markAllAsRead(UUID userId) {
        List<Notification> unread = notificationRepository.findAll().stream()
            .filter(n -> n.getUser() != null && n.getUser().getId().equals(userId) && (n.getIsRead() == null || !n.getIsRead()))
            .toList();
            
        for (Notification n : unread) {
            n.setIsRead(true);
        }
        notificationRepository.saveAll(unread);
    }
}
