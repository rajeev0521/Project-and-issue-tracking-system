package com.example.PaITS.workflow;

import com.example.PaITS.common.exception.InvalidStatusTransitionException;
import com.example.PaITS.issue.entity.IssueStatus;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

@Component
public class WorkflowEngine {

    private final Map<IssueStatus, Set<IssueStatus>> transitionMap = new EnumMap<>(IssueStatus.class);

    @PostConstruct
    public void initTransitions() {
        // OPEN -> IN_PROGRESS
        transitionMap.put(IssueStatus.OPEN, Set.of(IssueStatus.IN_PROGRESS));
        
        // IN_PROGRESS -> DONE, OPEN
        transitionMap.put(IssueStatus.IN_PROGRESS, Set.of(IssueStatus.DONE, IssueStatus.OPEN));
        
        // DONE -> IN_PROGRESS (Reopen)
        transitionMap.put(IssueStatus.DONE, Set.of(IssueStatus.IN_PROGRESS));
    }

    public void validateTransition(IssueStatus from, IssueStatus to) {
        if (from == to) {
            return; // Idempotent same-status transitions are allowed
        }
        
        if (!isValidTransition(from, to)) {
            throw new InvalidStatusTransitionException(from, to, getAvailableTransitions(from));
        }
    }

    public Set<IssueStatus> getAvailableTransitions(IssueStatus current) {
        return transitionMap.getOrDefault(current, Collections.emptySet());
    }

    public boolean isValidTransition(IssueStatus from, IssueStatus to) {
        if (from == to) {
            return true;
        }
        Set<IssueStatus> allowed = getAvailableTransitions(from);
        return allowed.contains(to);
    }
}
