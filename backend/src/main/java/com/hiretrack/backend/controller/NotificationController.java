package com.hiretrack.backend.controller;

import com.hiretrack.backend.entity.Notification;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.repository.UserRepository;
import com.hiretrack.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notification>> getMyNotifications(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok().build();
    }
}
