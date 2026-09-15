package com.studytrack.service;

import com.studytrack.dto.studylog.CreateStudyLogRequest;
import com.studytrack.dto.studylog.StudyLogDto;
import com.studytrack.entity.Student;
import com.studytrack.entity.StudyLog;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.exception.UnauthorizedException;
import com.studytrack.repository.StudyLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final StudentService studentService;

    public StudyLogService(StudyLogRepository studyLogRepository, StudentService studentService) {
        this.studyLogRepository = studyLogRepository;
        this.studentService = studentService;
    }

    @Transactional
    

    public StudyLogDto createStudyLog(String studentEmail, CreateStudyLogRequest request) {
        Student student = studentService.getStudentByEmail(studentEmail);

        LocalDate date = request.getDate() != null ? request.getDate() : LocalDate.now();

        StudyLog log = StudyLog.builder()
                .student(student)
                .date(date)
                .topicName(request.getTopicName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .build();

        log = studyLogRepository.save(log);

        // Update student streak and total study minutes
        studentService.recordActivityAndCalculateStreak(student, request.getDurationMinutes(), date);

        return toDto(log);
    }

    @Transactional(readOnly = true)
    public List<StudyLogDto> getStudyLogs(String studentEmail) {
        Student student = studentService.getStudentByEmail(studentEmail);
        return studyLogRepository.findByStudentIdOrderByDateDesc(student.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudyLogDto> getStudyLogsByStudentId(Long studentId) {
        return studyLogRepository.findByStudentIdOrderByDateDesc(studentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStudyLog(Long logId, String studentEmail) {
        StudyLog log = studyLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Study log not found with id: " + logId));

        if (!log.getStudent().getUser().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException("You are not authorized to delete this study log");
        }

        studyLogRepository.delete(log);
    }

    public StudyLogDto toDto(StudyLog log) {
        return StudyLogDto.builder()
                .id(log.getId())
                .studentId(log.getStudent().getId())
                .date(log.getDate())
                .topicName(log.getTopicName())
                .startTime(log.getStartTime())
                .endTime(log.getEndTime())
                .durationMinutes(log.getDurationMinutes())
                .notes(log.getNotes())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
