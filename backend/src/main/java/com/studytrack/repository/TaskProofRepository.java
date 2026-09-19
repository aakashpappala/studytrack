package com.studytrack.repository;

import com.studytrack.entity.Task;
import com.studytrack.entity.TaskProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskProofRepository extends JpaRepository<TaskProof, Long> {

    List<TaskProof> findByTask(Task task);
}