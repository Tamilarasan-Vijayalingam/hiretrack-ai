package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Resume;
import com.hiretrack.backend.dto.ResumeDTO;
import com.hiretrack.backend.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository repository;

    @Override
    public ResumeDTO create(ResumeDTO dto) { return null; }
    @Override
    public ResumeDTO update(UUID id, ResumeDTO dto) { return null; }
    @Override
    public ResumeDTO getById(UUID id) { return null; }
    @Override
    public List<ResumeDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
