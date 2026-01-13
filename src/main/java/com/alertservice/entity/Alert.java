package com.alertservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import java.util.Set;
import java.util.HashSet;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Data
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String message;

    private String event;

    @Column(nullable = false)
    private String severity;

    // map to channels_sent column in DB
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "alert_channels_sent")
    @Column(name = "channel")
    private Set<String> channelsSent = new HashSet<>();

    public Set<String> getChannelsSent() {
        return channelsSent;
    }

    public void setChannelsSent(Set<String> channelsSent) {
        this.channelsSent = channelsSent;
    }

    @Column(name = "status")
    private String status;

    @Column(name = "cost")
    private Double cost;

    // map to created_at column
    @Column(name = "created_at")
    private Instant createdAt;

    // tell Hibernate the DB column type is jsonb
    @Column(name = "metadata", columnDefinition = "jsonb", length = 2000)
    private String metadata;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }


}
