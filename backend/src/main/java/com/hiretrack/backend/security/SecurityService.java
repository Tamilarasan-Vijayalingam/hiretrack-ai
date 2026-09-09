package com.hiretrack.backend.security;

import com.hiretrack.backend.entity.Role;
import com.hiretrack.backend.entity.Student;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.repository.StudentRepository;
import com.hiretrack.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public boolean canAccessStudentData(Authentication authentication, UUID studentId) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return false;
        
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.PLACEMENT_OFFICER) {
            return true;
        }

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return false;

        if (user.getRole() == Role.STUDENT) {
            return student.getUser().getId().equals(user.getId());
        }

        if (user.getRole() == Role.HOD) {
            if (user.getDepartment() != null && student.getDepartment() != null) {
                return user.getDepartment().getId().equals(student.getDepartment().getId());
            }
        }
        
        return false;
    }
}
