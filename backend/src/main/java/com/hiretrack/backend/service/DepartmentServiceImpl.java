package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Department;
import com.hiretrack.backend.dto.DepartmentDTO;
import com.hiretrack.backend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    @Override
    public DepartmentDTO create(DepartmentDTO dto) { return null; }
    @Override
    public DepartmentDTO update(UUID id, DepartmentDTO dto) { return null; }
    @Override
    public DepartmentDTO getById(UUID id) { return null; }
    @Override
    public List<DepartmentDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
