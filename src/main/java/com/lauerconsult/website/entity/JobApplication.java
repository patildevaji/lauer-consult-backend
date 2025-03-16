package com.lauerconsult.website.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "job_applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting jobPosting;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String currentLocation;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] resume;

    private Timestamp appliedAt = new Timestamp(System.currentTimeMillis());
}
