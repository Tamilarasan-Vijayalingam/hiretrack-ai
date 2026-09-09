package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.StudentDTO;
import com.hiretrack.backend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PLACEMENT_OFFICER')")
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.canAccessStudentData(authentication, #id)")
    public ResponseEntity<StudentDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD')")
    public ResponseEntity<List<StudentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @securityService.canAccessStudentData(authentication, #id))")
    public ResponseEntity<StudentDTO> update(@PathVariable UUID id, @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
