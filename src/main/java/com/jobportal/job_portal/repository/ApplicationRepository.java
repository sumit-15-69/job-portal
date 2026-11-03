package com.jobportal.job_portal.repository;

import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByStudentAndJob(User student, Job job);

    List<JobApplication> findByStudentOrderByAppliedAtDesc(User student);

    List<JobApplication> findByJobOrderByAppliedAtDesc(Job job);
    void deleteByJob(Job job);
}