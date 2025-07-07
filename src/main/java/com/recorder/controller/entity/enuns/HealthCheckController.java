package com.recorder.controller.entity.enuns;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthCheckController implements HealthIndicator {

    // Formato ISO 8601 para timestamp
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_INSTANT
            .withZone(ZoneId.systemDefault());

    @GetMapping("/health-check")
    public Map<String, Object> healthCheck() {
        Map<String, Object> healthDetails = new HashMap<>();
        healthDetails.put("status", "UP");
        healthDetails.put("backend", "Spring Boot");
        healthDetails.put("timestamp", TIMESTAMP_FORMATTER.format(Instant.now()));
        healthDetails.put("service", "Recorder API");
        healthDetails.put("version", "1.0.0");

        return healthDetails;
    }

    @Override
    public Health health() {
        // Integração com Spring Boot Actuator
        return Health.up()
                .withDetail("timestamp", TIMESTAMP_FORMATTER.format(Instant.now()))
                .build();
    }
}