package com.jobportal.job_portal.dto;

import com.jobportal.job_portal.entity.Role;

public record RegisterRequest(String name, String email, String password, Role role) {
}