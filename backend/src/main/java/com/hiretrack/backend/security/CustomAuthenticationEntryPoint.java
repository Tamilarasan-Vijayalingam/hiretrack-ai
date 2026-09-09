package com.hiretrack.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpStatus.UNAUTHORIZED.value());
        data.put("error", "Unauthorized");
        data.put("message", authException.getMessage());
        data.put("path", request.getServletPath());

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
