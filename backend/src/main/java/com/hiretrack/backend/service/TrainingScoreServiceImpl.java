package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.TrainingScore;
import com.hiretrack.backend.dto.TrainingScoreDTO;
import com.hiretrack.backend.repository.TrainingScoreRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingScoreServiceImpl implements TrainingScoreService {
    private final TrainingScoreRepository repository;

    @Override
    public TrainingScoreDTO create(TrainingScoreDTO dto) { return null; }
    @Override
    public TrainingScoreDTO update(UUID id, TrainingScoreDTO dto) { return null; }
    @Override
    public TrainingScoreDTO getById(UUID id) { return null; }
    @Override
    public List<TrainingScoreDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
