package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.task.CreateTaskRequest;
import com.studytrack.dto.task.TaskDto;
import com.studytrack.service.FileStorageService;
import com.studytrack.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final FileStorageService fileStorageService;

    public TaskController(
            TaskService taskService,
            FileStorageService fileStorageService) {

        this.taskService = taskService;
        this.fileStorageService = fileStorageService;
    }

    // =========================================================
    // STUDENT ENDPOINTS
    // =========================================================

    @GetMapping("/api/student/tasks")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getMyTasks(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TaskDto> tasks =
                taskService.getTasksForStudent(
                        userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.ok(tasks)
        );
    }

    @GetMapping("/api/student/tasks/today")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getMyTodayTasks(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TaskDto> tasks =
                taskService.getTodayTasksForStudent(
                        userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.ok(tasks)
        );
    }

    // =========================================================
    // STUDENT - SUBMIT MULTIPLE IMAGE / VIDEO PROOFS
    // =========================================================

    @PostMapping("/api/student/tasks/{id}/submit-proof")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<TaskDto>> submitTaskProof(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException(
                    "At least one image or video file is required"
            );
        }

        List<String> proofTypes = new ArrayList<>();
        List<String> proofUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(
                        "One of the selected files is empty"
                );
            }

            String contentType = file.getContentType();

            if (contentType == null) {
                throw new IllegalArgumentException(
                        "File type could not be detected"
                );
            }

            String proofType;

            if (contentType.startsWith("image/")) {
                proofType = "IMAGE";

            } else if (contentType.startsWith("video/")) {
                proofType = "VIDEO";

            } else {
                throw new IllegalArgumentException(
                        "Only image or video files are allowed"
                );
            }

            // Upload file to Cloudinary
            String proofUrl =
                    fileStorageService.storeFile(file);

            proofTypes.add(proofType);
            proofUrls.add(proofUrl);
        }

        // Save all proofs for this task
        TaskDto updated =
                taskService.submitTaskProofs(
                        id,
                        proofTypes,
                        proofUrls,
                        userDetails.getUsername()
                );

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Proof files uploaded and submitted for admin verification",
                        updated
                )
        );
    }

    // =========================================================
    // ADMIN ENDPOINTS
    // =========================================================

    @GetMapping("/api/admin/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getAllTasksForAdmin(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<TaskDto> tasks =
                taskService.getAllTasksForAdmin(
                        studentId,
                        date);

        return ResponseEntity.ok(
                ApiResponse.ok(tasks)
        );
    }

    @GetMapping("/api/admin/tasks/pending-verification")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<TaskDto>>>
    getPendingVerificationTasks() {

        List<TaskDto> tasks =
                taskService.getPendingVerificationTasks();

        return ResponseEntity.ok(
                ApiResponse.ok(tasks)
        );
    }

    @PutMapping("/api/admin/tasks/{id}/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<TaskDto>> approveTask(
            @PathVariable Long id) {

        TaskDto updated =
                taskService.approveTask(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Task approved successfully",
                        updated
                )
        );
    }

    @PutMapping("/api/admin/tasks/{id}/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<TaskDto>> rejectTask(
            @PathVariable Long id,
            @RequestParam String adminMessage) {

        TaskDto updated =
                taskService.rejectTask(
                        id,
                        adminMessage);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Task rejected",
                        updated
                )
        );
    }

    @DeleteMapping("/api/admin/tasks/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(
                ApiResponse.ok("Task deleted successfully", null)
        );
    }

    @PostMapping("/api/admin/tasks")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<TaskDto>> createTaskForStudent(
            @Valid @RequestBody CreateTaskRequest request) {

        TaskDto created =
                taskService.createTask(request);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Task created successfully",
                        created
                )
        );
    }
}