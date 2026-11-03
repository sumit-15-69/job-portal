package com.jobportal.job_portal.service;

import com.jobportal.job_portal.dto.ApplicationResponse;
import com.jobportal.job_portal.entity.ApplicationStatus;
import com.jobportal.job_portal.entity.Job;
import com.jobportal.job_portal.entity.JobApplication;
import com.jobportal.job_portal.entity.Role;
import com.jobportal.job_portal.entity.User;
import com.jobportal.job_portal.repository.ApplicationRepository;
import com.jobportal.job_portal.repository.JobRepository;
import com.jobportal.job_portal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ApplicationResponse apply(Long jobId, String email) {
        User student = findUser(email);
        Job job = findJob(jobId);

        if (applicationRepository.existsByStudentAndJob(student, job)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You have already applied to this job");
        }

        JobApplication application = new JobApplication();
        application.setStudent(student);
        application.setJob(job);

        return toResponse(applicationRepository.save(application));
    }

    public List<ApplicationResponse> getMyApplications(String email) {
        User student = findUser(email);
        return applicationRepository.findByStudentOrderByAppliedAtDesc(student)
                .stream().map(this::toResponse).toList();
    }

    public List<ApplicationResponse> getApplicantsForJob(Long jobId, String email) {
        User user = findUser(email);
        Job job = findJob(jobId);
        checkCanManage(job, user);

        return applicationRepository.findByJobOrderByAppliedAtDesc(job)
                .stream().map(this::toResponse).toList();
    }

    public ApplicationResponse updateStatus(Long applicationId,
                                            ApplicationStatus status,
                                            String email) {
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
        }

        User user = findUser(email);
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Application not found"));

        checkCanManage(application.getJob(), user);

        application.setStatus(status);
        return toResponse(applicationRepository.save(application));
    }

    private void checkCanManage(Job job, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = job.getPostedBy() != null
                && job.getPostedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can manage only your own jobs");
        }
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

    private ApplicationResponse toResponse(JobApplication a) {
        return new ApplicationResponse(
                a.getId(),
                a.getJob().getId(), a.getJob().getTitle(), a.getJob().getCompany(),
                a.getStudent().getId(), a.getStudent().getName(), a.getStudent().getEmail(),
                a.getStatus(), a.getAppliedAt());
    }
}