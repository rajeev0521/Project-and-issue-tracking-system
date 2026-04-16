package com.example.PaITS.common.exception;

import com.example.PaITS.issue.entity.IssueStatus;
import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidStatusTransitionException extends RuntimeException {
    private final IssueStatus fromStatus;
    private final IssueStatus toStatus;
    private final Set<IssueStatus> availableTransitions;

    public InvalidStatusTransitionException(IssueStatus fromStatus, IssueStatus toStatus, Set<IssueStatus> availableTransitions) {
        super(String.format("Invalid status transition from %s to %s. Allowed transitions: %s", fromStatus, toStatus, availableTransitions));
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.availableTransitions = availableTransitions;
    }
}
