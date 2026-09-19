package com.studytrack.repository;

import com.studytrack.entity.TaskProof;
import com.studytrack.entity.TaskSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskProofRepository
        extends JpaRepository<TaskProof, Long> {

    List<TaskProof> findBySubmissionOrderByUploadedAtAsc(
            TaskSubmission submission);

    @Modifying
    @Query("DELETE FROM TaskProof p WHERE p.task.id = :taskId")
    int deleteAllByTaskId(@Param("taskId") Long taskId);
}