package com.alertservice.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConditionalOnProperty(
        prefix = "twilio",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class twilioConfig {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }
}
