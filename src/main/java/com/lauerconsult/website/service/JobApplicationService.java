package com.lauerconsult.website.service;

import com.lauerconsult.website.DTO.JobApplicationDTO;
import com.lauerconsult.website.entity.JobApplication;
import com.lauerconsult.website.entity.JobPosting;
import com.lauerconsult.website.repository.JobApplicationRepository;
import com.lauerconsult.website.repository.JobPostingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JavaMailSender mailSender;

    // Load recruiter emails from `application.yml`
    @Value("${app.recruiter-emails}")
    private String recruiterEmails;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, JobPostingRepository jobPostingRepository, JavaMailSender mailSender) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.mailSender = mailSender;
    }

    public void applyForJob(JobApplicationDTO applicationDTO, MultipartFile resume) throws IOException {
        JobPosting jobPosting = jobPostingRepository.findById(applicationDTO.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found: " + applicationDTO.getJobId()));
        if (resume == null || resume.isEmpty()) {
            throw new RuntimeException("Resume file is required.");
        }
        JobApplication application = new JobApplication();
        application.setFirstName(applicationDTO.getFirstName());
        application.setLastName(applicationDTO.getLastName());
        application.setEmail(applicationDTO.getEmail());
        application.setPhone(applicationDTO.getPhone());
        application.setCurrentLocation(applicationDTO.getCurrentLocation());
        application.setResume(resume.getBytes());


        jobApplicationRepository.save(application);

        // Send email notification to recruiters
        sendEmailNotification(application, resume);
    }

    private void sendEmailNotification(JobApplication application, MultipartFile resume) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Convert comma-separated emails into a list
            List<String> recipients = Arrays.asList(recruiterEmails.split(","));
            helper.setTo(recipients.toArray(new String[0]));

            helper.setSubject("New Job Application: " + application.getFirstName() + " " + application.getLastName());

            String emailContent = "<p><strong>New Job Application Received</strong></p>" +
                    "<p><strong>Position:</strong> " + application.getJobPosting().getPositionName() + "</p>" +
                    "<p><strong>Candidate Name:</strong> " + application.getFirstName() + " " + application.getLastName() + "</p>" +
                    "<p><strong>Email:</strong> " + application.getEmail() + "</p>" +
                    "<p><strong>Phone:</strong> " + application.getPhone() + "</p>" +
                    "<p><strong>Current Location:</strong> " + application.getCurrentLocation() + "</p>" +
                    "<p>Attached is the candidate's resume.</p>";

            helper.setText(emailContent, true);

            // Attach Resume
            helper.addAttachment("Resume_" + application.getFirstName() + ".pdf", resume);

            // Send email
            mailSender.send(message);
            log.info("Job application email sent successfully to recruiters.");

        } catch (MessagingException e) {
            log.error("Error sending job application email", e);
        }
    }
}