package com.studytrack.service;

import com.studytrack.dto.task.CreateTaskRequest;
import com.studytrack.dto.task.TaskDto;
import com.studytrack.dto.task.TaskProofDto;
import com.studytrack.dto.task.TaskSubmissionDto;
import com.studytrack.entity.*;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.exception.UnauthorizedException;
import com.studytrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProofRepository taskProofRepository;
    private final TaskSubmissionRepository taskSubmissionRepository;
    private final StudentRepository studentRepository;
    private final RoadmapRepository roadmapRepository;
    private final SubjectRepository subjectRepository;
    private final ModuleRepository moduleRepository;
    private final TopicRepository topicRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final NotificationRepository notificationRepository;
    private final StudentService studentService;

    public TaskService(
            TaskRepository taskRepository,
            TaskProofRepository taskProofRepository,
            TaskSubmissionRepository taskSubmissionRepository,
            StudentRepository studentRepository,
            RoadmapRepository roadmapRepository,
            SubjectRepository subjectRepository,
            ModuleRepository moduleRepository,
            TopicRepository topicRepository,
            StudentRoadmapRepository studentRoadmapRepository,
            NotificationRepository notificationRepository,
            StudentService studentService) {

        this.taskRepository = taskRepository;
        this.taskProofRepository = taskProofRepository;
        this.taskSubmissionRepository = taskSubmissionRepository;
        this.studentRepository = studentRepository;
        this.roadmapRepository = roadmapRepository;
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
        this.topicRepository = topicRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.notificationRepository = notificationRepository;
        this.studentService = studentService;
    }

    // ============================================================
    // CREATE TASK
    // ============================================================

    @Transactional
    public TaskDto createTask(CreateTaskRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: "
                                        + request.getStudentId()));

        Roadmap roadmap = request.getRoadmapId() != null
                ? roadmapRepository.findById(request.getRoadmapId()).orElse(null)
                : null;

        Subject subject = request.getSubjectId() != null
                ? subjectRepository.findById(request.getSubjectId()).orElse(null)
                : null;

        RoadmapModule module = request.getModuleId() != null
                ? moduleRepository.findById(request.getModuleId()).orElse(null)
                : null;

        Topic topic = request.getTopicId() != null
                ? topicRepository.findById(request.getTopicId()).orElse(null)
                : null;

        LocalDate assigned =
                request.getAssignedDate() != null
                        ? request.getAssignedDate()
                        : LocalDate.now();

        Task task = Task.builder()
                .student(student)
                .roadmap(roadmap)
                .subject(subject)
                .module(module)
                .topic(topic)
                .title(request.getTitle())
                .description(request.getDescription())
                .assignedDate(assigned)
                .dueDate(request.getDueDate())
                .estimatedDurationMinutes(
                        request.getEstimatedDurationMinutes() != null
                                ? request.getEstimatedDurationMinutes()
                                : 60)
                .priority(
                        request.getPriority() != null
                                ? request.getPriority()
                                : TaskPriority.MEDIUM)
                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : TaskStatus.NOT_STARTED)
                .build();

        task = taskRepository.save(task);

        Notification notification = Notification.builder()
                .student(student)
                .title("New Task Assigned")
                .message(
                        "A new task has been assigned: "
                                + task.getTitle())
                .type(NotificationType.TASK_ASSIGNED)
                .build();

        notificationRepository.save(notification);

        return toDto(task);
    }

    // ============================================================
    // STUDENT - GET ALL TASKS
    // ============================================================

    @Transactional(readOnly = true)
    public List<TaskDto> getTasksForStudent(String studentEmail) {

        return taskRepository.findByStudentEmail(studentEmail)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // STUDENT - GET TODAY TASKS
    // ============================================================

    @Transactional(readOnly = true)
    public List<TaskDto> getTodayTasksForStudent(String studentEmail) {

        LocalDate today = LocalDate.now();

        return taskRepository
                .findByStudentEmailAndAssignedDate(
                        studentEmail,
                        today)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // ADMIN - GET ALL TASKS
    // ============================================================

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasksForAdmin(
            Long studentId,
            LocalDate date) {

        List<Task> tasks;

        if (studentId != null && date != null) {

            tasks = taskRepository
                    .findByStudentIdAndAssignedDate(
                            studentId,
                            date);

        } else if (studentId != null) {

            tasks = taskRepository
                    .findByStudentIdOrderByAssignedDateDesc(
                            studentId);

        } else {

            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // STUDENT - SUBMIT SINGLE PROOF
    // ============================================================

    @Transactional
    public TaskDto submitTaskProof(
            Long taskId,
            String proofType,
            String proofUrl,
            String studentEmail) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + taskId));

        validateStudentTask(task, studentEmail);

        validateTaskCanReceiveSubmission(task);

        validateProof(proofType, proofUrl);

        TaskSubmission submission = createSubmission(task);

        TaskProof taskProof = new TaskProof();

        // IMPORTANT:
        // Keep both old task_id relation and new submission_id relation.
        taskProof.setTask(task);
        taskProof.setSubmission(submission);

        taskProof.setProofType(proofType.toUpperCase());
        taskProof.setProofUrl(proofUrl);
        taskProof.setUploadedAt(LocalDateTime.now());

        taskProofRepository.save(taskProof);

        updateTaskAfterProofSubmission(task);

        return toDto(task);
    }

    // ============================================================
    // STUDENT - SUBMIT MULTIPLE PROOFS
    // ============================================================

    @Transactional
    public TaskDto submitTaskProofs(
            Long taskId,
            List<String> proofTypes,
            List<String> proofUrls,
            String studentEmail) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + taskId));

        validateStudentTask(task, studentEmail);

        if (proofTypes == null || proofTypes.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one proof is required");
        }

        if (proofUrls == null || proofUrls.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one proof file is required");
        }

        if (proofTypes.size() != proofUrls.size()) {
            throw new IllegalArgumentException(
                    "Proof type and proof URL count must match");
        }

        validateTaskCanReceiveSubmission(task);

        /*
         * IMPORTANT:
         *
         * We DO NOT delete old submissions/proofs.
         *
         * Every new submission gets its own TaskSubmission row.
         * This preserves complete submission history.
         */
        TaskSubmission submission = createSubmission(task);

        for (int i = 0; i < proofTypes.size(); i++) {

            String proofType = proofTypes.get(i);
            String proofUrl = proofUrls.get(i);

            validateProof(proofType, proofUrl);

            TaskProof taskProof = new TaskProof();

            // IMPORTANT:
            // Keep both old task_id relation and new submission_id relation.
            taskProof.setTask(task);
            taskProof.setSubmission(submission);

            taskProof.setProofType(proofType.toUpperCase());
            taskProof.setProofUrl(proofUrl);
            taskProof.setUploadedAt(LocalDateTime.now());

            taskProofRepository.save(taskProof);
        }

        updateTaskAfterProofSubmission(task);

        return toDto(task);
    }

    // ============================================================
    // CREATE SUBMISSION
    // ============================================================

    private TaskSubmission createSubmission(Task task) {

        TaskSubmission submission = new TaskSubmission();

        submission.setTask(task);
        submission.setStatus(TaskStatus.PENDING_VERIFICATION);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setVerifiedAt(null);
        submission.setAdminMessage(null);

        return taskSubmissionRepository.save(submission);
    }

    // ============================================================
    // VALIDATE STUDENT TASK
    // ============================================================

    private void validateStudentTask(
            Task task,
            String studentEmail) {

        if (studentEmail != null &&
                !task.getStudent()
                        .getUser()
                        .getEmail()
                        .equals(studentEmail)) {

            throw new UnauthorizedException(
                    "You do not have permission to submit proof for this task");
        }
    }

    // ============================================================
    // VALIDATE TASK SUBMISSION STATUS
    // ============================================================

    private void validateTaskCanReceiveSubmission(Task task) {

        if (task.getStatus() == TaskStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException(
                    "Task is already waiting for admin verification");
        }

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed task cannot be submitted again");
        }
    }

    // ============================================================
    // VALIDATE PROOF
    // ============================================================

    private void validateProof(
            String proofType,
            String proofUrl) {

        if (proofType == null || proofType.isBlank()) {
            throw new IllegalArgumentException(
                    "Proof type is required");
        }

        if (proofUrl == null || proofUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "Image or video proof is required");
        }

        if (!proofType.equalsIgnoreCase("IMAGE") &&
                !proofType.equalsIgnoreCase("VIDEO")) {

            throw new IllegalArgumentException(
                    "Proof must be an image or video");
        }
    }

    // ============================================================
    // UPDATE TASK AFTER PROOF SUBMISSION
    // ============================================================

    private void updateTaskAfterProofSubmission(Task task) {

        task.setSubmittedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.PENDING_VERIFICATION);
        task.setCompletedAt(null);
        task.setVerifiedAt(null);
        task.setAdminMessage(null);

        taskRepository.save(task);
    }
