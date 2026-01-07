package com.alertservice.model;

import lombok.Data;
import java.util.List;
import java.util.UUID;

public class alertResponse {
    private UUID id;
    private String message;
    private String status;
    private List<channelResult> channelResults;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<channelResult> getChannelResults() {
        return channelResults;
    }

    public void setChannelResults(List<channelResult> channelResults) {
        this.channelResults = channelResults;
    }

    public static class channelResult{
        private String channel;
        private boolean success;
        private String message;
        private Double cost;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }

}
