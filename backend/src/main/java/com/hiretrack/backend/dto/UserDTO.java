package com.hiretrack.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "Role is required")
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
