package com.studytrack.service;

import com.studytrack.dto.task.CreateTaskRequest;
import com.studytrack.dto.task.TaskDto;
import com.studytrack.dto.task.UpdateTaskStatusRequest;
import com.studytrack.entity.*;
import com.studytrack.exception.ResourceNotFoundException;
import com.studytrack.exception.UnauthorizedException;
import com.studytrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final StudentRepository studentRepository;
    private final RoadmapRepository roadmapRepository;
    private final SubjectRepository subjectRepository;
    private final ModuleRepository moduleRepository;
    private final TopicRepository topicRepository;
    private final StudentRoadmapRepository studentRoadmapRepository;
    private final NotificationRepository notificationRepository;
    private final StudentService studentService;

    public TaskService(TaskRepository taskRepository, StudentRepository studentRepository, RoadmapRepository roadmapRepository, SubjectRepository subjectRepository, ModuleRepository moduleRepository, TopicRepository topicRepository, StudentRoadmapRepository studentRoadmapRepository, NotificationRepository notificationRepository, StudentService studentService) {
        this.taskRepository = taskRepository;
        this.studentRepository = studentRepository;
        this.roadmapRepository = roadmapRepository;
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
        this.topicRepository = topicRepository;
        this.studentRoadmapRepository = studentRoadmapRepository;
        this.notificationRepository = notificationRepository;
        this.studentService = studentService;
    }

    @Transactional
    

    public TaskDto createTask(CreateTaskRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

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

        LocalDate assigned = request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now();

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
                .estimatedDurationMinutes(request.getEstimatedDurationMinutes() != null ? request.getEstimatedDurationMinutes() : 60)
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.NOT_STARTED)
                .build();

        task = taskRepository.save(task);

        // Notify student of new task
        Notification notification = Notification.builder()
                .student(student)
                .title("New Task Assigned")
                .message("A new task has been assigned: " + task.getTitle())
                .type(NotificationType.TASK_ASSIGNED)
                .build();
        notificationRepository.save(notification);

        return toDto(task);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getTasksForStudent(String studentEmail) {
        return taskRepository.findByStudentEmail(studentEmail).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getTodayTasksForStudent(String studentEmail) {
        LocalDate today = LocalDate.now();
        return taskRepository.findByStudentEmailAndAssignedDate(studentEmail, today).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasksForAdmin(Long studentId, LocalDate date) {
        List<Task> tasks;
        if (studentId != null && date != null) {
            tasks = taskRepository.findByStudentIdAndAssignedDate(studentId, date);
        } else if (studentId != null) {
            tasks = taskRepository.findByStudentIdOrderByAssignedDateDesc(studentId);
        } else {
            tasks = taskRepository.findAll();
        }
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public TaskDto updateTaskStatus(Long taskId, UpdateTaskStatusRequest request, String studentEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Enforce student ownership
        if (studentEmail != null && !task.getStudent().getUser().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException("You do not have permission to modify this task");
        }

        task.setStatus(request.getStatus());
        if (request.getStatus() == TaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now());
            // Update student streak
            studentService.recordActivityAndCalculateStreak(task.getStudent(), 0, LocalDate.now());
            updateRoadmapProgress(task.getStudent(), task.getRoadmap());
        } else {
            task.setCompletedAt(null);
        }

        task = taskRepository.save(task);
        return toDto(task);
    }

    @Transactional
    public TaskDto completeTask(Long taskId, String studentEmail) {
        UpdateTaskStatusRequest req = new UpdateTaskStatusRequest();
        req.setStatus(TaskStatus.COMPLETED);
        return updateTaskStatus(taskId, req, studentEmail);
    }

    private void updateRoadmapProgress(Student student, Roadmap roadmap) {
        if (roadmap == null) return;

        studentRoadmapRepository.findByStudentIdAndRoadmapId(student.getId(), roadmap.getId())
                .ifPresent(sr -> {
                    long totalTasks = taskRepository.findByStudentIdAndRoadmapId(student.getId(), roadmap.getId()).size();
                    long completedTasks = taskRepository.findByStudentIdAndRoadmapId(student.getId(), roadmap.getId())
                            .stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();

                    double pct = totalTasks == 0 ? 0.0 : Math.round(((double) completedTasks / totalTasks) * 100.0 * 10.0) / 10.0;
                    sr.setCompletionPercentage(pct);
                    if (pct >= 100.0) {
                        sr.setStatus("COMPLETED");
                    }
                    studentRoadmapRepository.save(sr);
                });
    }

    public TaskDto toDto(Task t) {
        return TaskDto.builder()
                .id(t.getId())
                .studentId(t.getStudent().getId())
                .studentName(t.getStudent().getUser().getFullName())
                .roadmapId(t.getRoadmap() != null ? t.getRoadmap().getId() : null)
                .roadmapTitle(t.getRoadmap() != null ? t.getRoadmap().getTitle() : null)
                .subjectId(t.getSubject() != null ? t.getSubject().getId() : null)
                .subjectTitle(t.getSubject() != null ? t.getSubject().getTitle() : null)
                .moduleId(t.getModule() != null ? t.getModule().getId() : null)
                .moduleTitle(t.getModule() != null ? t.getModule().getTitle() : null)
                .topicId(t.getTopic() != null ? t.getTopic().getId() : null)
                .topicTitle(t.getTopic() != null ? t.getTopic().getTitle() : null)
                .title(t.getTitle())
                .description(t.getDescription())
                .assignedDate(t.getAssignedDate())
                .dueDate(t.getDueDate())
                .estimatedDurationMinutes(t.getEstimatedDurationMinutes())
                .priority(t.getPriority().name())
                .status(t.getStatus().name())
                .completedAt(t.getCompletedAt())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
