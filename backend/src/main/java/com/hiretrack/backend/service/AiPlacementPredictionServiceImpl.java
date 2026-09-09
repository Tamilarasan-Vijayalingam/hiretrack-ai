package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.AiPlacementPrediction;
import com.hiretrack.backend.dto.AiPlacementPredictionDTO;
import com.hiretrack.backend.repository.AiPlacementPredictionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiPlacementPredictionServiceImpl implements AiPlacementPredictionService {
    private final AiPlacementPredictionRepository repository;

    @Override
    public AiPlacementPredictionDTO create(AiPlacementPredictionDTO dto) { return null; }
    @Override
    public AiPlacementPredictionDTO update(UUID id, AiPlacementPredictionDTO dto) { return null; }
    @Override
    public AiPlacementPredictionDTO getById(UUID id) { return null; }
    @Override
    public List<AiPlacementPredictionDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
