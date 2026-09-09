package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Drive;
import com.hiretrack.backend.dto.DriveDTO;
import java.util.List;
import java.util.UUID;

public interface DriveService {
    DriveDTO create(DriveDTO dto);
    DriveDTO update(UUID id, DriveDTO dto);
    DriveDTO getById(UUID id);
    List<DriveDTO> getAll();
    void delete(UUID id);
}
