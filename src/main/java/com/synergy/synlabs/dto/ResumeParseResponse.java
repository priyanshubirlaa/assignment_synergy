package com.synergy.synlabs.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResumeParseResponse {
    private List<Education> education;
    private String email;
    private List<Experience> experience;
    private String name;
    private String phone;
    private List<String> skills;
    
    @Data
    public static class Education {
        private String name;
        private String url;
    }
    
    @Data
    public static class Experience {
        private List<String> dates;
        private String name;
        private String url;
    }
}
