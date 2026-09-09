package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.SkillDTO;
import com.hiretrack.backend.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService service;

    @PostMapping
    public ResponseEntity<SkillDTO> create(@RequestBody SkillDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillDTO> update(@PathVariable UUID id, @RequestBody SkillDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
