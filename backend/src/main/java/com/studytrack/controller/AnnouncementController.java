package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.notification.AnnouncementDto;
import com.studytrack.dto.notification.CreateAnnouncementRequest;
import com.studytrack.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    

    public ResponseEntity<ApiResponse<List<AnnouncementDto>>> getAllAnnouncements() {
        List<AnnouncementDto> list = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<AnnouncementDto>> createAnnouncement(
            @Valid @RequestBody CreateAnnouncementRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        AnnouncementDto created = announcementService.createAnnouncement(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Announcement created and broadcasted", created));
    }
}
