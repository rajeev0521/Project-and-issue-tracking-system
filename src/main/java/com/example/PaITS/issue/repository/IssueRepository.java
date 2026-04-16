package com.example.PaITS.issue.repository;

import com.example.PaITS.issue.entity.Issue;
import com.example.PaITS.issue.entity.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID> {
    List<Issue> findByProjectId(UUID projectId);
    List<Issue> findByReporterId(UUID reporterId);
    List<Issue> findByAssigneeId(UUID assigneeId);
    List<Issue> findByProjectIdAndStatus(UUID projectId, IssueStatus status);
    List<Issue> findByProjectIdAndAssigneeId(UUID projectId, UUID assigneeId);
    void deleteByProjectId(UUID projectId);
}
