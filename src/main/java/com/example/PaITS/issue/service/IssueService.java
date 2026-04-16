package com.example.PaITS.issue.service;

import com.example.PaITS.issue.dto.IssueRequestDTO;
import com.example.PaITS.issue.dto.IssueResponseDTO;
import com.example.PaITS.issue.dto.IssueStatusUpdateDTO;
import com.example.PaITS.issue.entity.IssueStatus;

import java.util.List;
import java.util.UUID;

public interface IssueService {
    IssueResponseDTO createIssue(UUID projectId, UUID reporterId, IssueRequestDTO request);
    List<IssueResponseDTO> getIssuesByProject(UUID projectId);
    List<IssueResponseDTO> getIssuesByProjectAndStatus(UUID projectId, IssueStatus status);
    List<IssueResponseDTO> getIssuesByProjectAndAssignee(UUID projectId, UUID assigneeId);
    IssueResponseDTO getIssueById(UUID id);
    IssueResponseDTO updateIssue(UUID id, IssueRequestDTO request);
    void deleteIssue(UUID id);
    IssueResponseDTO updateStatus(UUID id, IssueStatusUpdateDTO statusUpdate);
    IssueResponseDTO assignIssue(UUID id, UUID assigneeId);
}
