package com.studytrack.repository;

import com.studytrack.entity.StudyNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudyNoteRepository extends JpaRepository<StudyNote, Long> {
    List<StudyNote> findByStudentIdOrderByUpdatedAtDesc(Long studentId);
    List<StudyNote> findByStudentIdAndTopicId(Long studentId, Long topicId);
    List<StudyNote> findByStudentIdAndTaskId(Long studentId, Long taskId);
}
