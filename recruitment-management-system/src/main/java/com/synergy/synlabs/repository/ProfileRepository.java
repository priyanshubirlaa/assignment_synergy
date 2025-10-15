package com.synergy.synlabs.repository;

import com.synergy.synlabs.model.Profile;
import com.synergy.synlabs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    Optional<Profile> findByApplicant(User applicant);
    
    Optional<Profile> findByApplicantId(Long applicantId);
}
