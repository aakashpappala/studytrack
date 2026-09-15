package com.studytrack.repository;

import com.studytrack.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Notification> findByStudentUserEmailOrderByCreatedAtDesc(String email);
    long countByStudentIdAndIsReadFalse(Long studentId);
    long countByStudentUserEmailAndIsReadFalse(String email);
}
