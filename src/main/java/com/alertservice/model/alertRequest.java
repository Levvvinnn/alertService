package com.alertservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;
import java.util.Map;

public class alertRequest {
    @NotBlank(message = "Message Required")
    private String message;
    private String severity;
    private List<String> channels;
    private List<String> recipients;
    private Map<String,String> metadata;
    private String event;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
