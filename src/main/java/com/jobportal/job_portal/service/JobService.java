package com.jobportal.job_portal.service;

import com.jobportal.job_portal.dto.JobRequest;
import com.jobportal.job_portal.dto.JobResponse;
import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.Role;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.ApplicationRepository;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobService(JobRepository jobRepository,
                      UserRepository userRepository,
                      ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public JobResponse createJob(JobRequest request, String email) {
        User recruiter = findUser(email);

        Job job = new Job();
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setCompany(request.company());
        job.setLocation(request.location());
        job.setSalary(request.salary());
        job.setPostedBy(recruiter);

        return toResponse(jobRepository.save(job));
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream().map(this::toResponse).toList();
    }

    public JobResponse getJobById(Long id) {
        return toResponse(findJob(id));
    }

    @Transactional
    public void deleteJob(Long id, String email) {
        Job job = findJob(id);
        User user = findUser(email);

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = job.getPostedBy() != null
                && job.getPostedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can delete only your own jobs");
        }

        applicationRepository.deleteByJob(job);
        jobRepository.delete(job);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Job findJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Job not found with id " + id));
    }

    private JobResponse toResponse(Job job) {
        User poster = job.getPostedBy();
        return new JobResponse(
                job.getId(), job.getTitle(), job.getDescription(), job.getCompany(),
                job.getLocation(), job.getSalary(), job.getCreatedAt(),
                poster != null ? poster.getId() : null,
                poster != null ? poster.getName() : null);
    }
}