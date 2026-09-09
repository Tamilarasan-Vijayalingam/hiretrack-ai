package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.AiPlacementReadiness;
import com.hiretrack.backend.dto.AiPlacementReadinessDTO;
import java.util.List;
import java.util.UUID;

public interface AiPlacementReadinessService {
    AiPlacementReadinessDTO create(AiPlacementReadinessDTO dto);
    AiPlacementReadinessDTO update(UUID id, AiPlacementReadinessDTO dto);
    AiPlacementReadinessDTO getById(UUID id);
    List<AiPlacementReadinessDTO> getAll();
    void delete(UUID id);
}
