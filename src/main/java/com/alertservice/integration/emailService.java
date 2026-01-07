package com.alertservice.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class emailService {
    private static final Logger log = LoggerFactory.getLogger(emailService.class);
    private JavaMailSender mailSender;

    @Value("$alert.channels.email.from")
    private String fromEmail;

    @Value("$spring.mail.username")
    private String smtpUsername;

    public boolean sendEmailTo(String emailTo,String subject, String body){
        try{
            if(smtpUsername==null||smtpUsername.startsWith("your-email")){
                log.info("Email (SIMULATED) to {}: {}", emailTo, subject);
                return true;
            }
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent successfully to {}",emailTo);
            return true;

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", emailTo, e.getMessage());
            throw new RuntimeException("Email send fail",e);
        }
    }
    public double cost(){
        return 0.001;
    }
}
