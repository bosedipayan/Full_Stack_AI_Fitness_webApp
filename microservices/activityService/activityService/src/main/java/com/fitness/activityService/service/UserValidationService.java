package com.fitness.activityService.service;

import com.fitness.activityService.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient userServiceClient;

    public boolean validateUser(String userId) {
        log.info("Validating user with ID: {}", userId);
        try {
            userServiceClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(); // User exists
        } catch (WebClientResponseException e) {
            e.printStackTrace(); // User does not exist or an error occurred
        }
        return false;
    }
}