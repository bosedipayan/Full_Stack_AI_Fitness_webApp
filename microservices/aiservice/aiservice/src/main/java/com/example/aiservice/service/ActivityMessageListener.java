package com.example.aiservice.service;

import com.example.aiservice.models.Activity;
import com.example.aiservice.models.AiRecommendation;
import com.example.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService activityAIService;

    private final RecommendationRepository recommendationRepository;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void processActivity(Activity activity) {
        log.info("Received activity: {}", activity.getUserId());
        AiRecommendation recommendation = activityAIService.generateRecommendation(activity);
        // Here you can add logic to process the activity, e.g., save it to a database or trigger other actions

        recommendationRepository.save(recommendation);
    }
}
