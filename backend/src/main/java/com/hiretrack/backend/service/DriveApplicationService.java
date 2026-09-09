package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.DriveApplication;
import com.hiretrack.backend.dto.DriveApplicationDTO;
import java.util.List;
import java.util.UUID;

public interface DriveApplicationService {
    DriveApplicationDTO create(DriveApplicationDTO dto);
    DriveApplicationDTO update(UUID id, DriveApplicationDTO dto);
    DriveApplicationDTO getById(UUID id);
    List<DriveApplicationDTO> getAll();
    void delete(UUID id);
}
