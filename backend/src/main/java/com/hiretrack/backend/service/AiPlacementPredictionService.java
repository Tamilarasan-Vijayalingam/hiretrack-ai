package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.AiPlacementPrediction;
import com.hiretrack.backend.dto.AiPlacementPredictionDTO;
import java.util.List;
import java.util.UUID;

public interface AiPlacementPredictionService {
    AiPlacementPredictionDTO create(AiPlacementPredictionDTO dto);
    AiPlacementPredictionDTO update(UUID id, AiPlacementPredictionDTO dto);
    AiPlacementPredictionDTO getById(UUID id);
    List<AiPlacementPredictionDTO> getAll();
    void delete(UUID id);
}
