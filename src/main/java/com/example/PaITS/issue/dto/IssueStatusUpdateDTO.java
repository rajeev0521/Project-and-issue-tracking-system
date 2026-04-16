package com.example.PaITS.issue.dto;

import com.example.PaITS.issue.entity.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueStatusUpdateDTO {
    @NotNull(message = "Status cannot be null")
    private IssueStatus status;
}
