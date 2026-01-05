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
    private int severity;
    private List<String> channels;
    private List<String> recipients;
    private Map<String,String> metadata;

}
