package com.synergy.synlabs.service;

import com.synergy.synlabs.dto.ResumeParseResponse;
import com.synergy.synlabs.model.Profile;
import com.synergy.synlabs.model.User;
import com.synergy.synlabs.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {
    
    private final ProfileRepository profileRepository;
    
    @Value("${resume.parser.api.url}")
    private String apiUrl;
    
    @Value("${resume.parser.api.key}")
    private String apiKey;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public Profile uploadAndParseResume(MultipartFile file, User user) throws IOException {
        // Validate file type
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".pdf") && 
                                       !originalFilename.toLowerCase().endsWith(".docx"))) {
            throw new IllegalArgumentException("Only PDF and DOCX files are allowed");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Save file
        String fileName = user.getId() + "_" + System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Parse resume using third-party API
        ResumeParseResponse parseResponse = parseResume(file);
        
        // Create or update profile
        Profile profile = profileRepository.findByApplicant(user)
                .orElse(new Profile());
        
        profile.setApplicant(user);
        profile.setResumeFileAddress(filePath.toString());
        profile.setName(parseResponse.getName());
        profile.setEmail(parseResponse.getEmail());
        profile.setPhone(parseResponse.getPhone());
        
        // Convert lists to strings
        if (parseResponse.getSkills() != null) {
            profile.setSkills(String.join(", ", parseResponse.getSkills()));
        }
        
        if (parseResponse.getEducation() != null) {
            profile.setEducation(parseResponse.getEducation().stream()
                    .map(edu -> edu.getName())
                    .collect(Collectors.joining(", ")));
        }
        
        if (parseResponse.getExperience() != null) {
            profile.setExperience(parseResponse.getExperience().stream()
                    .map(exp -> {
                        String name = exp.getName() != null ? exp.getName() : "";
                        java.util.List<String> dates = exp.getDates() != null ? exp.getDates() : java.util.Collections.emptyList();
                        String datesStr = String.join(", ", dates);
                        return datesStr.isEmpty() ? name : (name + " (" + datesStr + ")");
                    })
                    .collect(java.util.stream.Collectors.joining(", ")));
        }
        
        return profileRepository.save(profile);
    }
    
    private ResumeParseResponse parseResume(MultipartFile file) throws IOException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", apiKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            HttpEntity<ByteArrayResource> requestEntity = new HttpEntity<>(resource, headers);

            try {
                ResumeParseResponse response = restTemplate.postForObject(
                        apiUrl,
                        requestEntity,
                        ResumeParseResponse.class
                );

                if (response == null) {
                    throw new RuntimeException("Failed to parse resume");
                }

                return response;
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                String body = e.getResponseBodyAsString();
                log.error("Resume API 4xx: status={} body={}", e.getStatusCode(), body);
                throw new RuntimeException("Failed to parse resume: " + body);
            } catch (org.springframework.web.client.HttpServerErrorException e) {
                String body = e.getResponseBodyAsString();
                log.error("Resume API 5xx: status={} body={}", e.getStatusCode(), body);
                throw new RuntimeException("Resume service unavailable: " + body);
            } catch (Exception e) {
                log.error("Error parsing resume: {}", e.getMessage());
                throw new RuntimeException("Failed to parse resume: " + e.getMessage());
            }
        } finally {
            // Any cleanup code if needed in future
        }
    }
    
    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByApplicantId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
    
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}
