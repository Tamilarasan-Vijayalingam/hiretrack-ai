package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Alumni;
import com.hiretrack.backend.dto.AlumniDTO;
import java.util.List;
import java.util.UUID;

public interface AlumniService {
    AlumniDTO create(AlumniDTO dto);
    AlumniDTO update(UUID id, AlumniDTO dto);
    AlumniDTO getById(UUID id);
    List<AlumniDTO> getAll();
    void delete(UUID id);
}
