package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.CourseDTO;
import com.hiretrack.backend.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @PostMapping
    public ResponseEntity<CourseDTO> create(@RequestBody CourseDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable UUID id, @RequestBody CourseDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
