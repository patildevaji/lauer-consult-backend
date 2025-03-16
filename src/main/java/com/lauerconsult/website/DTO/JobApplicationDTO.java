package com.lauerconsult.website.DTO;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobApplicationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String currentLocation;
    private Long jobId;
}