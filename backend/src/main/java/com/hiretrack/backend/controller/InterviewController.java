package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.InterviewDTO;
import com.hiretrack.backend.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService service;

    @PostMapping
    public ResponseEntity<InterviewDTO> create(@RequestBody InterviewDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<InterviewDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewDTO> update(@PathVariable UUID id, @RequestBody InterviewDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
