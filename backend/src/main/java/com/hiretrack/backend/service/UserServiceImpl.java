package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.dto.UserDTO;
import com.hiretrack.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDTO create(UserDTO dto) { return null; }
    @Override
    public UserDTO update(UUID id, UserDTO dto) { return null; }
    @Override
    public UserDTO getById(UUID id) { return null; }
    @Override
    public List<UserDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
