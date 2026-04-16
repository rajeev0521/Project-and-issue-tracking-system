package com.example.PaITS.issue.service;

import com.example.PaITS.issue.dto.IssueRequestDTO;
import com.example.PaITS.issue.dto.IssueResponseDTO;
import com.example.PaITS.issue.dto.IssueStatusUpdateDTO;
import com.example.PaITS.issue.entity.Issue;
import com.example.PaITS.issue.entity.IssueStatus;
import com.example.PaITS.common.exception.ResourceNotFoundException;
import com.example.PaITS.issue.repository.IssueRepository;
import com.example.PaITS.project.entity.Project;
import com.example.PaITS.project.repository.ProjectRepository;
import com.example.PaITS.user.entity.User;
import com.example.PaITS.user.repository.UserRepository;
import com.example.PaITS.workflow.WorkflowEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final WorkflowEngine workflowEngine;

    @Override
    @Transactional
    public IssueResponseDTO createIssue(UUID projectId, UUID reporterId, IssueRequestDTO request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", reporterId));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
        }

        // Increment sequence and generate key
        int nextSequence = project.getIssueSequence() + 1;
        project.setIssueSequence(nextSequence);
        projectRepository.save(project);

        String issueKey = project.getProjectKey() + "-" + nextSequence;

        Issue issue = Issue.builder()
                .project(project)
                .issueKey(issueKey)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(IssueStatus.OPEN)
                .priority(request.getPriority())
                .issueType(request.getIssueType())
                .reporter(reporter)
                .assignee(assignee)
                .build();

        return mapToResponseDTO(issueRepository.save(issue));
    }

    @Override
    public List<IssueResponseDTO> getIssuesByProject(UUID projectId) {
        return issueRepository.findByProjectId(projectId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueResponseDTO> getIssuesByProjectAndStatus(UUID projectId, IssueStatus status) {
        return issueRepository.findByProjectIdAndStatus(projectId, status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueResponseDTO> getIssuesByProjectAndAssignee(UUID projectId, UUID assigneeId) {
        return issueRepository.findByProjectIdAndAssigneeId(projectId, assigneeId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IssueResponseDTO getIssueById(UUID id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));
        return mapToResponseDTO(issue);
    }

    @Override
    @Transactional
    public IssueResponseDTO updateIssue(UUID id, IssueRequestDTO request) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setPriority(request.getPriority());
        issue.setIssueType(request.getIssueType());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
            issue.setAssignee(assignee);
        } else {
            issue.setAssignee(null);
        }

        return mapToResponseDTO(issueRepository.save(issue));
    }

    @Override
    @Transactional
    public void deleteIssue(UUID id) {
        if (!issueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Issue", "id", id);
        }
        issueRepository.deleteById(id);
    }

    @Override
    @Transactional
    public IssueResponseDTO updateStatus(UUID id, IssueStatusUpdateDTO statusUpdate) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        workflowEngine.validateTransition(issue.getStatus(), statusUpdate.getStatus());
        issue.setStatus(statusUpdate.getStatus());

        return mapToResponseDTO(issueRepository.save(issue));
    }

    @Override
    @Transactional
    public IssueResponseDTO assignIssue(UUID id, UUID assigneeId) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", assigneeId));
        
        issue.setAssignee(assignee);

        return mapToResponseDTO(issueRepository.save(issue));
    }

    private IssueResponseDTO mapToResponseDTO(Issue issue) {
        return IssueResponseDTO.builder()
                .id(issue.getId())
                .issueKey(issue.getIssueKey())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .priority(issue.getPriority())
                .issueType(issue.getIssueType())
                .availableTransitions(workflowEngine.getAvailableTransitions(issue.getStatus()))
                .projectId(issue.getProject().getId())
                .reporterId(issue.getReporter().getId())
                .assigneeId(issue.getAssignee() != null ? issue.getAssignee().getId() : null)
                .createdAt(issue.getCreatedAt())
                .updatedAt(issue.getUpdatedAt())
                .build();
    }
}
