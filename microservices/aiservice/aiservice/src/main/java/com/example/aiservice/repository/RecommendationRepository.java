package com.example.aiservice.repository;

import com.example.aiservice.models.AiRecommendation;
import org.jspecify.annotations.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<AiRecommendation, String> {
    List<AiRecommendation> findByUserId(String userId);

     Optional<AiRecommendation> findByActivityType(String activityType);
}
