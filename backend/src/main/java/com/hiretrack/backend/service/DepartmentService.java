package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Department;
import com.hiretrack.backend.dto.DepartmentDTO;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDTO create(DepartmentDTO dto);
    DepartmentDTO update(UUID id, DepartmentDTO dto);
    DepartmentDTO getById(UUID id);
    List<DepartmentDTO> getAll();
    void delete(UUID id);
}
