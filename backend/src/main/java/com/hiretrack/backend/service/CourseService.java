package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.Course;
import com.hiretrack.backend.dto.CourseDTO;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseDTO create(CourseDTO dto);
    CourseDTO update(UUID id, CourseDTO dto);
    CourseDTO getById(UUID id);
    List<CourseDTO> getAll();
    void delete(UUID id);
}
