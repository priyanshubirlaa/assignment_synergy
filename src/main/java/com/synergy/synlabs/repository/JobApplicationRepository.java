package com.synergy.synlabs.repository;

import com.synergy.synlabs.model.Job;
import com.synergy.synlabs.model.JobApplication;
import com.synergy.synlabs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    List<JobApplication> findByJob(Job job);
    
    List<JobApplication> findByApplicant(User applicant);
    
    Optional<JobApplication> findByJobAndApplicant(Job job, User applicant);
    
    boolean existsByJobAndApplicant(Job job, User applicant);
}
