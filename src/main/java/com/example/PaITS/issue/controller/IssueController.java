package com.example.PaITS.issue.controller;

import com.example.PaITS.issue.dto.IssueRequestDTO;
import com.example.PaITS.issue.dto.IssueResponseDTO;
import com.example.PaITS.issue.dto.IssueStatusUpdateDTO;
import com.example.PaITS.issue.entity.IssueStatus;
import com.example.PaITS.issue.service.IssueService;
import com.example.PaITS.user.entity.User;
import com.example.PaITS.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/issues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IssueController {

    private final IssueService issueService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<IssueResponseDTO> createIssue(
            @PathVariable UUID projectId,
           @valid @RequestBody IssueRequestDTO request,
            Authentication authentication) {
        
        UUID reporterId = getCurrentUserId(authentication);
        IssueResponseDTO created = issueService.createIssue(projectId, reporterId, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getIssuesByProject(
            @PathVariable UUID projectId,
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) UUID assigneeId) {
        
        if (status != null) {
            return ResponseEntity.ok(issueService.getIssuesByProjectAndStatus(projectId, status));
        } else if (assigneeId != null) {
            return ResponseEntity.ok(issueService.getIssuesByProjectAndAssignee(projectId, assigneeId));
        }
        
        return ResponseEntity.ok(issueService.getIssuesByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getIssueById(@PathVariable UUID id) {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> updateIssue(
            @PathVariable UUID id,
            @valid @RequestBody IssueRequestDTO request) {
        return ResponseEntity.ok(issueService.updateIssue(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteIssue(@PathVariable UUID id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IssueResponseDTO> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody IssueStatusUpdateDTO statusUpdate) {
        return ResponseEntity.ok(issueService.updateStatus(id, statusUpdate));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IssueResponseDTO> assignIssue(
            @PathVariable UUID id,
            @RequestParam UUID assigneeId) {
        return ResponseEntity.ok(issueService.assignIssue(id, assigneeId));
    }

    private UUID getCurrentUserId(Authentication authentication) {
        String email = authentication.getName(); // JwtFilter sets email as principal
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
        return user.getId();
    }
}
