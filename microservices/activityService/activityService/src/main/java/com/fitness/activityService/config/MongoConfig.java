package com.fitness.activityService.config;

import com.fitness.activityService.model.Activity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
