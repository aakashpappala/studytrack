package com.studytrack.service;

import com.studytrack.dto.notification.AnnouncementDto;
import com.studytrack.dto.notification.CreateAnnouncementRequest;
import com.studytrack.entity.*;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.repository.AnnouncementRepository;
import com.studytrack.repository.StudentRepository;
import com.studytrack.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    public AnnouncementService(AnnouncementRepository announcementRepository, UserRepository userRepository, StudentRepository studentRepository, NotificationService notificationService) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    

    public List<AnnouncementDto> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AnnouncementDto createAnnouncement(String adminEmail, CreateAnnouncementRequest request) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + adminEmail));

        Announcement announcement = Announcement.builder()
                .admin(admin)
                .title(request.getTitle())
                .content(request.getContent())
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .build();

        announcement = announcementRepository.save(announcement);

        // Broadcast notification to all active students
        List<Student> activeStudents = studentRepository.findAllActive();
        for (Student s : activeStudents) {
            notificationService.sendNotification(
                    s,
                    "Announcement: " + announcement.getTitle(),
                    announcement.getContent(),
                    NotificationType.ANNOUNCEMENT
            );
        }

        return toDto(announcement);
    }

    public AnnouncementDto toDto(Announcement a) {
        return AnnouncementDto.builder()
                .id(a.getId())
                .adminId(a.getAdmin().getId())
                .adminName(a.getAdmin().getFullName())
                .title(a.getTitle())
                .content(a.getContent())
                .priority(a.getPriority().name())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
