package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.studylog.CreateStudyLogRequest;
import com.studytrack.dto.studylog.StudyLogDto;
import com.studytrack.service.StudyLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    @PostMapping("/api/student/study-logs")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<StudyLogDto>> createStudyLog(
            @Valid @RequestBody CreateStudyLogRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudyLogDto log = studyLogService.createStudyLog(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Study session recorded successfully", log));
    }

    @GetMapping("/api/student/study-logs")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<List<StudyLogDto>>> getMyStudyLogs(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<StudyLogDto> logs = studyLogService.getStudyLogs(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }

    @DeleteMapping("/api/student/study-logs/{id}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<Void>> deleteStudyLog(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        studyLogService.deleteStudyLog(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Study log deleted successfully", null));
    }

    @GetMapping("/api/admin/students/{id}/study-hours")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<StudyLogDto>>> getStudentStudyLogsForAdmin(@PathVariable Long id) {
        List<StudyLogDto> logs = studyLogService.getStudyLogsByStudentId(id);
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }
}
