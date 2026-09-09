package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.dto.UserDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO create(UserDTO dto);
    UserDTO update(UUID id, UserDTO dto);
    UserDTO getById(UUID id);
    List<UserDTO> getAll();
    void delete(UUID id);
}
