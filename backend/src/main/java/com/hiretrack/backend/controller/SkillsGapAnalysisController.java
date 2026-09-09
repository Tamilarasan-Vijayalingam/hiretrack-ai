package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.SkillsGapAnalysisDTO;
import com.hiretrack.backend.service.SkillsGapAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skillsgapanalysiss")
@RequiredArgsConstructor
public class SkillsGapAnalysisController {
    private final SkillsGapAnalysisService service;

    @PostMapping
    public ResponseEntity<SkillsGapAnalysisDTO> create(@RequestBody SkillsGapAnalysisDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillsGapAnalysisDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SkillsGapAnalysisDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillsGapAnalysisDTO> update(@PathVariable UUID id, @RequestBody SkillsGapAnalysisDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