// ============================================================
// ADMIN - DELETE TASK
// ============================================================

    @Transactional
    public void deleteTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + taskId));

        /*
         * First delete all proofs.
         *
         * task_proofs has:
         * 1. task_id -> tasks.id
         * 2. submission_id -> task_submissions.id
         *
         * So proofs must be deleted first.
         */
        List<TaskSubmission> submissions =
                taskSubmissionRepository
                        .findByTaskOrderBySubmittedAtDesc(task);

        for (TaskSubmission submission : submissions) {

            List<TaskProof> proofs =
                    taskProofRepository
                            .findBySubmissionOrderByUploadedAtAsc(
                                    submission);

            if (!proofs.isEmpty()) {
                taskProofRepository.deleteAll(proofs);
            }
        }

        /*
         * Delete any old proofs which may not have
         * a submission relation.
         */
        List<TaskProof> taskProofs =
                taskProofRepository.findAll()
                        .stream()
                        .filter(proof ->
                                proof.getTask() != null &&
                                        proof.getTask().getId()
                                                .equals(taskId))
                        .collect(Collectors.toList());

        if (!taskProofs.isEmpty()) {
            taskProofRepository.deleteAll(taskProofs);
        }

        /*
         * Now delete submission history.
         */
        if (!submissions.isEmpty()) {
            taskSubmissionRepository.deleteAll(submissions);
        }

        /*
         * Finally delete the task.
         */
        taskRepository.delete(task);
    }


    // ============================================================
    // ADMIN - GET PENDING VERIFICATION TASKS
    // ============================================================

    @Transactional(readOnly = true)
    public List<TaskDto> getPendingVerificationTasks() {

        return taskRepository
                .findByStatus(TaskStatus.PENDING_VERIFICATION)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET LATEST SUBMISSION
    // ============================================================

    private TaskSubmission getLatestSubmission(Task task) {

        return taskSubmissionRepository
                .findByTaskOrderBySubmittedAtDesc(task)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No submission found for this task"));
    }

    // ============================================================
    // ADMIN - APPROVE TASK
    // ============================================================

    @Transactional
    public TaskDto approveTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + taskId));

        if (task.getStatus() != TaskStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException(
                    "Only pending verification tasks can be approved");
        }

        TaskSubmission submission = getLatestSubmission(task);

        submission.setStatus(TaskStatus.COMPLETED);
        submission.setVerifiedAt(LocalDateTime.now());
        submission.setAdminMessage("Task approved by admin");

        taskSubmissionRepository.save(submission);

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        task.setVerifiedAt(LocalDateTime.now());
        task.setAdminMessage("Task approved by admin");

        task = taskRepository.save(task);

        // Update student streak
        studentService.recordActivityAndCalculateStreak(
                task.getStudent(),
                0,
                LocalDate.now());

        // Update roadmap progress
        updateRoadmapProgress(
                task.getStudent(),
                task.getRoadmap());

        return toDto(task);
    }

    // ============================================================
    // ADMIN - REJECT TASK
    // ============================================================

    @Transactional
    public TaskDto rejectTask(
            Long taskId,
            String adminMessage) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + taskId));

        if (task.getStatus() != TaskStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException(
                    "Only pending verification tasks can be rejected");
        }

        if (adminMessage == null || adminMessage.isBlank()) {
            throw new IllegalArgumentException(
                    "Admin message is required when rejecting a task");
        }

        TaskSubmission submission = getLatestSubmission(task);

        submission.setStatus(TaskStatus.REJECTED);
        submission.setVerifiedAt(LocalDateTime.now());
        submission.setAdminMessage(adminMessage);

        taskSubmissionRepository.save(submission);

        task.setStatus(TaskStatus.REJECTED);
        task.setVerifiedAt(LocalDateTime.now());
        task.setAdminMessage(adminMessage);

        task = taskRepository.save(task);

        return toDto(task);
    }

    // ============================================================
    // ROADMAP PROGRESS
    // ============================================================

    private void updateRoadmapProgress(
            Student student,
            Roadmap roadmap) {

        if (roadmap == null) {
            return;
        }

        studentRoadmapRepository
                .findByStudentIdAndRoadmapId(
                        student.getId(),
                        roadmap.getId())
                .ifPresent(sr -> {

                    long totalTasks =
                            taskRepository
                                    .findByStudentIdAndRoadmapId(
                                            student.getId(),
                                            roadmap.getId())
                                    .size();

                    long completedTasks =
                            taskRepository
                                    .findByStudentIdAndRoadmapId(
                                            student.getId(),
                                            roadmap.getId())
                                    .stream()
                                    .filter(t ->
                                            t.getStatus()
                                                    == TaskStatus.COMPLETED)
                                    .count();

                    double pct =
                            totalTasks == 0
                                    ? 0.0
                                    : Math.round(
                                    ((double) completedTasks
                                            / totalTasks)
                                            * 100.0
                                            * 10.0)
                                    / 10.0;

                    sr.setCompletionPercentage(pct);

                    if (pct >= 100.0) {
                        sr.setStatus("COMPLETED");
                    }

                    studentRoadmapRepository.save(sr);
                });
    }

    // ============================================================
    // DTO CONVERSION
    // ============================================================

    @Transactional(readOnly = true)
    public TaskDto toDto(Task t) {

        // --------------------------------------------------------
        // ALL SUBMISSION HISTORY
        // --------------------------------------------------------

        List<TaskSubmissionDto> submissionHistory =
                taskSubmissionRepository
                        .findByTaskOrderBySubmittedAtDesc(t)
                        .stream()
                        .map(this::toSubmissionDto)
                        .collect(Collectors.toList());

        // --------------------------------------------------------
        // CURRENT/LATEST SUBMISSION PROOFS
        // --------------------------------------------------------

        List<TaskProofDto> proofs = new ArrayList<>();

        if (!submissionHistory.isEmpty()) {

            TaskSubmission latestSubmission =
                    taskSubmissionRepository
                            .findByTaskOrderBySubmittedAtDesc(t)
                            .stream()
                            .findFirst()
                            .orElse(null);

            if (latestSubmission != null) {

                proofs = taskProofRepository
                        .findBySubmissionOrderByUploadedAtAsc(
                                latestSubmission)
                        .stream()
                        .map(this::toProofDto)
                        .collect(Collectors.toList());
            }
        }

        /*
         * OLD COMPLETED TASK FALLBACK
         *
         * Old tasks ki submission history undakapoyina,
         * Task table lo old proofType/proofUrl untayi.
         * Vatini student-side final proof ga expose chestunnam.
         */
        if (proofs.isEmpty()
                && t.getStatus() == TaskStatus.COMPLETED
                && t.getProofUrl() != null
                && !t.getProofUrl().isBlank()) {

            TaskProofDto oldProof = new TaskProofDto();

            oldProof.setId(null);
            oldProof.setProofType(t.getProofType());
            oldProof.setProofUrl(t.getProofUrl());
            oldProof.setUploadedAt(t.getSubmittedAt());

            proofs.add(oldProof);
        }

        return TaskDto.builder()
                .id(t.getId())

                .studentId(t.getStudent().getId())

                .studentName(
                        t.getStudent()
                                .getUser()
                                .getFullName())

                .roadmapId(
                        t.getRoadmap() != null
                                ? t.getRoadmap().getId()
                                : null)

                .roadmapTitle(
                        t.getRoadmap() != null
                                ? t.getRoadmap().getTitle()
                                : null)

                .subjectId(
                        t.getSubject() != null
                                ? t.getSubject().getId()
                                : null)

                .subjectTitle(
                        t.getSubject() != null
                                ? t.getSubject().getTitle()
                                : null)

                .moduleId(
                        t.getModule() != null
                                ? t.getModule().getId()
                                : null)

                .moduleTitle(
                        t.getModule() != null
                                ? t.getModule().getTitle()
                                : null)

                .topicId(
                        t.getTopic() != null
                                ? t.getTopic().getId()
                                : null)

                .topicTitle(
                        t.getTopic() != null
                                ? t.getTopic().getTitle()
                                : null)

                .title(t.getTitle())
                .description(t.getDescription())

                .assignedDate(t.getAssignedDate())
                .dueDate(t.getDueDate())

                .estimatedDurationMinutes(
                        t.getEstimatedDurationMinutes())

                .priority(t.getPriority().name())
                .status(t.getStatus().name())

                .completedAt(t.getCompletedAt())

                .submittedAt(t.getSubmittedAt())
                .verifiedAt(t.getVerifiedAt())
                .adminMessage(t.getAdminMessage())

                .proofs(proofs)

                .submissionHistory(submissionHistory)

                .createdAt(t.getCreatedAt())

                .build();
    }

    // ============================================================
    // SUBMISSION DTO CONVERSION
    // ============================================================

    private TaskSubmissionDto toSubmissionDto(
            TaskSubmission submission) {

        List<TaskProofDto> proofs =
                taskProofRepository
                        .findBySubmissionOrderByUploadedAtAsc(
                                submission)
                        .stream()
                        .map(this::toProofDto)
                        .collect(Collectors.toList());

        TaskSubmissionDto dto = new TaskSubmissionDto();

        dto.setId(submission.getId());

        dto.setStatus(
                submission.getStatus() != null
                        ? submission.getStatus().name()
                        : null);

        dto.setSubmittedAt(
                submission.getSubmittedAt());

        dto.setVerifiedAt(
                submission.getVerifiedAt());

        dto.setAdminMessage(
                submission.getAdminMessage());

        dto.setProofs(proofs);

        return dto;
    }

    // ============================================================
    // PROOF DTO CONVERSION
    // ============================================================

    private TaskProofDto toProofDto(TaskProof proof) {

        TaskProofDto dto = new TaskProofDto();

        dto.setId(proof.getId());
        dto.setProofType(proof.getProofType());
        dto.setProofUrl(proof.getProofUrl());
        dto.setUploadedAt(proof.getUploadedAt());

        return dto;
    }
}