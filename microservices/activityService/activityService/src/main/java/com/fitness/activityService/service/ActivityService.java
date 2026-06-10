package com.fitness.activityService.service;

import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import com.fitness.activityService.model.Activity;
import com.fitness.activityService.model.ActivityType;
import com.fitness.activityService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest request) {
        // Placeholder for tracking an activity
        // In a real implementation, this would involve saving the activity to a database
        // and performing any necessary calculations (e.g., calories burned)
        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if (!isValidUser) {
            throw new RuntimeException("Invalid user ID");
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(ActivityType.valueOf(request.getType()))
                .caloriesBurned(request.getCaloriesBurned())
                .duration(request.getDuration())
                .startTime(LocalDateTime.parse(request.getStartTime()))
                .additionalMetrices(request.getAdditionalMetrices())
                .build();

        Activity saved = activityRepository.save(activity);

        try{
            kafkaTemplate.send(topicName, saved.getUserId(), saved);
        } catch (Exception e) {
            // Handle Kafka exceptions (e.g., log the error, retry logic, etc.)
            System.err.println("Failed to send activity event to Kafka: " + e.getMessage());
        }
        return mapToResponse(saved);
    }

    private ActivityResponse mapToResponse(Activity saved) {
        ActivityResponse response = new ActivityResponse();
        response.setId(saved.getId());
        response.setUserId(saved.getUserId());
        response.setType(saved.getType().name());
        response.setCaloriesBurned(saved.getCaloriesBurned());
        response.setDuration(saved.getDuration());
        response.setStartTime(saved.getStartTime().toString());
        response.setAdditionalMetrices(saved.getAdditionalMetrices());
        return response;
    }
}
