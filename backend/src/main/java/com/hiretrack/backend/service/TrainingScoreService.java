package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.TrainingScore;
import com.hiretrack.backend.dto.TrainingScoreDTO;
import java.util.List;
import java.util.UUID;

public interface TrainingScoreService {
    TrainingScoreDTO create(TrainingScoreDTO dto);
    TrainingScoreDTO update(UUID id, TrainingScoreDTO dto);
    TrainingScoreDTO getById(UUID id);
    List<TrainingScoreDTO> getAll();
    void delete(UUID id);
}
