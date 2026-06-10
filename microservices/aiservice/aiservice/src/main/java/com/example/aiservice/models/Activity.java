package com.example.aiservice.models;

import com.fitness.activityService.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private String id;
    private String userId;
    private ActivityType type; // e.g., "running", "cycling"
    private Integer caloriesBurned; // in calories
    private Integer duration; // in minutes
    private LocalDateTime startTime; // ISO format date string

    @Field("metrices")
    private Map<String, Object> additionalMetrices;

    @CreatedDate
    private LocalDateTime createdAt; // For any extra data specific to the activity type

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
