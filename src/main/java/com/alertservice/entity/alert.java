package com.alertservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Data
public class alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 1000)
    private String message;

    private String event;

    @Column(nullable = false)
    private Integer severity;

    @Column(name="Channels_Sent")
    private String channels;

    @Column(name="Status")
    private String status;

    @Column(name="Cost")
    private Double cost;

    @Column(name="Created_At")
    private Instant createdAt;

    @Column(name="metadata",length=2000)
    private String metadata;

    @PrePersist
    protected void onCreate(){
        createdAt=Instant.now();
    }
}
