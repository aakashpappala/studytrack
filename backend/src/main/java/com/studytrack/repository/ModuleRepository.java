package com.studytrack.repository;

import com.studytrack.entity.RoadmapModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<RoadmapModule, Long> {
    List<RoadmapModule> findBySubjectIdOrderByOrderIndexAsc(Long subjectId);
}
