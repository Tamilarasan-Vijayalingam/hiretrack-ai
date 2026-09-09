package com.hiretrack.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpStatus.FORBIDDEN.value());
        data.put("error", "Forbidden");
        data.put("message", "You do not have permission to access this resource.");
        data.put("path", request.getServletPath());

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
