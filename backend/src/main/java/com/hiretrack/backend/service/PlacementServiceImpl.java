package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Placement;
import com.hiretrack.backend.dto.PlacementDTO;
import com.hiretrack.backend.repository.PlacementRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlacementServiceImpl implements PlacementService {
    private final PlacementRepository repository;

    @Override
    public PlacementDTO create(PlacementDTO dto) { return null; }
    @Override
    public PlacementDTO update(UUID id, PlacementDTO dto) { return null; }
    @Override
    public PlacementDTO getById(UUID id) { return null; }
    @Override
    public List<PlacementDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
