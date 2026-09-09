package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Interview;
import com.hiretrack.backend.dto.InterviewDTO;
import java.util.List;
import java.util.UUID;

public interface InterviewService {
    InterviewDTO create(InterviewDTO dto);
    InterviewDTO update(UUID id, InterviewDTO dto);
    InterviewDTO getById(UUID id);
    List<InterviewDTO> getAll();
    void delete(UUID id);
}
