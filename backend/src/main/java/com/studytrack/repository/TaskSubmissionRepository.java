package com.studytrack.repository;

import com.studytrack.entity.Task;
import com.studytrack.entity.TaskSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskSubmissionRepository
        extends JpaRepository<TaskSubmission, Long> {

    List<TaskSubmission> findByTaskOrderBySubmittedAtDesc(
            Task task);

    @Modifying
    @Query("DELETE FROM TaskSubmission s WHERE s.task.id = :taskId")
    int deleteAllByTaskId(@Param("taskId") Long taskId);
}