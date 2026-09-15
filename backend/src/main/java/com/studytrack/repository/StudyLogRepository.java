package com.studytrack.repository;

import com.studytrack.entity.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
    List<StudyLog> findByStudentIdOrderByDateDesc(Long studentId);
    List<StudyLog> findByStudentIdAndDate(Long studentId, LocalDate date);
    List<StudyLog> findByStudentIdAndDateBetweenOrderByDateAsc(Long studentId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(l.durationMinutes), 0) FROM StudyLog l WHERE l.student.id = :studentId AND l.date = :date")
    Long sumDurationByStudentAndDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(l.durationMinutes), 0) FROM StudyLog l WHERE l.student.id = :studentId AND l.date BETWEEN :startDate AND :endDate")
    Long sumDurationByStudentAndDateBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(l.durationMinutes), 0) FROM StudyLog l")
    Long sumTotalDurationMinutes();

    @Query("SELECT COALESCE(SUM(l.durationMinutes), 0) FROM StudyLog l WHERE l.date BETWEEN :startDate AND :endDate")
    Long sumTotalDurationMinutesBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
