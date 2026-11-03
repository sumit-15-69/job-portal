package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.dto.ApplicationResponse;
import com.jobportal.job_portal.dto.StatusUpdateRequest;
import com.jobportal.job_portal.service.ApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply/{jobId}")
    public ApplicationResponse apply(@PathVariable Long jobId,
                                     Authentication authentication) {
        return applicationService.apply(jobId, authentication.getName());
    }

    @GetMapping("/my")
    public List<ApplicationResponse> myApplications(Authentication authentication) {
        return applicationService.getMyApplications(authentication.getName());
    }

    @GetMapping("/job/{jobId}")
    public List<ApplicationResponse> applicants(@PathVariable Long jobId,
                                                Authentication authentication) {
        return applicationService.getApplicantsForJob(jobId, authentication.getName());
    }

    @PutMapping("/{id}/status")
    public ApplicationResponse updateStatus(@PathVariable Long id,
                                            @RequestBody StatusUpdateRequest request,
                                            Authentication authentication) {
        return applicationService.updateStatus(id, request.status(), authentication.getName());
    }
}