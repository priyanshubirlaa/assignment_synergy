package com.synergy.synlabs.service;

import com.synergy.synlabs.dto.JobRequest;
import com.synergy.synlabs.model.Job;
import com.synergy.synlabs.model.JobApplication;
import com.synergy.synlabs.model.User;
import com.synergy.synlabs.model.UserType;
import com.synergy.synlabs.repository.JobApplicationRepository;
import com.synergy.synlabs.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {
    
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final UserService userService;
    
    public Job createJob(JobRequest request, User admin) {
        if (!admin.getUserType().equals(UserType.ADMIN)) {
            throw new RuntimeException("Only admin users can create jobs");
        }
        
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setPostedBy(admin);
        job.setTotalApplications(0);
        
        return jobRepository.save(job);
    }
    
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    public Optional<Job> getJobById(Long jobId) {
        return jobRepository.findByIdWithApplications(jobId);
    }
    
    public JobApplication applyForJob(Long jobId, User applicant) {
        if (!applicant.getUserType().equals(UserType.APPLICANT)) {
            throw new RuntimeException("Only applicant users can apply for jobs");
        }
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        if (jobApplicationRepository.existsByJobAndApplicant(job, applicant)) {
            throw new RuntimeException("You have already applied for this job");
        }
        
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setStatus("APPLIED");
        
        JobApplication savedApplication = jobApplicationRepository.save(application);
        
        // Update total applications count
        job.setTotalApplications(job.getTotalApplications() + 1);
        jobRepository.save(job);
        
        return savedApplication;
    }
    
    public List<JobApplication> getApplicationsForJob(Long jobId, User admin) {
        if (!admin.getUserType().equals(UserType.ADMIN)) {
            throw new RuntimeException("Only admin users can view job applications");
        }
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        return jobApplicationRepository.findByJob(job);
    }
    
    public List<JobApplication> getUserApplications(User applicant) {
        if (!applicant.getUserType().equals(UserType.APPLICANT)) {
            throw new RuntimeException("Only applicant users can view their applications");
        }
        
        return jobApplicationRepository.findByApplicant(applicant);
    }
}
