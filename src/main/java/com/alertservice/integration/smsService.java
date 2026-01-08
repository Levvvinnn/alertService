package com.alertservice.integration;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class smsService {
    private static final Logger log = LoggerFactory.getLogger(smsService.class);

    // provide empty defaults so placeholder resolution never fails
    @Value("${twilio.phone-number:}")
    private String fromNumber;

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    private boolean twilioEnabled = false;

    @PostConstruct
    public void init() {
        twilioEnabled = accountSid != null && !accountSid.isBlank() &&
                authToken != null && !authToken.isBlank() &&
                fromNumber != null && !fromNumber.isBlank();
        if (twilioEnabled) {
            log.info("smsService: Twilio enabled (fromNumber set)");
        } else {
            log.info("smsService: Twilio disabled (running in simulated mode)");
        }
    }

    public boolean sendSms(String toNumber, String message) {
        try {
            if (!twilioEnabled) {
                log.info("SMS (SIMULATED) to {} from {}: {}", toNumber, fromNumber, message);
                return true;
            }

            Message twilioMessage = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    message
            ).create();

            log.info("SMS sent successfully. SID: {}", twilioMessage.getSid());
            return true;
        } catch (Exception e) {
            log.error("Failed to send sms to {}: {}", toNumber, e.getMessage(), e);
            return false;
        }
    }

    public double cost() {
        return 0.01;
    }
}
