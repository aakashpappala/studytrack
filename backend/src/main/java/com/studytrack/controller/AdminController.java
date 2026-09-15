package com.studytrack.controller;

import com.studytrack.dto.admin.*;
import com.studytrack.dto.common.ApiResponse;
import com.studytrack.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    

    public ResponseEntity<ApiResponse<AdminDashboardDto>> getDashboard() {
        AdminDashboardDto dto = adminService.getDashboard();
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentSummaryDto>>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long roadmapId) {
        List<StudentSummaryDto> list = adminService.getAllStudents(search, status, roadmapId);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDetailDto>> getStudentById(@PathVariable Long id) {
        StudentDetailDto dto = adminService.getStudentDetail(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PostMapping("/students")
    public ResponseEntity<ApiResponse<StudentSummaryDto>> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentSummaryDto dto = adminService.createStudent(request);
        return ResponseEntity.ok(ApiResponse.ok("Student created successfully", dto));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentSummaryDto>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        StudentSummaryDto dto = adminService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Student updated successfully", dto));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok("Student deleted successfully", null));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<AdminAnalyticsDto>> getAnalytics(@RequestParam(required = false) Long studentId) {
        AdminAnalyticsDto dto = adminService.getAnalytics(studentId);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }
}
