package com.alertservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class alertResponse {
    private UUID id;
    private String message;
    private String status;
    private List<channelResult> channelResults;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class channelResult {
        private String channel;
        private boolean success;
        private String message;
        private Double cost;
    }
}
