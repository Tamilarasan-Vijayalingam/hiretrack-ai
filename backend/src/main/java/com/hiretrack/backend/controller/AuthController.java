package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.AuthRequest;
import com.hiretrack.backend.dto.AuthResponse;
import com.hiretrack.backend.dto.UserDTO;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.repository.UserRepository;
import com.hiretrack.backend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().name());

        return ResponseEntity.ok(new AuthResponse(jwt, userDTO));
    }
}
