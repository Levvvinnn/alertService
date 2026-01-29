package com.alertservice.controller;

import com.alertservice.entity.Alert;
import com.alertservice.model.alertRequest;
import com.alertservice.service.alertService;
import com.alertservice.model.alertResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
 
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class alertController {
    private final alertService alertService;

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
        return ResponseEntity.ok(Map.of(
                "Service", "Alert-Service",
                "Status", "UP",
                "Version", "1.0.0"
        ));
    }

    @GetMapping("test/{severity}")
    public ResponseEntity<alertResponse> test(@PathVariable String severity){
        alertRequest request = alertRequest.builder()
                .message("This is a test " + severity + " alert from the system")
                .severity(severity)
                .event("Test alert")
                .build();
        alertResponse response = alertService.processAlert(request);
        return ResponseEntity.ok(response);
    }


}
