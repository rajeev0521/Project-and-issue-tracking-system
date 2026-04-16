package com.example.PaITS.workflow;

import com.example.PaITS.common.exception.InvalidStatusTransitionException;
import com.example.PaITS.issue.entity.IssueStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowEngineTest {

    private WorkflowEngine workflowEngine;

    @BeforeEach
    void setUp() {
        workflowEngine = new WorkflowEngine();
        workflowEngine.initTransitions();
    }

    @Test
    void openToInProgress_succeeds() {
        assertDoesNotThrow(() -> workflowEngine.validateTransition(IssueStatus.OPEN, IssueStatus.IN_PROGRESS));
    }

    @Test
    void inProgressToDone_succeeds() {
        assertDoesNotThrow(() -> workflowEngine.validateTransition(IssueStatus.IN_PROGRESS, IssueStatus.DONE));
    }

    @Test
    void inProgressToOpen_succeeds() {
        assertDoesNotThrow(() -> workflowEngine.validateTransition(IssueStatus.IN_PROGRESS, IssueStatus.OPEN));
    }

    @Test
    void doneToInProgress_succeeds() {
        assertDoesNotThrow(() -> workflowEngine.validateTransition(IssueStatus.DONE, IssueStatus.IN_PROGRESS));
    }

    @Test
    void openToDone_rejected() {
        assertThrows(InvalidStatusTransitionException.class, 
                     () -> workflowEngine.validateTransition(IssueStatus.OPEN, IssueStatus.DONE));
    }

    @Test
    void doneToOpen_rejected() {
        assertThrows(InvalidStatusTransitionException.class, 
                     () -> workflowEngine.validateTransition(IssueStatus.DONE, IssueStatus.OPEN));
    }

    @Test
    void sameStatus_noOp() {
        assertDoesNotThrow(() -> workflowEngine.validateTransition(IssueStatus.OPEN, IssueStatus.OPEN));
        assertDoesNotThrow(() -> workflowEngine.validateTransition(IssueStatus.DONE, IssueStatus.DONE));
    }

    @Test
    void getAvailableTransitions_open() {
        Set<IssueStatus> transitions = workflowEngine.getAvailableTransitions(IssueStatus.OPEN);
        assertEquals(Set.of(IssueStatus.IN_PROGRESS), transitions);
    }

    @Test
    void getAvailableTransitions_inProgress() {
        Set<IssueStatus> transitions = workflowEngine.getAvailableTransitions(IssueStatus.IN_PROGRESS);
        assertEquals(Set.of(IssueStatus.DONE, IssueStatus.OPEN), transitions);
    }

    @Test
    void getAvailableTransitions_done() {
        Set<IssueStatus> transitions = workflowEngine.getAvailableTransitions(IssueStatus.DONE);
        assertEquals(Set.of(IssueStatus.IN_PROGRESS), transitions);
    }
}
