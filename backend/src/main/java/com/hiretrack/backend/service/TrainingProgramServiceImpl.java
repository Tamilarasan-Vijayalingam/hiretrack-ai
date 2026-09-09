package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.TrainingProgram;
import com.hiretrack.backend.dto.TrainingProgramDTO;
import com.hiretrack.backend.repository.TrainingProgramRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository repository;

    @Override
    public TrainingProgramDTO create(TrainingProgramDTO dto) { return null; }
    @Override
    public TrainingProgramDTO update(UUID id, TrainingProgramDTO dto) { return null; }
    @Override
    public TrainingProgramDTO getById(UUID id) { return null; }
    @Override
    public List<TrainingProgramDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
