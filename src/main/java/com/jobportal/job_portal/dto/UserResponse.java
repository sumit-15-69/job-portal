package com.jobportal.job_portal.dto;

import com.jobportal.job_portal.entity.Role;

public record UserResponse(Long id, String name, String email, Role role) {
}