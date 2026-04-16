package com.example.PaITS.issue.dto;

import com.example.PaITS.issue.entity.IssuePriority;
import com.example.PaITS.issue.entity.IssueStatus;
import com.example.PaITS.issue.entity.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponseDTO {
    private UUID id;
    private String issueKey;
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private IssueType issueType;
    private Set<IssueStatus> availableTransitions;
    private UUID projectId;
    private UUID reporterId;
    private UUID assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
