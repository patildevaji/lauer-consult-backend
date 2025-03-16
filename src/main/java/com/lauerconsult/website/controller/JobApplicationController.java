package com.lauerconsult.website.controller;

import com.lauerconsult.website.DTO.JobApplicationDTO;
import com.lauerconsult.website.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<String> applyForJob(
            @RequestParam Long jobId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String currentLocation,
            @RequestParam MultipartFile resume) {

        try {
            JobApplicationDTO applicationDTO = new JobApplicationDTO(
                    firstName, lastName, email, phone, currentLocation, jobId);

            jobApplicationService.applyForJob(applicationDTO, resume);

            return ResponseEntity.ok("Application submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error submitting application.");
        }
    }
}