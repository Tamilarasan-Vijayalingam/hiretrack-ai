package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Course;
import com.hiretrack.backend.dto.CourseDTO;
import com.hiretrack.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repository;

    @Override
    public CourseDTO create(CourseDTO dto) { return null; }
    @Override
    public CourseDTO update(UUID id, CourseDTO dto) { return null; }
    @Override
    public CourseDTO getById(UUID id) { return null; }
    @Override
    public List<CourseDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
