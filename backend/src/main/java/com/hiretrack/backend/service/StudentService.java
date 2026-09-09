package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Student;
import com.hiretrack.backend.dto.StudentDTO;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentDTO create(StudentDTO dto);
    StudentDTO update(UUID id, StudentDTO dto);
    StudentDTO getById(UUID id);
    List<StudentDTO> getAll();
    void delete(UUID id);
}
