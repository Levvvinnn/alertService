package com.alertservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Set;
import java.util.HashSet;
import java.time.Instant;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String message;

    private String event;

    @Column(nullable = false)
    private String severity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "alert_channels_sent")
    @Column(name = "channel")
    @Builder.Default
    private Set<String> channelsSent = new HashSet<>();

    @Column(name = "status")
    private String status;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "metadata", columnDefinition = "jsonb", length = 2000)
    private String metadata;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
