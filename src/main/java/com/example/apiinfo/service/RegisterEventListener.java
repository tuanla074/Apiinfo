package com.example.apiinfo.service;

import com.example.apiinfo.model.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegisterEventListener {

    @Autowired
    private UserInfoService userInfoService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "register-event-topic", groupId = "api-info-group")
    public void listenToRegisterEvent(String eventJson) {
        System.out.println("APIinfo received event: " + eventJson);

        try {
            // Parse the incoming JSON string into a Map
            Map<String, Object> eventData = objectMapper.readValue(eventJson, new TypeReference<Map<String, Object>>() {});

            // Extract specific fields
            Long userId = Long.valueOf(eventData.get("userId").toString());
            Integer age = (Integer) eventData.get("age");
            String addr = (String) eventData.get("addr");

            // Log extracted data
            UserInfo userInfo = new UserInfo();

            // Set the fields
            userInfo.setAge(age);
            userInfo.setAddress(addr);
            userInfo.setUserId(userId);


            userInfoService.addUserInfo(userInfo);

        } catch (Exception e) {
            System.err.println("Error processing RegisterEvent: " + e.getMessage());
        }
    }
}
