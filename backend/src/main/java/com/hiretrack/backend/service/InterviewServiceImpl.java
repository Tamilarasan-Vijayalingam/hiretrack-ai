package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Interview;
import com.hiretrack.backend.dto.InterviewDTO;
import com.hiretrack.backend.repository.InterviewRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository repository;

    @Override
    public InterviewDTO create(InterviewDTO dto) { return null; }
    @Override
    public InterviewDTO update(UUID id, InterviewDTO dto) { return null; }
    @Override
    public InterviewDTO getById(UUID id) { return null; }
    @Override
    public List<InterviewDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
