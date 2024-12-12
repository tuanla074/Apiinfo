package com.example.apiinfo.service;

import com.example.apiinfo.model.UserInfo;
import com.example.apiinfo.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USER_INFO_CACHE_PREFIX = "user_info:";
    private static final String ALL_USER_INFO_CACHE_KEY = "all_user_info";

    // Get user_info by ID with caching
    public UserInfo getUserInfoById(Long id) {
        String cacheKey = USER_INFO_CACHE_PREFIX + id;

        // Check Redis cache
        UserInfo cachedUserInfo = (UserInfo) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUserInfo != null) {
            System.out.println("Cache hit for key: " + cacheKey); // Debug
            return cachedUserInfo;
        }

        System.out.println("Cache miss for key: " + cacheKey); // Debug

        // Fetch from database if not cached
        UserInfo userInfo = userInfoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Store in Redis with a TTL of 30 seconds
        redisTemplate.opsForValue().set(cacheKey, userInfo, 30, TimeUnit.SECONDS);
        System.out.println("Cached user info for key: " + cacheKey); // Debug

        return userInfo;
    }

    // Get all user_info records with caching
    public List<UserInfo> getAllUserInfo() {
        // Check Redis cache
        @SuppressWarnings("unchecked")
        List<UserInfo> cachedUserInfoList = (List<UserInfo>) redisTemplate.opsForValue().get(ALL_USER_INFO_CACHE_KEY);
        if (cachedUserInfoList != null) {
            System.out.println("Cache hit for key: " + ALL_USER_INFO_CACHE_KEY); // Debug
            return cachedUserInfoList;
        }

        System.out.println("Cache miss for key: " + ALL_USER_INFO_CACHE_KEY); // Debug

        // Fetch from database if not cached
        List<UserInfo> userInfoList = userInfoRepo.findAll();

        // Store in Redis with a TTL of 30 seconds
        redisTemplate.opsForValue().set(ALL_USER_INFO_CACHE_KEY, userInfoList, 30, TimeUnit.SECONDS);
        System.out.println("Cached all user info for key: " + ALL_USER_INFO_CACHE_KEY); // Debug

        return userInfoList;
    }

    // Get user_info by userId with caching
    public UserInfo getUserInfoByUserId(Long userId) {
        String cacheKey = USER_INFO_CACHE_PREFIX + userId;

        // Check Redis cache
        UserInfo cachedUserInfo = (UserInfo) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUserInfo != null) {
            System.out.println("Cache hit for key: " + cacheKey); // Debug
            return cachedUserInfo;
        }

        System.out.println("Cache miss for key: " + cacheKey); // Debug

        // Fetch from database if not cached
        UserInfo userInfo = userInfoRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));

        // Store in Redis with a TTL of 30 seconds
        redisTemplate.opsForValue().set(cacheKey, userInfo, 30, TimeUnit.SECONDS);
        System.out.println("Cached user info for key: " + cacheKey); // Debug

        return userInfo;
    }
}
