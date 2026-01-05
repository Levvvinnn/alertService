package com.alertservice.integration;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import jdk.internal.org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class smsService {

    @Value("${twilio.phone-number}")
    private String fromNumber;

    @Value("${twilio.account-sid")
    private String accountSid;

    public boolean sendSms(String toNumber,String message){
        try{
            Log log=null;
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
            Log log=null;
            log.error("Failed to send sms to {}:{}",toNumber,e.getMessage());
            throw new RuntimeException("SMS failed",e);
        }
    }
    public double cost(){
        return 0.01;
    }
}
