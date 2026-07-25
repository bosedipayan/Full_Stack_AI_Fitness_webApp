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
        try{
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

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));

        } catch (Exception e) {
            log.error("Error processing AI response: {}", e.getMessage());
            // Handle the error appropriately, e.g., return a default recommendation or throw a custom exception
        }

        return null;
    }

    private List<String> extractImprovements(JsonNode improvements) {
        return improvements.findValuesAsText("recommendation");
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
