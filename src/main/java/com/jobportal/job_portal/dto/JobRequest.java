package com.jobportal.job_portal.dto;

public record JobRequest(String title, String description, String company,
                         String location, Double salary) {
}