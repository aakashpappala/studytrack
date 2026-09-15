package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.roadmap.AssignRoadmapRequest;
import com.studytrack.dto.roadmap.RoadmapDto;
import com.studytrack.service.RoadmapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @GetMapping
    

    public ResponseEntity<ApiResponse<List<RoadmapDto>>> getAllRoadmaps() {
        return ResponseEntity.ok(ApiResponse.ok(roadmapService.getAllRoadmaps()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoadmapDto>> getRoadmapById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(roadmapService.getRoadmapById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RoadmapDto>> createRoadmap(@Valid @RequestBody RoadmapDto dto) {
        RoadmapDto created = roadmapService.createRoadmap(dto);
        return ResponseEntity.ok(ApiResponse.ok("Roadmap created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RoadmapDto>> updateRoadmap(
            @PathVariable Long id,
            @Valid @RequestBody RoadmapDto dto) {
        RoadmapDto updated = roadmapService.updateRoadmap(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Roadmap updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRoadmap(@PathVariable Long id) {
        roadmapService.deleteRoadmap(id);
        return ResponseEntity.ok(ApiResponse.ok("Roadmap deleted successfully", null));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignRoadmap(@Valid @RequestBody AssignRoadmapRequest request) {
        roadmapService.assignRoadmap(request.getStudentId(), request.getRoadmapId());
        return ResponseEntity.ok(ApiResponse.ok("Roadmap assigned successfully", null));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<RoadmapDto>> getMyRoadmap(@AuthenticationPrincipal UserDetails userDetails) {
        RoadmapDto roadmap = roadmapService.getStudentRoadmap(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(roadmap));
    }
}
