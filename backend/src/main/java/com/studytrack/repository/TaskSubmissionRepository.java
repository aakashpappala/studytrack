package com.studytrack.repository;

import com.studytrack.entity.Task;
import com.studytrack.entity.TaskSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskSubmissionRepository
        extends JpaRepository<TaskSubmission, Long> {

    List<TaskSubmission> findByTaskOrderBySubmittedAtDesc(Task task);
}