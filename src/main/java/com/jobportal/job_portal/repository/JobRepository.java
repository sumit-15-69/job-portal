package com.jobportal.job_portal.repository;

import com.jobportal.job_portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}