package com.hiretrack.backend.service;

import com.hiretrack.backend.dto.CompanyDTO;
import com.hiretrack.backend.dto.CompanySummaryDTO;
import com.hiretrack.backend.dto.DriveDTO;
import com.hiretrack.backend.dto.StudentEligibilityDetailDTO;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    CompanyDTO create(CompanyDTO dto);
    CompanyDTO update(UUID id, CompanyDTO dto);
    CompanyDTO getById(UUID id);
    List<CompanyDTO> getAll();
    void delete(UUID id);
    List<StudentEligibilityDetailDTO> getEligibleStudents(UUID companyId);
    CompanySummaryDTO getSummary();
    List<DriveDTO> getCompanyDrives(UUID companyId);
}
