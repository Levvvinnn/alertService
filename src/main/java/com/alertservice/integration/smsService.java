package com.alertservice.integration;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class smsService {
    private static final Logger log = LoggerFactory.getLogger(smsService.class);

    @Value("${twilio.phone-number}")
    private String fromNumber;

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;


    public boolean sendSms(String toNumber,String message){
        try{
            if(accountSid==null || accountSid.startsWith("your-")){
                log.info("SMS sent to {}:{}",toNumber,message);
                return true;
            }
            Message twilioMessage=Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    message
            ).create();

            log.info("SMS sent successfully. SID: {}", twilioMessage.getSid());
            return true;
        }catch(Exception e){
            log.error("Failed to send sms to {}:{}",toNumber,e.getMessage());
            throw new RuntimeException("SMS failed",e);
        }
    }
    public double cost(){
        return 0.01;
    }
}
