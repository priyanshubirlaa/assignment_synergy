package com.synergy.synlabs.repository;

import com.synergy.synlabs.model.Job;
import com.synergy.synlabs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByPostedBy(User postedBy);
    
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.applications WHERE j.id = :jobId")
    Optional<Job> findByIdWithApplications(@Param("jobId") Long jobId);
}
