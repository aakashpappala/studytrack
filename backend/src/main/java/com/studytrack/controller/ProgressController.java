package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.progress.DailyProgressDto;
import com.studytrack.dto.progress.MonthlyProgressDto;
import com.studytrack.dto.progress.StudentProgressSummaryDto;
import com.studytrack.dto.progress.WeeklyProgressDto;
import com.studytrack.entity.Student;
import com.studytrack.service.ProgressService;
import com.studytrack.service.StudentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/student/progress")
@PreAuthorize("hasAuthority('ROLE_STUDENT')")
public class ProgressController {

    private final ProgressService progressService;
    private final StudentService studentService;

    public ProgressController(ProgressService progressService, StudentService studentService) {
        this.progressService = progressService;
        this.studentService = studentService;
    }

    @GetMapping
    

    public ResponseEntity<ApiResponse<StudentProgressSummaryDto>> getMyProgressSummary(
            @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentService.getStudentByEmail(userDetails.getUsername());
        StudentProgressSummaryDto summary = progressService.getStudentProgressSummary(student);
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<DailyProgressDto>> getMyDailyProgress(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentService.getStudentByEmail(userDetails.getUsername());
        DailyProgressDto daily = progressService.getDailyProgress(student, date);
        return ResponseEntity.ok(ApiResponse.ok(daily));
    }

    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<List<WeeklyProgressDto>>> getMyWeeklyProgress(
            @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentService.getStudentByEmail(userDetails.getUsername());
        List<WeeklyProgressDto> weekly = progressService.getWeeklyProgress(student);
        return ResponseEntity.ok(ApiResponse.ok(weekly));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<List<MonthlyProgressDto>>> getMyMonthlyProgress(
            @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentService.getStudentByEmail(userDetails.getUsername());
        List<MonthlyProgressDto> monthly = progressService.getMonthlyProgress(student);
        return ResponseEntity.ok(ApiResponse.ok(monthly));
    }
}
