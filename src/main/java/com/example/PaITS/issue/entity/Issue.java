package com.example.PaITS.issue.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID projectId;

    @Column(nullable = false, unique = true, length = 20)
    private String issueKey;

    private String title;

    public String description;
    public String status = "OPEN";
    public String priority = "MEDIUM";
    public String issueType = "TASK";

    private UUID reporterId;
    private UUID assigneeId;
    private UUID parentIssueId;

    private LocalDate dueDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
