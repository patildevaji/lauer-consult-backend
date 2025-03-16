package com.lauerconsult.website.DTO;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPostingDTO {
    private Long id;

    private String positionName;
    private String location;
    private String jobType;
    private String description;
}