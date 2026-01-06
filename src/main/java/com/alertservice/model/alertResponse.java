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

        private String getChannel(){
            return channel;
        }

    }

    public void setId(UUID id){
        this.id=id;
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
