package com.studytrack.repository;

import com.studytrack.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByModuleIdOrderByOrderIndexAsc(Long moduleId);
    long countByModuleSubjectRoadmapId(Long roadmapId);
}
