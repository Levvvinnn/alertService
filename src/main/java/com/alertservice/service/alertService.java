package com.alertservice.service;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.alertservice.entity.Alert;
import com.alertservice.model.alertRequest;
import com.alertservice.model.alertResponse;
import com.alertservice.repository.alertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alertservice.integration.smsService;
import com.alertservice.integration.emailService;
import com.alertservice.integration.slackService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class alertService {
    private final smsService smsService;
    private final emailService emailService;
    private final slackService slackService;
    private final alertRepository alertRepository;
    private final ObjectMapper objectMapper;


    public alertResponse processAlert(alertRequest request){
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
        
        long successCount = results.stream().filter(alertResponse.channelResult::isSuccess).count();
        String status = successCount == results.size() ? "success" : 
                        successCount > 0 ? "partial" : "failed";
        
        alertResponse response = alertResponse.builder()
                .id(UUID.randomUUID())
                .message("Alert processed")
                .status(status)
                .channelResults(results)
                .build();
        
        saveAlert(request, response, totalCost);
        return response;
    }

    private List<String> determineChannels(alertRequest request) {
        if (request != null && request.getChannels() != null && !request.getChannels().isEmpty()) {
            return request.getChannels();
        }
        String severe = request == null || request.getSeverity() == null ? "" : request.getSeverity().toLowerCase();

        return switch (severe) {
            case "critical" -> Arrays.asList("sms", "slack", "email");
            case "warning"  -> Arrays.asList("slack", "email");
            default         -> List.of("slack");
        };
    }


    private alertResponse.channelResult sendToChannel(String channel,String message,String severity,List<String> recipients){
        try{
            Double cost = 0.0;
            boolean success=switch(channel.toLowerCase()){
                case "sms"->{
                    String phone=getDefaultRecipient(recipients,"sms");
                    boolean sent=smsService.sendSms(phone,message);
                    cost = smsService.cost();
                    yield sent;
                }
                case "slack" -> {
                    String slackChannel = getDefaultRecipient(recipients, "slack");
                    boolean sent = slackService.sendSlackMessage(slackChannel, message, severity);
                    cost = slackService.cost();
                    yield sent;
                }
                case "email" -> {
                    String email = getDefaultRecipient(recipients, "email");
                    String subject = String.format("[%s] Alert Notification", severity.toUpperCase());
                    boolean sent = emailService.sendEmailTo(email, subject, message);
                    cost = emailService.cost();
                    yield sent;
                }
                default -> {
                    log.warn("Unknown channel: {}", channel);
                    yield false;
                }
            };
            
            return alertResponse.channelResult.builder()
                    .channel(channel)
                    .success(success)
                    .message(success ? "Sent successfully" : "Failed to send")
                    .cost(cost)
                    .build();
        } catch (Exception e) {
            log.error("Error sending to {}: {}", channel, e.getMessage());
            return alertResponse.channelResult.builder()
                    .channel(channel)
                    .success(false)
                    .message("Error: " + e.getMessage())
                    .build();
        }
    }
    private void saveAlert(alertRequest request,alertResponse response,double cost){
        try{
            Set<String> channelsSet = response.getChannelResults().stream()
                    .map(alertResponse.channelResult::getChannel)
                    .collect(Collectors.toSet());
            
            String metadata = null;
            if (request.getMetadata() != null) {
                metadata = objectMapper.writeValueAsString(request.getMetadata());
            }
            
            Alert alert = Alert.builder()
                    .message(request.getMessage())
                    .severity(request.getSeverity())
                    .event(request.getEvent())
                    .status(response.getStatus())
                    .cost(cost)
                    .channelsSent(channelsSet)
                    .metadata(metadata)
                    .build();

            if (alert != null) {
                alertRepository.save(alert);
                log.info("Alert saved to database with ID: {}", alert.getId());
            }
        } catch (Exception e) {
            log.error("Failed to save alert to database: {}", e.getMessage());
        }
    }
    private String getDefaultRecipient(List<String> recipients,String channel){
        String firstRecipient = (recipients != null && !recipients.isEmpty()) ? recipients.get(0) : null;
        if (firstRecipient != null && !firstRecipient.isBlank()) {
            return firstRecipient;
        }
        return switch(channel){
            case "sms" ->"+1234567890";
            case "email" -> "admin@example.com";
            case "slack"->"#alerts";
            default->"";
        };

    }
    public List<Alert> getRecentAlerts(){
        return alertRepository.findTop10ByOrderByCreatedAtDesc();
    }

}
