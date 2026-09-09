package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.DriveApplication;
import com.hiretrack.backend.dto.DriveApplicationDTO;
import com.hiretrack.backend.repository.DriveApplicationRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriveApplicationServiceImpl implements DriveApplicationService {
    private final DriveApplicationRepository repository;

    @Override
    public DriveApplicationDTO create(DriveApplicationDTO dto) { return null; }
    @Override
    public DriveApplicationDTO update(UUID id, DriveApplicationDTO dto) { return null; }
    @Override
    public DriveApplicationDTO getById(UUID id) { return null; }
    @Override
    public List<DriveApplicationDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
