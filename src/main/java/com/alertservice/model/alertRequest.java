package com.alertservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
public class alertRequest {
    @NotBlank(message = "Message Required")
    private String message;
    private String severity;
    private List<String> channels;
    private List<String> recipients;
    private Map<String,String> metadata;
    private String event;

    public void setMessage(String message) {
        this.message=message;
    }

    public void setSeverity(String severity){
        this.severity=severity;
    }
    
    public void setEvent(String event){
        this.event=event;
    }

    public String getSeverity() {
        return severity;
    }

    public List<String> getChannels() {
        return channels;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getRecipients() {
        return recipients;
    }
}
