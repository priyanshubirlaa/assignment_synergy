package com.synergy.synlabs.controller;

import com.synergy.synlabs.model.Profile;
import com.synergy.synlabs.model.User;
import com.synergy.synlabs.service.ResumeService;
import com.synergy.synlabs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResumeController {
    
    private final ResumeService resumeService;
    private final UserService userService;
    
    @PostMapping("/uploadResume")
    public ResponseEntity<Map<String, Object>> uploadResume(@RequestParam("file") MultipartFile file,
                                                           Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Profile profile = resumeService.uploadAndParseResume(file, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Resume uploaded and parsed successfully");
            response.put("profile", profile);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "File upload failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
