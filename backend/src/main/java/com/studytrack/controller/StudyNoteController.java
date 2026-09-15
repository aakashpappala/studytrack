package com.studytrack.controller;

import com.studytrack.dto.common.ApiResponse;
import com.studytrack.dto.note.StudyNoteDto;
import com.studytrack.dto.note.StudyNoteRequest;
import com.studytrack.service.StudyNoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/notes")
@PreAuthorize("hasAuthority('ROLE_STUDENT')")
public class StudyNoteController {

    private final StudyNoteService studyNoteService;

    public StudyNoteController(StudyNoteService studyNoteService) {
        this.studyNoteService = studyNoteService;
    }

    @GetMapping
    

    public ResponseEntity<ApiResponse<List<StudyNoteDto>>> getMyNotes(@AuthenticationPrincipal UserDetails userDetails) {
        List<StudyNoteDto> notes = studyNoteService.getNotes(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(notes));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudyNoteDto>> createNote(
            @Valid @RequestBody StudyNoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudyNoteDto note = studyNoteService.createNote(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Note created successfully", note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyNoteDto>> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody StudyNoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudyNoteDto note = studyNoteService.updateNote(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Note updated successfully", note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        studyNoteService.deleteNote(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Note deleted successfully", null));
    }
}
