package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.SkillsGapAnalysis;
import com.hiretrack.backend.dto.SkillsGapAnalysisDTO;
import com.hiretrack.backend.repository.SkillsGapAnalysisRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillsGapAnalysisServiceImpl implements SkillsGapAnalysisService {
    private final SkillsGapAnalysisRepository repository;

    @Override
    public SkillsGapAnalysisDTO create(SkillsGapAnalysisDTO dto) { return null; }
    @Override
    public SkillsGapAnalysisDTO update(UUID id, SkillsGapAnalysisDTO dto) { return null; }
    @Override
    public SkillsGapAnalysisDTO getById(UUID id) { return null; }
    @Override
    public List<SkillsGapAnalysisDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
