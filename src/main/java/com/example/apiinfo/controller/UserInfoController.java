package com.example.apiinfo.controller;

import com.example.apiinfo.model.UserInfo;
import com.example.apiinfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService; // Use the service instead of the repository

    // GET all user_info records
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUserInfo() {
        List<UserInfo> userInfoList = userInfoService.getAllUserInfo();
        return ResponseEntity.ok(userInfoList);
    }

    @PostMapping
    public ResponseEntity<UserInfo> addUserInfo(@RequestBody UserInfo userInfo) {
        UserInfo savedUserInfo = userInfoService.addUserInfo(userInfo);
        return ResponseEntity.ok(savedUserInfo);
    }

    // GET user_info by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserInfoById(@PathVariable Long id) {
        UserInfo userInfo = userInfoService.getUserInfoById(id);
        return ResponseEntity.ok(userInfo);
    }

    // GET user_info by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserInfo> getUserInfoByUserId(@PathVariable Long userId) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        return ResponseEntity.ok(userInfo);
    }
}
