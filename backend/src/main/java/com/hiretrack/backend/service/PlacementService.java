package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Placement;
import com.hiretrack.backend.dto.PlacementDTO;
import java.util.List;
import java.util.UUID;

public interface PlacementService {
    PlacementDTO create(PlacementDTO dto);
    PlacementDTO update(UUID id, PlacementDTO dto);
    PlacementDTO getById(UUID id);
    List<PlacementDTO> getAll();
    void delete(UUID id);
}
