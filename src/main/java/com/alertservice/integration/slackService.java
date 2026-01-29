package com.alertservice.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;


@Service
@Slf4j
public class slackService {
    private final String webhookurl;

    @Value("${app.simulate.slack:false}")
    private boolean simulateSlack;

    private final RestTemplate restTemplate = new RestTemplate();

    public slackService(@Value("${slack.webhook-url:}") String webhookurl) {
        this.webhookurl = webhookurl;
    }

    public boolean sendSlackMessage(String channel, String message, String severity) {
        try {
            // treat empty/blank webhook as simulated as well
            if (simulateSlack || webhookurl == null || webhookurl.isBlank() || webhookurl.startsWith("YOUR/WEBHOOK")) {
                log.info("Slack (SIMULATED) to {}:{} {}", channel, severity, message);
                return true;
            }

            Map<String, String> payload = Map.of(
                    "text", String.format("%s: %s", severity != null ? severity.toUpperCase() : "ALERT", message),
                    "channel", channel != null ? channel : ""
            );

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, String>> req = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(webhookurl != null ? webhookurl : "", req, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Slack message sent successfully to {}", channel);
                return true;
            } else {
                log.error("Slack returned status: {}", response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            log.error("Failed to send slack message :{}", e.getMessage());
            throw new RuntimeException("Slack send failed", e);
        }
    }

    public double cost() {
        return 0.0;
    }

}
