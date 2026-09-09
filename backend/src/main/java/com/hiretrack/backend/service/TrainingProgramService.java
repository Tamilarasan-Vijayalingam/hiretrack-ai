package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.TrainingProgram;
import com.hiretrack.backend.dto.TrainingProgramDTO;
import java.util.List;
import java.util.UUID;

public interface TrainingProgramService {
    TrainingProgramDTO create(TrainingProgramDTO dto);
    TrainingProgramDTO update(UUID id, TrainingProgramDTO dto);
    TrainingProgramDTO getById(UUID id);
    List<TrainingProgramDTO> getAll();
    void delete(UUID id);
}
