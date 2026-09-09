package com.hiretrack.backend.service;

import com.hiretrack.backend.dto.DriveDTO;
import com.hiretrack.backend.entity.Company;
import com.hiretrack.backend.entity.Drive;
import com.hiretrack.backend.entity.DriveStatus;
import com.hiretrack.backend.repository.CompanyRepository;
import com.hiretrack.backend.repository.DriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DriveServiceImpl implements DriveService {
    private final DriveRepository repository;
    private final CompanyRepository companyRepository;

    @Override
    public DriveDTO create(DriveDTO dto) {
        Drive drive = toEntity(dto);
        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId()).orElse(null);
            drive.setCompany(company);
        }
        Drive saved = repository.save(drive);
        return toDTO(saved);
    }

    @Override
    public DriveDTO update(UUID id, DriveDTO dto) {
        Drive drive = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drive not found with ID: " + id));

        if (dto.getTitle() != null) drive.setTitle(dto.getTitle());
        if (dto.getJobRole() != null) drive.setJobRole(dto.getJobRole());
        if (dto.getDriveDate() != null) drive.setDriveDate(dto.getDriveDate());
        if (dto.getRegistrationDeadline() != null) drive.setRegistrationDeadline(dto.getRegistrationDeadline());
        if (dto.getTestDate() != null) drive.setTestDate(dto.getTestDate());
        if (dto.getInterviewDate() != null) drive.setInterviewDate(dto.getInterviewDate());
        if (dto.getPackageOffered() != null) drive.setPackageOffered(dto.getPackageOffered());
        if (dto.getMinCgpa() != null) drive.setMinCgpa(dto.getMinCgpa());
        if (dto.getMaxBacklogs() != null) drive.setMaxBacklogs(dto.getMaxBacklogs());
        if (dto.getEligibilityCriteria() != null) drive.setEligibilityCriteria(dto.getEligibilityCriteria());
        if (dto.getStatus() != null) {
            try {
                drive.setStatus(DriveStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (Exception ignored) {}
        }
        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId()).orElse(null);
            drive.setCompany(company);
        }

        Drive updated = repository.save(drive);
        return toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DriveDTO getById(UUID id) {
        Drive drive = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drive not found with ID: " + id));
        return toDTO(drive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriveDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public DriveDTO toDTO(Drive entity) {
        if (entity == null) return null;
        DriveDTO dto = new DriveDTO();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        dto.setTitle(entity.getTitle());
        dto.setJobRole(entity.getJobRole());
        dto.setDriveDate(entity.getDriveDate());
        dto.setRegistrationDeadline(entity.getRegistrationDeadline());
        dto.setTestDate(entity.getTestDate());
        dto.setInterviewDate(entity.getInterviewDate());
        dto.setPackageOffered(entity.getPackageOffered());
        dto.setMinCgpa(entity.getMinCgpa());
        dto.setMaxBacklogs(entity.getMaxBacklogs());
        dto.setEligibilityCriteria(entity.getEligibilityCriteria());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private Drive toEntity(DriveDTO dto) {
        Drive entity = new Drive();
        entity.setTitle(dto.getTitle() != null ? dto.getTitle() : "Campus Placement Drive");
        entity.setJobRole(dto.getJobRole() != null ? dto.getJobRole() : "Software Engineer");
        entity.setDriveDate(dto.getDriveDate() != null ? dto.getDriveDate() : java.time.LocalDate.now().plusDays(14));
        entity.setRegistrationDeadline(dto.getRegistrationDeadline() != null ? dto.getRegistrationDeadline() : java.time.LocalDate.now().plusDays(7));
        entity.setTestDate(dto.getTestDate());
        entity.setInterviewDate(dto.getInterviewDate());
        entity.setPackageOffered(dto.getPackageOffered() != null ? dto.getPackageOffered() : "6 LPA");
        entity.setMinCgpa(dto.getMinCgpa());
        entity.setMaxBacklogs(dto.getMaxBacklogs() != null ? dto.getMaxBacklogs() : 0);
        entity.setEligibilityCriteria(dto.getEligibilityCriteria());
        if (dto.getStatus() != null) {
            try {
                entity.setStatus(DriveStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (Exception e) {
                entity.setStatus(DriveStatus.UPCOMING);
            }
        } else {
            entity.setStatus(DriveStatus.UPCOMING);
        }
        return entity;
    }
}
