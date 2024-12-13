package com.example.apiinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {

    @Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;

    public void sendMessage(Long userId, String userInfoJson) {
        kafkaTemplate.send("user-info-topic", userId, userInfoJson);
        System.out.println("Sent user info to Kafka: " + userInfoJson);
    }
}
