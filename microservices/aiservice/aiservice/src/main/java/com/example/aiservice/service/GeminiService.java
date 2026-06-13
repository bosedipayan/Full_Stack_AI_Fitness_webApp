package com.example.aiservice.service;

import com.fitness.activityService.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientConfig) {
        this.webClient = webClientConfig.build();
    }
    public String getRecommendations(String prompt)
    {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                    Map.of(
                            "parts", new Object[] {
                                Map.of(
                                        "text", prompt
                                )
                            }
                            )
                }
        );
        // Implementation for sending request and receiving response would go here

        String response = webClient.post()
                .uri(geminiApiUrl)
                .header("Content-Type", "application/json")
                .header("Google-api-key", geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
