package com.synergy.synlabs.controller;

import com.synergy.synlabs.model.Job;
import com.synergy.synlabs.model.JobApplication;
import com.synergy.synlabs.model.User;
import com.synergy.synlabs.service.JobService;
import com.synergy.synlabs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobController {
    
    private final JobService jobService;
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(Authentication authentication) {
        try {
            List<Job> jobs = jobService.getAllJobs();
            
            Map<String, Object> response = new HashMap<>();
            response.put("jobs", jobs);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyForJob(@RequestParam Long jobId,
                                                          Authentication authentication) {
        try {
            User applicant = userService.getUserByEmail(authentication.getName());
            JobApplication application = jobService.applyForJob(jobId, applicant);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Applied for job successfully");
            response.put("application", application);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

