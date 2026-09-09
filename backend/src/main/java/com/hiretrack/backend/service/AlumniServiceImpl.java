package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Alumni;
import com.hiretrack.backend.dto.AlumniDTO;
import com.hiretrack.backend.repository.AlumniRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlumniServiceImpl implements AlumniService {
    private final AlumniRepository repository;

    @Override
    public AlumniDTO create(AlumniDTO dto) { return null; }
    @Override
    public AlumniDTO update(UUID id, AlumniDTO dto) { return null; }
    @Override
    public AlumniDTO getById(UUID id) { return null; }
    @Override
    public List<AlumniDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
