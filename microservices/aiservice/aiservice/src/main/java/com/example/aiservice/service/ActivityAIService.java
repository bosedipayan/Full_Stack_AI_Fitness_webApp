package com.example.aiservice.service;

import com.example.aiservice.models.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ActivityAIService {
    private final GeminiService geminiService;

    public void generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        log.info("RESPONSE FROM AI {}", geminiService.getRecommendations(prompt));
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("Generate a recommendation for the following activity: %s. Include details about the " +
                "activity, its benefits, and any tips for getting started.", activity.getType());
    }
}
