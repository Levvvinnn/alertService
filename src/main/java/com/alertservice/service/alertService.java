package com.alertservice.service;

import com.alertservice.entity.alert;
import com.alertservice.model.alertRequest;
import com.alertservice.model.alertResponse;
import com.alertservice.repository.alertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(alertService.class);
    private  smsService smsService;
    private  emailService emailService;
    private  slackService slackService;
    private  ObjectMapper objectMapper;

    public alertService(
            smsService smsService,
            emailService emailService,
            slackService slackService,
            ObjectMapper objectMapper
    ) {
        this.smsService = smsService;
        this.emailService = emailService;
        this.slackService = slackService;
        this.objectMapper = objectMapper;
    }


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
        return response;
    }

    private List<String> determineChannels(alertRequest request) {
        // defensive null checks: if client provided explicit channels, use them
        if (request != null && request.getChannels() != null && !request.getChannels().isEmpty()) {
            return request.getChannels();
        }

        // fallback based on severity (safely handle null severity)
        String sev = request == null || request.getSeverity() == null
                ? ""
                : request.getSeverity().toLowerCase();

        return switch (sev) {
            case "critical" -> Arrays.asList("sms", "slack", "email");
            case "warning"  -> Arrays.asList("slack", "email");
            default         -> List.of("slack");
        };
    }


    private alertResponse.channelResult sendToChannel(String channel,String message,String severity,List<String> recipients){
        alertResponse.channelResult result=new alertResponse.channelResult();
        result.setChannel(channel);
        try{
            boolean success=switch(channel.toLowerCase()){
                case "sms"->{
                    String phone=getDefaultRecipient(recipients,"sms");
                    boolean sent=smsService.sendSms(phone,message);
                    result.setCost(smsService.cost());
                    yield sent;
                }
                case "slack" -> {
                    String slackChannel = getDefaultRecipient(recipients, "slack");
                    boolean sent = slackService.sendSlackMessage(slackChannel, message, severity);
                    result.setCost(slackService.cost());
                    yield sent;
                }
                case "email" -> {
                    String email = getDefaultRecipient(recipients, "email");
                    String subject = String.format("[%s] Alert Notification", severity.toUpperCase());
                    boolean sent = emailService.sendEmailTo(email, subject, message);
                    result.setCost(emailService.cost());
                    yield sent;
                }
                default -> {

                    log.warn("Unknown channel: {}", channel);
                    yield false;
                }
            };
            result.setSuccess(success);
            result.setMessage(success?"Sent successfully":"Failed to send");
        } catch (Exception e) {

            log.error("Error sending to {}: {}", channel, e.getMessage());
            result.setSuccess(false);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;

    }

    private String getDefaultRecipient(List<String> recipients,String channel){
        if(recipients!=null){
            return recipients.get(0);
        }
        return switch(channel){
            case "sms" ->"+1234567890";
            case "email" -> "admin@example.com";
            case "slack"->"#alerts";
            default->"";
        };

    }
    public List<alert> getRecentAlerts(){
        return alertRepository.findTop10ByOrderByCreatedAtDesc();
    }

}
