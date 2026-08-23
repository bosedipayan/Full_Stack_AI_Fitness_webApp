package com.example.aiservice.service;

import com.example.aiservice.models.Activity;
import com.example.aiservice.models.AiRecommendation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ActivityAIService {
    private final GeminiService geminiService;

    public AiRecommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getRecommendations(prompt);
        log.info("RESPONSE FROM AI {}", aiResponse);

        return processAIResponse(activity, aiResponse);
    }

    private AiRecommendation processAIResponse(Activity activity, String aiResponse) {
        StringBuilder fullAnalysis = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("contents")
                    .get(0)
                    .path("parts")
                    .get(0)
                    .path("text");

            String recommendationText = textNode.asText()
                    .replaceAll("```json\\n", "") // Replace escaped newlines with actual newlines;
                    .replaceAll("\\n```", "") // Replace escaped quotes with actual quotes
                    .trim();

//            log.info("Parsed recommendation text: {}", recommendationText);

            JsonNode analysisJson = objectMapper.readTree(recommendationText);
            JsonNode analysisNode = analysisJson.path("analysis");

            fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return AiRecommendation.builder()
                    .activityId(activity.getId())
                    .activityType(String.valueOf(activity.getType()))
                    .recommendationText(fullAnalysis.toString())
                    .userId(activity.getUserId())
                    .improvements(List.of("No specific improvements suggested."))
                    .suggestions(List.of("No specific suggestions provided."))
                    .safetyTips(List.of("No specific safety guidelines provided."))
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
//            log.error("Error processing AI response: {}", e.getMessage());
            e.printStackTrace();
            return createDefaultRecommendation(activity);
            // Handle the error appropriately, e.g., return a default recommendation or throw a custom exception
        }
    }

    private AiRecommendation createDefaultRecommendation(Activity activity) {
        return AiRecommendation.builder()
                .activityId(activity.getId())
                .activityType(String.valueOf(activity.getType()))
                .recommendationText("No specific recommendations available at this time.")
                .userId(activity.getUserId())
                .improvements(Collections.singletonList("Continue with your current routine and monitor your progress."))
                .suggestions(Collections.singletonList("Consider consulting a fitness consultant"))
                .safetyTips(Arrays.asList("Ensure proper hydration before and after workouts.", "Warm up before starting any exercise.", "Listen to your body and avoid overexertion."))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safety) {
        List<String> safetyList = new ArrayList<>();

        if(safety.isArray())
        {
            safety.forEach(e -> safetyList.add(e.asText()));
        }

        return safetyList.isEmpty() ? List.of("No specific safety guidelines provided.") : safetyList;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestionList = new java.util.ArrayList<>();

        if(suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion ->{
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestionList.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestionList.isEmpty() ? List.of("No specific suggestions provided.") : suggestionList;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new java.util.ArrayList<>();
        if(improvementsNode.isArray()) {
            improvementsNode.forEach(improvement ->{
                String area = improvement.path("area").asText();
                String recommendation = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, recommendation));
            });
        }
        return improvements.isEmpty() ? List.of("No specific improvements suggested.") : improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(analysisNode.has(key)) {
            fullAnalysis.append(prefix)
                    .append(" ")
                    .append(analysisNode.get(key).asText()).append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrices()
        );
    }
}
