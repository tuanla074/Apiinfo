package com.example.apiinfo.service;

import com.example.apiinfo.model.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegisterEventListener {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "register-event-topic", groupId = "api-info-group")
    public void listenToRegisterEvent(String eventJson) {
        System.out.println("APIinfo received event: " + eventJson);

        try {
            // Parse JSON input
            JsonNode eventNode = objectMapper.readTree(eventJson);

            // Transform the input JSON to the required Elasticsearch format
            String userId = eventNode.get("id").asText();
            String username = eventNode.get("username").asText();
            String fullname = eventNode.get("fullname").asText();
            int age = eventNode.get("age").asInt();
            String address = eventNode.get("address").asText();

            // Construct the Elasticsearch-compatible JSON
            String elasticSearchJson = String.format(
                    "{ \"users\": { \"id\": \"%s\", \"username\": \"%s\", \"fullname\": \"%s\", \"userinfo\": { \"age\": %d, \"address\": \"%s\" } } }",
                    userId, username, fullname, age, address
            );
            // Log extracted data
            UserInfo userInfo = new UserInfo();

            // Set the fields
            userInfo.setAge(age);
            userInfo.setAddress(address);
            userInfo.setUserId(Long.valueOf(userId));

            userInfoService.addUserInfo(userInfo);
            elasticSearchService.saveJsonToElasticsearch(userId, elasticSearchJson);

        } catch (Exception e) {
            System.err.println("Error processing RegisterEvent: " + e.getMessage());
        }
    }
}
