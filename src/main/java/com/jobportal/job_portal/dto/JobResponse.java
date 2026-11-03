package com.jobportal.job_portal.dto;

import java.time.LocalDateTime;

public record JobResponse(Long id, String title, String description, String company,
                          String location, Double salary, LocalDateTime createdAt,
                          Long postedById, String postedByName) {
}