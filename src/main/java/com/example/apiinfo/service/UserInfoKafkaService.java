package com.example.apiinfo.service;

import com.example.apiinfo.model.UserInfo;
import com.example.apiinfo.repo.UserInfoRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserInfoKafkaService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired
    private UserInfoProducer userInfoProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-id-topic", groupId = "api-info-group")
    public void consumeUserId(String message) {
        System.out.println("Received message: " + message);

        // Parse the userId from the message
        Long userId = extractUserIdFromMessage(message);
        System.out.println("Extracted userId: " + userId);

        // Fetch UserInfo from the repository
        UserInfo userInfo = userInfoRepo.findByUserId(userId).orElse(null);

        if (userInfo != null) {
            try {
                // Convert UserInfo to JSON
                String userInfoJson = objectMapper.writeValueAsString(userInfo);

                // Use producer to send UserInfo to Kafka
                userInfoProducer.sendMessage(userId, userInfoJson);
            } catch (Exception e) {
                System.err.println("Error serializing UserInfo: " + e.getMessage());
            }
        } else {
            System.out.println("No UserInfo found for userId: " + userId);
        }
    }

    private Long extractUserIdFromMessage(String message) {
        // Assuming the message format is "Requesting user info for ID: <userId>"
        try {
            return Long.parseLong(message.split(":")[1].trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format: " + message);
        }
    }
}
