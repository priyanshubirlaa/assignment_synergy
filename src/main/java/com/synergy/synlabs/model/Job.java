package com.synergy.synlabs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "posted_on", nullable = false)
    private LocalDateTime postedOn;
    
    @Column(name = "total_applications")
    private Integer totalApplications = 0;
    
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobApplication> applications;
    
    @PrePersist
    protected void onCreate() {
        postedOn = LocalDateTime.now();
        if (totalApplications == null) {
            totalApplications = 0;
        }
    }
}
