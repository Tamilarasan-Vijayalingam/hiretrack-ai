package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.SkillsGapAnalysis;
import com.hiretrack.backend.dto.SkillsGapAnalysisDTO;
import java.util.List;
import java.util.UUID;

public interface SkillsGapAnalysisService {
    SkillsGapAnalysisDTO create(SkillsGapAnalysisDTO dto);
    SkillsGapAnalysisDTO update(UUID id, SkillsGapAnalysisDTO dto);
    SkillsGapAnalysisDTO getById(UUID id);
    List<SkillsGapAnalysisDTO> getAll();
    void delete(UUID id);
}
