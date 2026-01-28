package com.alertservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class alertRequest {

    @NotBlank(message = "Message Required")
    private String message;

    private String severity;
    private List<String> channels;
    private List<String> recipients;
    private Map<String, String> metadata;
    private String event;
}