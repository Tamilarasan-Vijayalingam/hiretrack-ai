package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.AuditLog;
import com.hiretrack.backend.dto.AuditLogDTO;
import com.hiretrack.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository repository;

    @Override
    public AuditLogDTO create(AuditLogDTO dto) { return null; }
    @Override
    public AuditLogDTO update(UUID id, AuditLogDTO dto) { return null; }
    @Override
    public AuditLogDTO getById(UUID id) { return null; }
    @Override
    public List<AuditLogDTO> getAll() { return List.of(); }
    @Override
    public void delete(UUID id) {}
}
