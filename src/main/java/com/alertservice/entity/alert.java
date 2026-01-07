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
    private String severity;

    @Column(name="ChannelsSent")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
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

    public void setChannelsSent(String join) {
        this.channels=join;
    }
}
