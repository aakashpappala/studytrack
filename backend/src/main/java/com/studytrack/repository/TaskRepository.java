package com.studytrack.repository;

import com.studytrack.entity.Task;
import com.studytrack.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStudentIdOrderByAssignedDateDesc(Long studentId);

    List<Task> findByStudentIdAndAssignedDate(Long studentId, LocalDate date);

    List<Task> findByStudentIdAndDueDate(Long studentId, LocalDate date);

    List<Task> findByStudentIdAndStatus(Long studentId, TaskStatus status);

    // Admin: get tasks waiting for verification
    List<Task> findByStatus(TaskStatus status);

    List<Task> findByStudentIdAndRoadmapId(Long studentId, Long roadmapId);

    List<Task> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    long countByStudentId(Long studentId);

    long countByStudentIdAndStatus(Long studentId, TaskStatus status);

    long countByStudentIdAndAssignedDate(Long studentId, LocalDate assignedDate);

    long countByStudentIdAndAssignedDateAndStatus(
            Long studentId,
            LocalDate assignedDate,
            TaskStatus status
    );

    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE t.status = 'COMPLETED' " +
            "AND CAST(t.completedAt AS date) = :date")
    long countTasksCompletedOnDate(@Param("date") LocalDate date);

    @Query("SELECT t FROM Task t " +
            "WHERE t.student.user.email = :email " +
            "ORDER BY t.assignedDate DESC")
    List<Task> findByStudentEmail(@Param("email") String email);

    @Query("SELECT t FROM Task t " +
            "WHERE t.student.user.email = :email " +
            "AND t.assignedDate = :date")
    List<Task> findByStudentEmailAndAssignedDate(
            @Param("email") String email,
            @Param("date") LocalDate date
    );
}