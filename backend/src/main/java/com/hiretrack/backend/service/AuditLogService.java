package com.hiretrack.backend.service;

import com.hiretrack.backend.entity.AuditLog;
import com.hiretrack.backend.dto.AuditLogDTO;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    AuditLogDTO create(AuditLogDTO dto);
    AuditLogDTO update(UUID id, AuditLogDTO dto);
    AuditLogDTO getById(UUID id);
    List<AuditLogDTO> getAll();
    void delete(UUID id);
}
