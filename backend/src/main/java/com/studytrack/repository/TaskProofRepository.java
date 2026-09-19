package com.studytrack.repository;

import com.studytrack.entity.TaskProof;
import com.studytrack.entity.TaskSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskProofRepository
        extends JpaRepository<TaskProof, Long> {

    List<TaskProof> findBySubmissionOrderByUploadedAtAsc(
            TaskSubmission submission);
}