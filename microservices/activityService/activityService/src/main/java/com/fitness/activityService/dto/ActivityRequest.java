package com.fitness.activityService.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ActivityRequest {
    private String userId;
    private String type; // e.g., "running", "cycling"
    private Integer caloriesBurned; // in calories
    private Integer duration; // in minutes
    private String startTime; // ISO format date string
    private Map<String, Object> additionalMetrices;
    // Getters and Setters
}
