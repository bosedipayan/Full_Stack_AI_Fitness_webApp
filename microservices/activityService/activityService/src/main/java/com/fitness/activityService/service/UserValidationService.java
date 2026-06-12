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
            System.out.println("Before WebClient call");

            Boolean isValid = userServiceClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            System.out.println("After WebClient call");

            return Boolean.TRUE.equals(isValid);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}