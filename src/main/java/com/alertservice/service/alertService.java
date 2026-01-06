package com.alertservice.service;

import com.alertservice.model.alertRequest;
import com.alertservice.model.alertResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alertservice.integration.smsService;
import com.alertservice.integration.emailService;
import com.alertservice.integration.slackService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class alertService {
    private smsService smsService;
    private emailService emailService;
    private slackService slackService;
    private ObjectMapper objectMapper;

    public alertResponse processAlert(alertRequest request){
        alertResponse response=new alertResponse();
        response.setId(UUID.randomUUID());
        response.setMessage("Alert processed");

        List<alertResponse.channelResult> results=new ArrayList<>();
        double totalCost=0.0;

        List<String> channels=determineChannels(request);

        for(String channel:channels){
            alertResponse.channelResult result=sendToChannel(
                    channel,
                    request.getMessage(),
                    request.getSeverity(),
                    request.getRecipients()

            );
            results.add(result);
            if(result.getCost()!=null){
                totalCost+=result.getCost();
            }
        }
        response.setChannelResults(results);

    }

    private List<String> determineChannels(alertRequest request){
        if(request.getChannels()!=null||request.getChannels().isEmpty()){
            return request.getChannels();
        }
        return switch(request.getSeverity().toLowerCase()){
            case "critical"-> Arrays.asList("sms,slack,email");
            case "warning" -> Arrays.asList("slack,email");
            default->List.of("slack");
        };
    }

    private alertResponse.channelResult sendToChannel(String channel){

    }
}
