package com.lauerconsult.website.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_postings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "position_name", nullable = false)
    private String positionName;
    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
}
