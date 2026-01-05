package com.alertservice.model;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class alertResponse {
    private UUID id;
    private String message;
    private String status;
    private List<channelResult> channelResults;

    @Data
    public static class channelResult{
        private String channel;
        private boolean success;
        private String message;
        private Double cost;
    }
}
