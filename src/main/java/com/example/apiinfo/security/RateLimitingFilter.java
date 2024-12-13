package com.example.apiinfo.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitingFilter implements Filter {

    // Create a bucket with a limit of 5 requests per second
    private final Bucket bucket = Bucket4j.builder()
            .addLimit(Bandwidth.classic(7, Refill.greedy(7, Duration.ofSeconds(1))))
            .build();

    @Override
    public void doFilter(jakarta.servlet.ServletRequest servletRequest,
                         jakarta.servlet.ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Apply rate limiting only to specific API paths (e.g., UserInfo API)
        if (request.getRequestURI().startsWith("/api/user-info")) {
            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response); // Allow the request if within limit
            } else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429 Too Many Requests
                response.getWriter().write("Too many requests. Please try again later.");
            }
        } else {
            chain.doFilter(request, response); // Allow other requests without rate-limiting
        }
    }
}
