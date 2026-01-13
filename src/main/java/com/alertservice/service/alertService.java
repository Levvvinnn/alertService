package com.alertservice.service;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.alertservice.entity.Alert;
import com.alertservice.model.alertRequest;
import com.alertservice.model.alertResponse;
import com.alertservice.repository.alertRepository;
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
public class alertService {
    private static final Logger log = LoggerFactory.getLogger(alertService.class);
    private  final smsService smsService;
    private  final emailService emailService;
    private  final slackService slackService;
    private final alertRepository alertRepository;
    private final ObjectMapper objectMapper;

    public alertService(
            smsService smsService,
            emailService emailService,
            slackService slackService, com.alertservice.repository.alertRepository alertRepository, ObjectMapper objectMapper
    ) {
        this.smsService = smsService;
        this.emailService = emailService;
        this.slackService = slackService;
        this.alertRepository = alertRepository;
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
        long successCount = results.stream().filter(alertResponse.channelResult::isSuccess).count();
        if (successCount == results.size()) {
            response.setStatus("success");
        } else if (successCount > 0) {
            response.setStatus("partial");
        } else {
            response.setStatus("failed");
        }
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
    private void saveAlert(alertRequest request,alertResponse response,double cost){
        try{
            Alert alert=new Alert();
            alert.setMessage(request.getMessage());
            alert.setSeverity(request.getSeverity());
            alert.setEvent(request.getEvent());
            alert.setStatus(response.getStatus());
            alert.setCost(cost);
            List<String> channels = response.getChannelResults().stream()
                    .map(alertResponse.channelResult::getChannel)
                    .toList();
            alert.setChannelsSent(String.join(",", channels));

            if (request.getMetadata() != null) {
                alert.setMetadata(objectMapper.writeValueAsString(request.getMetadata()));
            }

            alertRepository.save(alert);
            log.info("Alert saved to database with ID: {}", alert.getId());
        } catch (Exception e) {
            log.error("Failed to save alert to database: {}", e.getMessage());
        }
    }
    private String getDefaultRecipient(List<String> recipients,String channel){
        if(recipients!=null){
            return recipients.getFirst();
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
