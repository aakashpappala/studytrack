package com.studytrack.service;

import com.studytrack.dto.notification.NotificationDto;
import com.studytrack.entity.Notification;
import com.studytrack.entity.NotificationType;
import com.studytrack.entity.Student;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.exception.UnauthorizedException;
import com.studytrack.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    

    public List<NotificationDto> getNotifications(String studentEmail) {
        return notificationRepository.findByStudentUserEmailOrderByCreatedAtDesc(studentEmail).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(String studentEmail) {
        return notificationRepository.countByStudentUserEmailAndIsReadFalse(studentEmail);
    }

    @Transactional
    public void markAsRead(Long id, String studentEmail) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        if (!notification.getStudent().getUser().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException("You are not authorized to access this notification");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(String studentEmail) {
        List<Notification> notifications = notificationRepository.findByStudentUserEmailOrderByCreatedAtDesc(studentEmail);
        for (Notification n : notifications) {
            n.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void sendNotification(Student student, String title, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .student(student)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    public NotificationDto toDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .studentId(n.getStudent().getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType().name())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
