package com.synergy.synlabs.controller;

import com.synergy.synlabs.dto.JobRequest;
import com.synergy.synlabs.model.Job;
import com.synergy.synlabs.model.JobApplication;
import com.synergy.synlabs.model.Profile;
import com.synergy.synlabs.model.User;
import com.synergy.synlabs.service.JobService;
import com.synergy.synlabs.service.ResumeService;
import com.synergy.synlabs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {
    
    private final JobService jobService;
    private final UserService userService;
    private final ResumeService resumeService;
    
    @PostMapping("/job")
    public ResponseEntity<Map<String, Object>> createJob(@RequestBody JobRequest request,
                                                        Authentication authentication) {
        try {
            User admin = userService.getUserByEmail(authentication.getName());
            Job job = jobService.createJob(request, admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Job created successfully");
            response.put("job", job);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/job/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobWithApplicants(@PathVariable Long jobId,
                                                                   Authentication authentication) {
        try {
            User admin = userService.getUserByEmail(authentication.getName());
            Optional<Job> jobOpt = jobService.getJobById(jobId);
            
            if (jobOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Job not found");
                return ResponseEntity.notFound().build();
            }
            
            Job job = jobOpt.get();
            List<JobApplication> applications = jobService.getApplicationsForJob(jobId, admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("job", job);
            response.put("applications", applications);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/applicants")
    public ResponseEntity<Map<String, Object>> getAllApplicants(Authentication authentication) {
        try {
            // Verify admin authentication
            userService.getUserByEmail(authentication.getName());
            List<User> applicants = userService.getUsersByType(com.synergy.synlabs.model.UserType.APPLICANT);
            
            Map<String, Object> response = new HashMap<>();
            response.put("applicants", applicants);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<Map<String, Object>> getApplicantProfile(@PathVariable Long applicantId,
                                                                  Authentication authentication) {
        try {
            // Verify admin authentication
            userService.getUserByEmail(authentication.getName());
            Profile profile = resumeService.getProfileByUserId(applicantId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("profile", profile);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
