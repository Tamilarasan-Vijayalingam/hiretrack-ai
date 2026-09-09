package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Skill;
import com.hiretrack.backend.dto.SkillDTO;
import java.util.List;
import java.util.UUID;

public interface SkillService {
    SkillDTO create(SkillDTO dto);
    SkillDTO update(UUID id, SkillDTO dto);
    SkillDTO getById(UUID id);
    List<SkillDTO> getAll();
    void delete(UUID id);
}
