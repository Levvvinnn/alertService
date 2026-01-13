package com.alertservice.controller;

import com.alertservice.entity.Alert;
import com.alertservice.model.alertRequest;
import com.alertservice.service.alertService;
import com.alertservice.model.alertResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class alertController {
    private static final Logger log = LoggerFactory.getLogger(alertController.class);
    private final alertService alertService;

    public alertController(alertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/alerts")
    public ResponseEntity<alertResponse> recieveWebhook(@Valid @RequestBody alertRequest request){
        log.info("Webhook received:{}",request);
        alertResponse response=alertService.processAlert(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alerts/history")
    public ResponseEntity<List<Alert>> getHistory(){
        List<Alert> alerts=alertService.getRecentAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/alerts/health")
    public ResponseEntity<Map<String,String>> health(){
        Map<String,String> health=new HashMap<>();
        health.put("Service","Alert-Service");
        health.put("Status","UP");
        health.put("Version","1.0.0");
        return ResponseEntity.ok(health);
    }

    @GetMapping("test/{severity}")
    public ResponseEntity<alertResponse> test(@PathVariable String severity){
        alertRequest request=new alertRequest();
        request.setMessage("This is a test "+severity+" alert from the system");
        request.setSeverity(severity);
        request.setEvent("Test alert");
        alertResponse response=alertService.processAlert(request);
        return ResponseEntity.ok(response);
    }


}
