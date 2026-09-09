package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Student;
import com.hiretrack.backend.dto.StudentDTO;
import com.hiretrack.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;

    @Override
    public StudentDTO create(StudentDTO dto) { return null; }
    @Override
    public StudentDTO update(UUID id, StudentDTO dto) { return null; }
    @Override
    public StudentDTO getById(UUID id) { return null; }
    @Override
    public List<StudentDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
