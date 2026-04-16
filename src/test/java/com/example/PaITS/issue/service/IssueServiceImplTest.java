package com.example.PaITS.issue.service;

import com.example.PaITS.common.exception.InvalidStatusTransitionException;
import com.example.PaITS.common.exception.ResourceNotFoundException;
import com.example.PaITS.issue.dto.IssueResponseDTO;
import com.example.PaITS.issue.dto.IssueStatusUpdateDTO;
import com.example.PaITS.issue.entity.Issue;
import com.example.PaITS.issue.entity.IssueStatus;
import com.example.PaITS.issue.repository.IssueRepository;
import com.example.PaITS.project.entity.Project;
import com.example.PaITS.project.repository.ProjectRepository;
import com.example.PaITS.user.entity.User;
import com.example.PaITS.user.repository.UserRepository;
import com.example.PaITS.workflow.WorkflowEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceImplTest {

    @Mock
    private IssueRepository issueRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkflowEngine workflowEngine;

    @InjectMocks
    private IssueServiceImpl issueService;

    private UUID issueId;
    private Issue issue;

    @BeforeEach
    void setUp() {
        issueId = UUID.randomUUID();
        
        Project mockProject = new Project();
        mockProject.setId(UUID.randomUUID());
        
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());

        issue = new Issue();
        issue.setId(issueId);
        issue.setStatus(IssueStatus.OPEN);
        issue.setProject(mockProject);
        issue.setReporter(mockUser);
    }

    @Test
    void testUpdateStatus_ValidTransition() {
        IssueStatusUpdateDTO statusUpdateDto = new IssueStatusUpdateDTO(IssueStatus.IN_PROGRESS);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        doNothing().when(workflowEngine).validateTransition(IssueStatus.OPEN, IssueStatus.IN_PROGRESS);
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        IssueResponseDTO result = issueService.updateStatus(issueId, statusUpdateDto);

        assertNotNull(result);
        assertEquals(IssueStatus.IN_PROGRESS, issue.getStatus());
        verify(workflowEngine).validateTransition(IssueStatus.OPEN, IssueStatus.IN_PROGRESS);
        verify(issueRepository).save(issue);
    }

    @Test
    void testUpdateStatus_InvalidTransition() {
        IssueStatusUpdateDTO statusUpdateDto = new IssueStatusUpdateDTO(IssueStatus.DONE);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        doThrow(new InvalidStatusTransitionException(IssueStatus.OPEN, IssueStatus.DONE, null))
            .when(workflowEngine).validateTransition(IssueStatus.OPEN, IssueStatus.DONE);

        assertThrows(InvalidStatusTransitionException.class, () -> issueService.updateStatus(issueId, statusUpdateDto));

        verify(workflowEngine).validateTransition(IssueStatus.OPEN, IssueStatus.DONE);
        verify(issueRepository, never()).save(any(Issue.class));
    }

    @Test
    void testUpdateStatus_IssueNotFound() {
        IssueStatusUpdateDTO statusUpdateDto = new IssueStatusUpdateDTO(IssueStatus.IN_PROGRESS);
        
        when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> issueService.updateStatus(issueId, statusUpdateDto));
        
        verify(workflowEngine, never()).validateTransition(any(), any());
        verify(issueRepository, never()).save(any(Issue.class));
    }
}
