package com.hiretrack.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, RateLimitTracker> trackers = new ConcurrentHashMap<>();

    private static class RateLimitTracker {
        int count = 0;
        long windowStart = System.currentTimeMillis();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/api/auth/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String clientIp = getClientIP(request);
            RateLimitTracker tracker = trackers.computeIfAbsent(clientIp, k -> new RateLimitTracker());

            synchronized (tracker) {
                long now = System.currentTimeMillis();
                if (now - tracker.windowStart > 60000) { // 1 minute window
                    tracker.count = 0;
                    tracker.windowStart = now;
                }

                if (tracker.count >= 5) {
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.getWriter().write("Too many login attempts. Please try again later.");
                    return;
                }
                tracker.count++;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
