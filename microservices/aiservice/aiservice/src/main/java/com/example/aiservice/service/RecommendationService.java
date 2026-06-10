package com.example.aiservice.service;

import com.example.aiservice.models.AiRecommendation;
import com.example.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    public List<AiRecommendation> getRecommendationsForUser(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public @Nullable AiRecommendation getRecommendationsForActivity(String activityType) {
        return recommendationRepository.findByActivityType(activityType).orElseThrow(() -> new RuntimeException("No recommendations found for this activity type"));
    }
}
