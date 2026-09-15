package com.studytrack.service;

import com.studytrack.dto.progress.*;
import com.studytrack.entity.*;
import com.studytrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProgressService {

    private final TaskRepository taskRepository;
    private final StudyLogRepository studyLogRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;

    public ProgressService(TaskRepository taskRepository, StudyLogRepository studyLogRepository, StudentRoadmapRepository studentRoadmapRepository, SubjectRepository subjectRepository, TopicRepository topicRepository) {
        this.taskRepository = taskRepository;
        this.studyLogRepository = studyLogRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional(readOnly = true)
    

    public StudentProgressSummaryDto getStudentProgressSummary(Student student) {
        LocalDate today = LocalDate.now();

        // Today's tasks
        long todayTotal = taskRepository.countByStudentIdAndAssignedDate(student.getId(), today);
        long todayCompleted = taskRepository.countByStudentIdAndAssignedDateAndStatus(student.getId(), today, TaskStatus.COMPLETED);
        double todayPct = todayTotal == 0 ? 0.0 : Math.round(((double) todayCompleted / todayTotal) * 100.0 * 10.0) / 10.0;

        // Overall tasks
        long totalTasks = taskRepository.countByStudentId(student.getId());
        long completedTasks = taskRepository.countByStudentIdAndStatus(student.getId(), TaskStatus.COMPLETED);
        double overallPct = totalTasks == 0 ? 0.0 : Math.round(((double) completedTasks / totalTasks) * 100.0 * 10.0) / 10.0;

        // Active roadmap
        StudentRoadmap activeSr = studentRoadmapRepository.findActiveByStudentEmail(student.getUser().getEmail()).orElse(null);
        String roadmapTitle = activeSr != null ? activeSr.getRoadmap().getTitle() : "No Roadmap Assigned";

        // Study times
        Long todayMinutes = studyLogRepository.sumDurationByStudentAndDate(student.getId(), today);
        Long weeklyMinutes = studyLogRepository.sumDurationByStudentAndDateBetween(
                student.getId(), today.minusDays(6), today);
        Long monthlyMinutes = studyLogRepository.sumDurationByStudentAndDateBetween(
                student.getId(), today.withDayOfMonth(1), today);

        // Subject progress
        List<SubjectProgressDto> subjectProgress = new ArrayList<>();
        if (activeSr != null) {
            subjectProgress = getSubjectProgress(student, activeSr.getRoadmap());
        }

        return StudentProgressSummaryDto.builder()
                .studentId(student.getId())
                .studentName(student.getUser().getFullName())
                .roadmapTitle(roadmapTitle)
                .overallProgress(overallPct)
                .todayProgress(todayPct)
                .todayTotalTasks((int) todayTotal)
                .todayCompletedTasks((int) todayCompleted)
                .totalTasks((int) totalTasks)
                .completedTasks((int) completedTasks)
                .currentStreak(student.getCurrentStreak() != null ? student.getCurrentStreak() : 0)
                .longestStreak(student.getLongestStreak() != null ? student.getLongestStreak() : 0)
                .lastStudyDate(student.getLastStudyDate())
                .todayStudyTime(formatDuration(todayMinutes))
                .weeklyStudyTime(formatDuration(weeklyMinutes))
                .monthlyStudyTime(formatDuration(monthlyMinutes))
                .totalStudyMinutes(student.getTotalStudyMinutes() != null ? student.getTotalStudyMinutes() : 0L)
                .subjectProgress(subjectProgress)
                .build();
    }

    @Transactional(readOnly = true)
    public DailyProgressDto getDailyProgress(Student student, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }

        long total = taskRepository.countByStudentIdAndAssignedDate(student.getId(), date);
        long completed = taskRepository.countByStudentIdAndAssignedDateAndStatus(student.getId(), date, TaskStatus.COMPLETED);
        double pct = total == 0 ? 0.0 : Math.round(((double) completed / total) * 100.0 * 10.0) / 10.0;
        Long studyMinutes = studyLogRepository.sumDurationByStudentAndDate(student.getId(), date);

        return DailyProgressDto.builder()
                .date(date)
                .totalTasks((int) total)
                .completedTasks((int) completed)
                .progressPercentage(pct)
                .studyMinutes(studyMinutes != null ? studyMinutes : 0L)
                .build();
    }

    @Transactional(readOnly = true)
    public List<WeeklyProgressDto> getWeeklyProgress(Student student) {
        List<WeeklyProgressDto> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");

        for (int i = 3; i >= 0; i--) {
            LocalDate start = today.minusDays((i + 1) * 7L - 1);
            LocalDate end = today.minusDays(i * 7L);

            List<Task> tasks = taskRepository.findByStudentIdOrderByAssignedDateDesc(student.getId());
            long completed = tasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.COMPLETED &&
                            t.getAssignedDate() != null &&
                            !t.getAssignedDate().isBefore(start) &&
                            !t.getAssignedDate().isAfter(end))
                    .count();

            Long minutes = studyLogRepository.sumDurationByStudentAndDateBetween(student.getId(), start, end);

            list.add(WeeklyProgressDto.builder()
                    .weekLabel(start.format(dtf) + " - " + end.format(dtf))
                    .startDate(start)
                    .endDate(end)
                    .completedTasks((int) completed)
                    .studyMinutes(minutes != null ? minutes : 0L)
                    .progressPercentage(tasks.isEmpty() ? 0.0 : Math.round(((double) completed / tasks.size()) * 100.0 * 10.0) / 10.0)
                    .build());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<MonthlyProgressDto> getMonthlyProgress(Student student) {
        List<MonthlyProgressDto> list = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            LocalDate start = monthDate.withDayOfMonth(1);
            LocalDate end = monthDate.withDayOfMonth(monthDate.lengthOfMonth());

            List<Task> tasks = taskRepository.findByStudentIdOrderByAssignedDateDesc(student.getId());
            long completed = tasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.COMPLETED &&
                            t.getAssignedDate() != null &&
                            !t.getAssignedDate().isBefore(start) &&
                            !t.getAssignedDate().isAfter(end))
                    .count();

            Long minutes = studyLogRepository.sumDurationByStudentAndDateBetween(student.getId(), start, end);

            list.add(MonthlyProgressDto.builder()
                    .monthName(monthDate.getMonth().name().substring(0, 3))
                    .year(monthDate.getYear())
                    .completedTasks((int) completed)
                    .studyMinutes(minutes != null ? minutes : 0L)
                    .build());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<SubjectProgressDto> getSubjectProgress(Student student, Roadmap roadmap) {
        List<Subject> subjects = subjectRepository.findByRoadmapIdOrderByOrderIndexAsc(roadmap.getId());
        List<SubjectProgressDto> list = new ArrayList<>();

        for (Subject s : subjects) {
            List<Task> subjectTasks = taskRepository.findByStudentIdAndSubjectId(student.getId(), s.getId());
            long completed = subjectTasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
            int total = subjectTasks.size();

            double pct = total == 0 ? 0.0 : Math.round(((double) completed / total) * 100.0 * 10.0) / 10.0;

            list.add(SubjectProgressDto.builder()
                    .subjectId(s.getId())
                    .subjectTitle(s.getTitle())
                    .totalTopics(total)
                    .completedTopics((int) completed)
                    .progressPercentage(pct)
                    .build());
        }
        return list;
    }

    public static String formatDuration(Long totalMinutes) {
        if (totalMinutes == null || totalMinutes <= 0) {
            return "0h 0m";
        }
        long hours = totalMinutes / 60;
        long mins = totalMinutes % 60;
        return hours + "h " + mins + "m";
    }
}
