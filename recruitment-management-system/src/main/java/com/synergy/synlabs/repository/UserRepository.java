package com.synergy.synlabs.repository;

import com.synergy.synlabs.model.User;
import com.synergy.synlabs.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByUserType(UserType userType);
    
    boolean existsByEmail(String email);
}
