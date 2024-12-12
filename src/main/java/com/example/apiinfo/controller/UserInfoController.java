package com.example.apiinfo.controller;

import com.example.apiinfo.model.UserInfo;
import com.example.apiinfo.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoRepo userInfoRepository;

    // GET all user_info records
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUserInfo() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        return ResponseEntity.ok(userInfoList);
    }

    // GET user_info by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserInfoById(@PathVariable Long id) {
        return userInfoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET user_info by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserInfo> getUserInfoByUserId(@PathVariable Long userId) {
        return userInfoRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
