package com.example.aiservice.controller;

import com.example.aiservice.models.AiRecommendation;
import com.example.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AiRecommendation>> getUserRecommendations(@PathVariable String userId) {
        // Placeholder for actual recommendation logic
        List<AiRecommendation> recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/activities/{activityType}")
    public ResponseEntity<AiRecommendation> getActivityRecommendations(@PathVariable String activityType) {
        // Placeholder for actual recommendation logic
        return ResponseEntity.ok(recommendationService.getRecommendationsForActivity(activityType));
    }


}
