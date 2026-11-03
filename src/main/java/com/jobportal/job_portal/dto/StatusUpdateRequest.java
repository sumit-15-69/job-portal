package com.jobportal.job_portal.dto;

import com.jobportal.job_portal.entity.ApplicationStatus;

public record StatusUpdateRequest(ApplicationStatus status) {
}