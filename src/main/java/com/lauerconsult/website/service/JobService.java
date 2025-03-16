package com.lauerconsult.website.service;

import com.lauerconsult.website.DTO.JobPostingDTO;
import com.lauerconsult.website.entity.JobPosting;
import com.lauerconsult.website.entity.JobType;
import com.lauerconsult.website.repository.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobPostingRepository jobPostingRepository;

    public JobService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<JobPostingDTO> getAllJobs() {
        return jobPostingRepository.findAll().stream()
                .map(job -> new JobPostingDTO(
                        job.getId(), job.getPositionName(), job.getLocation(),
                        job.getJobType().toString(), job.getDescription()))
                .collect(Collectors.toList());
    }
    public JobPostingDTO createJob(JobPostingDTO jobPostingDTO) {
        if (jobPostingDTO.getPositionName() == null || jobPostingDTO.getPositionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Position name cannot be null or empty");
        }
        JobPosting job = JobPosting.builder()
                .positionName(jobPostingDTO.getPositionName())
                .location(jobPostingDTO.getLocation())
                .jobType(JobType.valueOf(jobPostingDTO.getJobType().toUpperCase()))
                .description(jobPostingDTO.getDescription())
                .build();

        job = jobPostingRepository.save(job);
        return new JobPostingDTO(job.getId(), job.getPositionName(), job.getLocation(), job.getJobType().toString(), job.getDescription());
    }
}