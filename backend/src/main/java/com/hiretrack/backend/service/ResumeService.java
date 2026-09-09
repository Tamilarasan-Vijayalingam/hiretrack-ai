package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Resume;
import com.hiretrack.backend.dto.ResumeDTO;
import java.util.List;
import java.util.UUID;

public interface ResumeService {
    ResumeDTO create(ResumeDTO dto);
    ResumeDTO update(UUID id, ResumeDTO dto);
    ResumeDTO getById(UUID id);
    List<ResumeDTO> getAll();
    void delete(UUID id);
}
