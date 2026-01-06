package com.alertservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.org.jline.utils.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class slackService {
    @Value("$slack.webhook-url")
    private String webhookurl;

    private final RestTemplate restTemplate=new RestTemplate();
    private final ObjectMapper objectMapper=new ObjectMapper();
    private Object request;

    public boolean sendSlackMessage(String channel,String message,String severity){
        try{
            Log log=null;
            if(webhookurl==null||webhookurl.startsWith("YOUR/WEBHOOK")){
                log.info("Slack (SIMULATED) to {}: {}", channel, message);
                return true;
            }
            ResponseEntity<String> response=restTemplate.postForEntity(
                    webhookurl,
                    request,
                    String.class
            );
            if(response.getStatusCode()==HttpStatus.OK){
                log.info("Slack message sent successfully to {}", channel);
                return true;
            } else {
                log.error("Slack returned status: {}", response.getStatusCode());
                return false;
            }

        }catch(Exception e){
            Log log=null;
            log.error("Failed to send slack message :{}",e.getMessage());
            throw new RuntimeException("Slack send failed",e);
        }
    }
    private String formatMessage(String message,String severity){
        String label=switch(severity.toLowerCase()){
            case "critical"->"⚠️";
            case "warning"->"🚨";
            default->"ℹ️";
        };
        return label+severity.toUpperCase()+message;
    }
    public double cost(){
        return 0.0;
    }

}
