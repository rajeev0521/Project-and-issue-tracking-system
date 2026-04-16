package com.example.PaITS.project.service;

import com.example.PaITS.project.dto.ProjectRequestDTO;
import com.example.PaITS.project.dto.ProjectResponseDTO;
import com.example.PaITS.project.entity.Project;
import com.example.PaITS.project.repository.ProjectRepository;
import com.example.PaITS.user.entity.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private com.example.PaITS.issue.repository.IssueRepository issueRepository;


    // ---- Helper: check if user is Admin ----
    private boolean isAdmin(User user) {
        return "ADMIN".equals(user.getRole());
    }

    // ---- Helper: check if user is the project Leader (creator) ----
    private boolean isLeader(Project project, User user) {
        return project.getCreatedBy().equals(user.getId());
    }

    // // ---- Helper: check if user is an assigned member ----
    // private boolean isMember(Project project, User user) {
    //     return project.getMembers().stream()
    //             .anyMatch(m -> m.getId().equals(user.getId()));
    // }

    // ---- Helper: check if user has ANY access to this project ----
    private boolean hasAccess(Project project, User user) {
        return isAdmin(user) || isLeader(project, user) /*isMember(project, user)*/;
    }

    // ===================== CREATE =====================

    @Override
    @Transactional
    public ProjectResponseDTO saveProject(ProjectRequestDTO request, UUID creatorId) {
        Project project = new Project();
        project.setProjectKey(request.getProjectKey());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(creatorId);
        project.setActive(request.isActive());

        Project saved = projectRepository.save(project);
        return mapToDTO(saved);
    }

    // ===================== READ ALL =====================

    @Override
    public List<ProjectResponseDTO> findAll() {
        return projectRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponseDTO> findAssignedProjects(UUID userId) {
        // Combine projects where user is Leader OR an assigned Member
        Set<Project> projects = new LinkedHashSet<>();
        projects.addAll(projectRepository.findByCreatedBy(userId));
        projects.addAll(projectRepository.findByMemberId(userId));

        return projects.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===================== READ BY ID =====================

    @Override
    public ProjectResponseDTO findById(UUID id, User currentUser) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // SECURITY: Only Admin, Leader, or assigned Members can view this project
        if (!hasAccess(project, currentUser)) {
            throw new RuntimeException("Unauthorized: You do not have access to this project.");
        }

        return mapToDTO(project);
    }

    // ===================== UPDATE =====================

    @Override
    @Transactional
    public ProjectResponseDTO updateProject(UUID id, ProjectRequestDTO details, User currentUser) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // SECURITY: Only Admin or the Project Leader can update
        if (!isAdmin(currentUser) && !isLeader(existingProject, currentUser)) {
            throw new RuntimeException("Unauthorized: Only Admins and the Project Leader can modify this project.");
        }

        existingProject.setName(details.getName());
        existingProject.setDescription(details.getDescription());
        existingProject.setProjectKey(details.getProjectKey());
        existingProject.setActive(details.isActive());

        Project updated = projectRepository.save(existingProject);
        return mapToDTO(updated);
    }

    // ===================== DELETE =====================

    @Override
    @Transactional
    public void deleteProject(UUID id, User currentUser) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot delete: Project not found"));

        // SECURITY: Only Admin or the Project Leader can delete
        if (!isAdmin(currentUser) && !isLeader(project, currentUser)) {
            throw new RuntimeException("Unauthorized: Only Admins and the Project Leader can delete this project.");
        }

        issueRepository.deleteByProjectId(id);
        projectRepository.deleteById(id);
    }

    // ===================== MEMBER MANAGEMENT =====================

    @Override
    @Transactional
    public ProjectResponseDTO addMember(UUID projectId, UUID userId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // SECURITY: Only Admin or Leader can add members
        if (!isAdmin(currentUser) && !isLeader(project, currentUser)) {
            throw new RuntimeException("Unauthorized: Only Admins and the Project Leader can add members.");
        }

        // User newMember = userRepository.findById(userId)
                // .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // project.getMembers().add(newMember);
        Project saved = projectRepository.save(project);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ProjectResponseDTO removeMember(UUID projectId, UUID userId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // SECURITY: Only Admin or Leader can remove members
        if (!isAdmin(currentUser) && !isLeader(project, currentUser)) {
            throw new RuntimeException("Unauthorized: Only Admins and the Project Leader can remove members.");
        }

        // project.getMembers().removeIf(m -> m.getId().equals(userId));
        Project saved = projectRepository.save(project);
        return mapToDTO(saved);
    }

    // ===================== MAPPER =====================

    private ProjectResponseDTO mapToDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setProjectKey(project.getProjectKey());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setActive(project.isActive());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }

}

    