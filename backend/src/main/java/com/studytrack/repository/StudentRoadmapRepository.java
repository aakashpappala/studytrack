package com.studytrack.repository;

import com.studytrack.entity.StudentRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRoadmapRepository extends JpaRepository<StudentRoadmap, Long> {

    List<StudentRoadmap> findByStudentId(Long studentId);

    Optional<StudentRoadmap> findByStudentIdAndStatus(
            Long studentId,
            String status);

    Optional<StudentRoadmap> findByStudentIdAndRoadmapId(
            Long studentId,
            Long roadmapId);

    @Query("SELECT sr FROM StudentRoadmap sr WHERE sr.student.user.email = :email AND sr.status = 'ACTIVE'")
    Optional<StudentRoadmap> findActiveByStudentEmail(
            @Param("email") String email);

    long countByRoadmapId(Long roadmapId);

    void deleteByRoadmapId(Long roadmapId);
}
