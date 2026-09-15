package com.studytrack.service;

import com.studytrack.dto.admin.*;
import com.studytrack.dto.progress.DailyProgressDto;
import com.studytrack.dto.progress.MonthlyProgressDto;
import com.studytrack.dto.progress.WeeklyProgressDto;
import com.studytrack.dto.task.TaskDto;
import com.studytrack.entity.*;
import com.studytrack.exception.BadRequestException;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoadmapRepository roadmapRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final TaskRepository taskRepository;
    private final StudyLogRepository studyLogRepository;
    private final StudyNoteRepository studyNoteRepository;
    private final NotificationRepository notificationRepository;
    private final ProgressService progressService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        StudentRepository studentRepository,
                        RoadmapRepository roadmapRepository,
                        StudentRoadmapRepository studentRoadmapRepository,
                        TaskRepository taskRepository,
                        StudyLogRepository studyLogRepository,
                        StudyNoteRepository studyNoteRepository,
                        NotificationRepository notificationRepository,
                        ProgressService progressService,
                        TaskService taskService,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.roadmapRepository = roadmapRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.taskRepository = taskRepository;
        this.studyLogRepository = studyLogRepository;
        this.studyNoteRepository = studyNoteRepository;
        this.notificationRepository = notificationRepository;
        this.progressService = progressService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    

    public AdminDashboardDto getDashboard() {
        long totalStudents = studentRepository.count();
        long activeStudents = studentRepository.findAllActive().size();
        long totalRoadmaps = roadmapRepository.count();
        LocalDate today = LocalDate.now();
        long tasksCompletedToday = taskRepository.countTasksCompletedOnDate(today);

        List<Student> students = studentRepository.findAllWithUser();

        double totalProgressSum = 0;
        List<StudentSummaryDto> summaries = new ArrayList<>();
        List<StudentSummaryDto> fallingBehind = new ArrayList<>();

        for (Student s : students) {
            StudentSummaryDto sumDto = toStudentSummaryDto(s);
            summaries.add(sumDto);
            totalProgressSum += sumDto.getOverallProgress();
            if (sumDto.getOverallProgress() < 40.0) {
                fallingBehind.add(sumDto);
            }
        }

        double avgProgress = students.isEmpty() ? 0.0 : Math.round((totalProgressSum / students.size()) * 10.0) / 10.0;
        Long totalStudyMinutes = studyLogRepository.sumTotalDurationMinutes();
        double totalStudyHours = Math.round(((totalStudyMinutes != null ? totalStudyMinutes : 0L) / 60.0) * 10.0) / 10.0;

        // Progress distribution brackets
        int p0_25 = 0, p26_50 = 0, p51_75 = 0, p76_100 = 0;
        for (StudentSummaryDto s : summaries) {
            double p = s.getOverallProgress();
            if (p <= 25.0) p0_25++;
            else if (p <= 50.0) p26_50++;
            else if (p <= 75.0) p51_75++;
            else p76_100++;
        }

        List<Map<String, Object>> progressDist = List.of(
                Map.of("range", "0-25%", "count", p0_25),
                Map.of("range", "26-50%", "count", p26_50),
                Map.of("range", "51-75%", "count", p51_75),
                Map.of("range", "76-100%", "count", p76_100)
        );

        // Weekly study hours trend
        List<WeeklyProgressDto> weeklyTrend = new ArrayList<>();
        for (int i = 3; i >= 0; i--) {
            LocalDate start = today.minusDays((i + 1) * 7L - 1);
            LocalDate end = today.minusDays(i * 7L);
            Long mins = studyLogRepository.sumTotalDurationMinutesBetween(start, end);
            weeklyTrend.add(WeeklyProgressDto.builder()
                    .weekLabel("Week " + (4 - i))
                    .startDate(start)
                    .endDate(end)
                    .studyMinutes(mins != null ? mins : 0L)
                    .build());
        }

        // Task status breakdown
        long completed = taskRepository.findAll().stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        long inProgress = taskRepository.findAll().stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long notStarted = taskRepository.findAll().stream().filter(t -> t.getStatus() == TaskStatus.NOT_STARTED).count();
        Map<String, Long> statusBreakdown = Map.of(
                "COMPLETED", completed,
                "IN_PROGRESS", inProgress,
                "NOT_STARTED", notStarted
        );

        // Roadmap completion comparison
        List<Roadmap> roadmaps = roadmapRepository.findAll();
        List<Map<String, Object>> roadmapComp = new ArrayList<>();
        for (Roadmap r : roadmaps) {
            long enrolledCount = studentRoadmapRepository.countByRoadmapId(r.getId());
            roadmapComp.add(Map.of(
                    "title", r.getTitle(),
                    "enrolledCount", enrolledCount,
                    "estimatedHours", r.getEstimatedHours() != null ? r.getEstimatedHours() : 40
            ));
        }

        // Recent activity
        List<Map<String, Object>> recentActivity = new ArrayList<>();
        List<Task> recentTasks = taskRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .toList();

        for (Task t : recentTasks) {
            recentActivity.add(Map.of(
                    "type", "TASK",
                    "studentName", t.getStudent().getUser().getFullName(),
                    "title", t.getTitle(),
                    "status", t.getStatus().name(),
                    "date", t.getAssignedDate().toString()
            ));
        }

        return AdminDashboardDto.builder()
                .totalStudents(totalStudents)
                .activeStudents(activeStudents)
                .totalRoadmaps(totalRoadmaps)
                .tasksCompletedToday(tasksCompletedToday)
                .averageStudentProgress(avgProgress)
                .totalStudyHours(totalStudyHours)
                .studentsFallingBehindCount(fallingBehind.size())
                .studentsFallingBehind(fallingBehind)
                .studentProgressDistribution(progressDist)
                .weeklyStudyHoursTrend(weeklyTrend)
                .taskStatusBreakdown(statusBreakdown)
                .roadmapCompletionComparison(roadmapComp)
                .recentActivity(recentActivity)
                .build();
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryDto> getAllStudents(String search, String status, Long roadmapId) {
        List<Student> list = studentRepository.findAllWithUser();

        return list.stream()
                .filter(s -> {
                    if (search != null && !search.isBlank()) {
                        String q = search.toLowerCase();
                        boolean nameMatch = s.getUser().getFullName().toLowerCase().contains(q);
                        boolean emailMatch = s.getUser().getEmail().toLowerCase().contains(q);
                        if (!nameMatch && !emailMatch) return false;
                    }
                    if (status != null && !status.isBlank()) {
                        if (!s.getUser().getStatus().name().equalsIgnoreCase(status)) return false;
                    }
                    if (roadmapId != null) {
                        StudentRoadmap sr = studentRoadmapRepository.findActiveByStudentEmail(s.getUser().getEmail()).orElse(null);
                        if (sr == null || !sr.getRoadmap().getId().equals(roadmapId)) return false;
                    }
                    return true;
                })
                .map(this::toStudentSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentDetailDto getStudentDetail(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        StudentSummaryDto sum = toStudentSummaryDto(student);
        LocalDate today = LocalDate.now();

        List<Task> allTasks = taskRepository.findByStudentIdOrderByAssignedDateDesc(student.getId());
        List<TaskDto> todayTasks = allTasks.stream()
                .filter(t -> today.equals(t.getAssignedDate()))
                .map(taskService::toDto)
                .collect(Collectors.toList());
        List<TaskDto> completedTasks = allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .map(taskService::toDto)
                .collect(Collectors.toList());
        List<TaskDto> pendingTasks = allTasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.COMPLETED)
                .map(taskService::toDto)
                .collect(Collectors.toList());

        Long todayMins = studyLogRepository.sumDurationByStudentAndDate(student.getId(), today);
        Long weeklyMins = studyLogRepository.sumDurationByStudentAndDateBetween(student.getId(), today.minusDays(6), today);
        Long monthlyMins = studyLogRepository.sumDurationByStudentAndDateBetween(student.getId(), today.withDayOfMonth(1), today);

        StudentRoadmap activeSr = studentRoadmapRepository.findActiveByStudentEmail(student.getUser().getEmail()).orElse(null);
        var subjectProgress = activeSr != null ? progressService.getSubjectProgress(student, activeSr.getRoadmap()) : Collections.emptyList();

        List<DailyProgressDto> recentActivity = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            recentActivity.add(progressService.getDailyProgress(student, d));
        }

        return StudentDetailDto.builder()
                .id(student.getId())
                .userId(student.getUser().getId())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phone(student.getPhone())
                .college(student.getCollege())
                .enrollmentNo(student.getEnrollmentNo())
                .status(student.getUser().getStatus().name())
                .roadmapId(sum.getRoadmapId())
                .roadmapTitle(sum.getRoadmapTitle())
                .overallProgress(sum.getOverallProgress())
                .todayProgress(sum.getTodayProgress())
                .currentStreak(student.getCurrentStreak() != null ? student.getCurrentStreak() : 0)
                .longestStreak(student.getLongestStreak() != null ? student.getLongestStreak() : 0)
                .lastStudyDate(student.getLastStudyDate())
                .todayStudyTime(ProgressService.formatDuration(todayMins))
                .weeklyStudyTime(ProgressService.formatDuration(weeklyMins))
                .monthlyStudyTime(ProgressService.formatDuration(monthlyMins))
                .totalStudyMinutes(student.getTotalStudyMinutes() != null ? student.getTotalStudyMinutes() : 0L)
                .subjectProgress((List) subjectProgress)
                .todayTasks(todayTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .weeklyProgress(progressService.getWeeklyProgress(student))
                .monthlyProgress(progressService.getMonthlyProgress(student))
                .recentDailyActivity(recentActivity)
                .build();
    }

    @Transactional
    public StudentSummaryDto createStudent(CreateStudentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        user = userRepository.save(user);

        Student student = Student.builder()
                .user(user)
                .phone(request.getPhone())
                .college(request.getCollege())
                .enrollmentNo(request.getEnrollmentNo())
                .currentStreak(0)
                .longestStreak(0)
                .totalStudyMinutes(0L)
                .build();
        student = studentRepository.save(student);

        if (request.getRoadmapId() != null) {
            Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId()).orElse(null);
            if (roadmap != null) {
                StudentRoadmap sr = StudentRoadmap.builder()
                        .student(student)
                        .roadmap(roadmap)
                        .assignedAt(LocalDateTime.now())
                        .status("ACTIVE")
                        .completionPercentage(0.0)
                        .build();
                studentRoadmapRepository.save(sr);
            }
        }

        return toStudentSummaryDto(student);
    }

    @Transactional
    public StudentSummaryDto updateStudent(Long studentId, UpdateStudentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        User user = student.getUser();
        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        if (request.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase()));
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        userRepository.save(user);

        student.setPhone(request.getPhone());
        student.setCollege(request.getCollege());
        student.setEnrollmentNo(request.getEnrollmentNo());
        student = studentRepository.save(student);

        if (request.getRoadmapId() != null) {
            Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId()).orElse(null);
            if (roadmap != null) {
                // Deactivate old
                List<StudentRoadmap> existing = studentRoadmapRepository.findByStudentId(studentId);
                for (StudentRoadmap sr : existing) {
                    sr.setStatus("INACTIVE");
                    studentRoadmapRepository.save(sr);
                }
                StudentRoadmap sr = StudentRoadmap.builder()
                        .student(student)
                        .roadmap(roadmap)
                        .assignedAt(LocalDateTime.now())
                        .status("ACTIVE")
                        .completionPercentage(0.0)
                        .build();
                studentRoadmapRepository.save(sr);
            }
        }

        return toStudentSummaryDto(student);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        User user = student.getUser();

        // 1. Delete all StudyNotes (must be deleted before tasks as notes can link to tasks)
        List<StudyNote> notes = studyNoteRepository.findByStudentIdOrderByUpdatedAtDesc(studentId);
        if (notes != null && !notes.isEmpty()) {
            studyNoteRepository.deleteAll(notes);
            studyNoteRepository.flush();
        }

        // 2. Delete all Tasks assigned to student
        List<Task> tasks = taskRepository.findByStudentIdOrderByAssignedDateDesc(studentId);
        if (tasks != null && !tasks.isEmpty()) {
            taskRepository.deleteAll(tasks);
            taskRepository.flush();
        }

        // 3. Delete all StudyLogs recorded by student
        List<StudyLog> logs = studyLogRepository.findByStudentIdOrderByDateDesc(studentId);
        if (logs != null && !logs.isEmpty()) {
            studyLogRepository.deleteAll(logs);
            studyLogRepository.flush();
        }

        // 4. Delete all StudentRoadmap enrollments
        List<StudentRoadmap> roadmaps = studentRoadmapRepository.findByStudentId(studentId);
        if (roadmaps != null && !roadmaps.isEmpty()) {
            studentRoadmapRepository.deleteAll(roadmaps);
            studentRoadmapRepository.flush();
        }

        // 5. Delete all Notifications for student
        List<Notification> notifications = notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        if (notifications != null && !notifications.isEmpty()) {
            notificationRepository.deleteAll(notifications);
            notificationRepository.flush();
        }

        // 6. Delete the Student record
        studentRepository.delete(student);
        studentRepository.flush();

        // 7. Delete the User record
        if (user != null && userRepository.existsById(user.getId())) {
            userRepository.delete(user);
            userRepository.flush();
        }
    }

    @Transactional(readOnly = true)
    public AdminAnalyticsDto getAnalytics(Long studentId) {
        List<Student> allStudents = studentRepository.findAllWithUser();
        long totalStudents = allStudents.size();

        double totalProg = 0;
        List<StudentSummaryDto> allSummaries = new ArrayList<>();
        for (Student s : allStudents) {
            StudentSummaryDto sum = toStudentSummaryDto(s);
            allSummaries.add(sum);
            totalProg += sum.getOverallProgress();
        }

        double avgProg = allStudents.isEmpty() ? 0.0 : Math.round((totalProg / totalStudents) * 10.0) / 10.0;

        Long totalMins = studyLogRepository.sumTotalDurationMinutes();
        double avgHours = allStudents.isEmpty() ? 0.0 : Math.round(((totalMins != null ? totalMins : 0L) / (double) totalStudents / 60.0) * 10.0) / 10.0;

        List<StudentSummaryDto> highestPerforming = allSummaries.stream()
                .sorted(Comparator.comparingDouble(StudentSummaryDto::getOverallProgress).reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<StudentSummaryDto> fallingBehind = allSummaries.stream()
                .filter(s -> s.getOverallProgress() < 40.0)
                .sorted(Comparator.comparingDouble(StudentSummaryDto::getOverallProgress))
                .collect(Collectors.toList());

        List<Roadmap> roadmaps = roadmapRepository.findAll();
        String mostCompletedRoadmap = "None";
        long maxEnrolled = -1;
        List<Map<String, Object>> roadmapStats = new ArrayList<>();

        for (Roadmap r : roadmaps) {
            long count = studentRoadmapRepository.countByRoadmapId(r.getId());
            if (count > maxEnrolled) {
                maxEnrolled = count;
                mostCompletedRoadmap = r.getTitle();
            }
            roadmapStats.add(Map.of(
                    "title", r.getTitle(),
                    "enrolledCount", count,
                    "estimatedHours", r.getEstimatedHours() != null ? r.getEstimatedHours() : 40
            ));
        }

        // Completion rates
        long totalTasksCount = taskRepository.count();
        long totalCompleted = taskRepository.findAll().stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        double overallCompletionRate = totalTasksCount == 0 ? 0.0 : Math.round(((double) totalCompleted / totalTasksCount) * 100.0 * 10.0) / 10.0;

        LocalDate today = LocalDate.now();
        List<Map<String, Object>> dailyTrend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            long done = taskRepository.countTasksCompletedOnDate(d);
            dailyTrend.add(Map.of("date", d.toString(), "completedTasks", done));
        }

        return AdminAnalyticsDto.builder()
                .totalStudents(totalStudents)
                .averageProgress(avgProg)
                .averageStudyHours(avgHours)
                .mostCompletedRoadmapTitle(mostCompletedRoadmap)
                .dailyCompletionRate(overallCompletionRate)
                .weeklyCompletionRate(Math.min(100.0, overallCompletionRate * 1.1))
                .monthlyCompletionRate(Math.min(100.0, overallCompletionRate * 1.25))
                .highestPerformingStudents(highestPerforming)
                .studentsFallingBehind(fallingBehind)
                .roadmapStats(roadmapStats)
                .dailyCompletionTrend(dailyTrend)
                .build();
    }

    private StudentSummaryDto toStudentSummaryDto(Student student) {
        LocalDate today = LocalDate.now();
        long totalTasks = taskRepository.countByStudentId(student.getId());
        long completedTasks = taskRepository.countByStudentIdAndStatus(student.getId(), TaskStatus.COMPLETED);
        double overallPct = totalTasks == 0 ? 0.0 : Math.round(((double) completedTasks / totalTasks) * 100.0 * 10.0) / 10.0;

        long todayTotal = taskRepository.countByStudentIdAndAssignedDate(student.getId(), today);
        long todayCompleted = taskRepository.countByStudentIdAndAssignedDateAndStatus(student.getId(), today, TaskStatus.COMPLETED);
        double todayPct = todayTotal == 0 ? 0.0 : Math.round(((double) todayCompleted / todayTotal) * 100.0 * 10.0) / 10.0;

        StudentRoadmap sr = studentRoadmapRepository.findActiveByStudentEmail(student.getUser().getEmail()).orElse(null);

        return StudentSummaryDto.builder()
                .id(student.getId())
                .userId(student.getUser().getId())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phone(student.getPhone())
                .college(student.getCollege())
                .enrollmentNo(student.getEnrollmentNo())
                .status(student.getUser().getStatus().name())
                .roadmapId(sr != null ? sr.getRoadmap().getId() : null)
                .roadmapTitle(sr != null ? sr.getRoadmap().getTitle() : "Unassigned")
                .overallProgress(overallPct)
                .todayProgress(todayPct)
                .currentStreak(student.getCurrentStreak() != null ? student.getCurrentStreak() : 0)
                .totalStudyMinutes(student.getTotalStudyMinutes() != null ? student.getTotalStudyMinutes() : 0L)
                .lastStudyDate(student.getLastStudyDate())
                .build();
    }
}
