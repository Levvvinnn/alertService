package com.alertservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class alertRequest {
    private UUID id;

    @NotBlank(message = "Message Required")
    private String message;

    private Instant time;
    private int severity;
    private List<String> channels;
    private List<String> recipients;
    private Map<String,String> metadata;

}
