package com.example.aiservice.service;

import com.example.aiservice.models.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.consumer.group-id}")
    public void processActivity(Activity activity) {
        log.info("Received activity: {}", activity.getUserId());
        // Here you can add logic to process the activity, e.g., save it to a database or trigger other actions
    }
}
