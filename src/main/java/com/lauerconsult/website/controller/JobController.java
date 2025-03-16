package com.lauerconsult.website.controller;

import com.lauerconsult.website.DTO.JobPostingDTO;
import com.lauerconsult.website.repository.JobPostingRepository;
import com.lauerconsult.website.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {
    private final JobService jobService;
    private final JobPostingRepository jobPostingRepository;

    public JobController(JobService jobService, JobPostingRepository jobPostingRepository) {
        this.jobService = jobService;
        this.jobPostingRepository = jobPostingRepository;
    }

    @GetMapping
    public List<JobPostingDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    // ✅ Add this missing POST mapping
    @PostMapping
    public ResponseEntity<JobPostingDTO> createJob(@RequestBody JobPostingDTO jobPostingDTO) {
        JobPostingDTO savedJob = jobService.createJob(jobPostingDTO);
        return ResponseEntity.status(201).body(savedJob);
    }
    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId) {
        if (!jobPostingRepository.existsById(jobId)) {
            return ResponseEntity.status(404).body("Job not found.");
        }

        jobPostingRepository.deleteById(jobId);
        return ResponseEntity.ok("Job posting deleted successfully.");
    }

}