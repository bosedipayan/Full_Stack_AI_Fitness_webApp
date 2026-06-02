package com.fitness.activityService.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private String id;
    private String userId;
    private String type; // e.g., "running", "cycling"
    private Integer caloriesBurned; // in calories
    private Integer duration; // in minutes
    private String startTime; // ISO format date string
    private Map<String, Object> additionalMetrices;
    private LocalDateTime createdAt; // For any extra data specific to the activity type
    private LocalDateTime updatedAt;


}
