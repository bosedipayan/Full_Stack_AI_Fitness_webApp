package com.example.aiservice.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "aiRecommendationService")
@Data
@Builder
public class AiRecommendation {
    @Id
    private String id;
    private String activityId;
    private String type;// e.g., "running", "cycling", "yoga"
    private String recommendationText; // e.g., "Based on your recent runs, we recommend trying interval training to improve your speed."
    private String userId; // ID of the user this recommendation is for
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safetyTips;

    @CreatedDate
    private LocalDateTime createdAt;
}
