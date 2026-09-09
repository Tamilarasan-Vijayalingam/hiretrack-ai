package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.AiPlacementReadiness;
import com.hiretrack.backend.dto.AiPlacementReadinessDTO;
import com.hiretrack.backend.repository.AiPlacementReadinessRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiPlacementReadinessServiceImpl implements AiPlacementReadinessService {
    private final AiPlacementReadinessRepository repository;

    @Override
    public AiPlacementReadinessDTO create(AiPlacementReadinessDTO dto) { return null; }
    @Override
    public AiPlacementReadinessDTO update(UUID id, AiPlacementReadinessDTO dto) { return null; }
    @Override
    public AiPlacementReadinessDTO getById(UUID id) { return null; }
    @Override
    public List<AiPlacementReadinessDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
