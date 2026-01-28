package com.alertservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-argument constructor
@Builder // Adds a builder pattern for object creation
public class alertRequest {

    @NotBlank(message = "Message Required")
    private String message;

    private String severity;
    private List<String> channels;
    private List<String> recipients;
    private Map<String, String> metadata;
    private String event;
}