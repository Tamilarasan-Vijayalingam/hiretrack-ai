package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Skill;
import com.hiretrack.backend.dto.SkillDTO;
import com.hiretrack.backend.repository.SkillRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository repository;

    @Override
    public SkillDTO create(SkillDTO dto) { return null; }
    @Override
    public SkillDTO update(UUID id, SkillDTO dto) { return null; }
    @Override
    public SkillDTO getById(UUID id) { return null; }
    @Override
    public List<SkillDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
