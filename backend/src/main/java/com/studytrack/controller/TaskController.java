package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.task.CreateTaskRequest;
import com.studytrack.dto.task.TaskDto;
import com.studytrack.dto.task.UpdateTaskStatusRequest;
import com.studytrack.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Student endpoints
    @GetMapping("/api/student/tasks")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getMyTasks(@AuthenticationPrincipal UserDetails userDetails) {
        List<TaskDto> tasks = taskService.getTasksForStudent(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(tasks));
    }

    @GetMapping("/api/student/tasks/today")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getMyTodayTasks(@AuthenticationPrincipal UserDetails userDetails) {
        List<TaskDto> tasks = taskService.getTodayTasksForStudent(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(tasks));
    }

    @PutMapping("/api/student/tasks/{id}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<TaskDto>> updateMyTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskDto updated = taskService.updateTaskStatus(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Task status updated", updated));
    }

    @PutMapping("/api/student/tasks/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<TaskDto>> completeMyTask(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskDto updated = taskService.completeTask(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Task marked as completed", updated));
    }

    // Admin endpoints
    @GetMapping("/api/admin/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getAllTasksForAdmin(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TaskDto> tasks = taskService.getAllTasksForAdmin(studentId, date);
        return ResponseEntity.ok(ApiResponse.ok(tasks));
    }

    @PostMapping("/api/admin/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<TaskDto>> createTaskForStudent(@Valid @RequestBody CreateTaskRequest request) {
        TaskDto created = taskService.createTask(request);
        return ResponseEntity.ok(ApiResponse.ok("Task created successfully", created));
    }
}
