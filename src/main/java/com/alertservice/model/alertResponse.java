package com.alertservice.model;

import lombok.Data;

import java.util.ArrayList;
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

        public String getChannel(){
            return channel;
        }
        public void setChannel(String channel){
            this.channel=channel;
        }
        public void setCost(Double cost){
            this.cost=cost;
        }
        public void setMessage(String message){
            this.message=message;
        }
        public void setSuccess(boolean success){
            this.success=success;
        }

        public Double getCost() {
            return cost;
        }
    }

    public void setId(UUID id){
        this.id=id;
    }
    public void setChannelResults(List<channelResult> results){
        this.channelResults=results;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public List<String> getChannels(){
        List<String> channels=new ArrayList<>();
        for(channelResult channel:channelResults){
            channels.add(channel.getChannel());
        }
        return channels;
    }

}
