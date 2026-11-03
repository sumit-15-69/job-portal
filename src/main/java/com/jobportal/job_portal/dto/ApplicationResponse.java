package com.jobportal.job_portal.dto;

import com.jobportal.job_portal.entity.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponse(Long id, Long jobId, String jobTitle, String company,
                                  Long studentId, String studentName, String studentEmail,
                                  ApplicationStatus status, LocalDateTime appliedAt) {
}