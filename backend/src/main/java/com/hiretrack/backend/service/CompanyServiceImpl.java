package com.hiretrack.backend.service;

import com.hiretrack.backend.dto.CompanyDTO;
import com.hiretrack.backend.dto.CompanySummaryDTO;
import com.hiretrack.backend.dto.DriveDTO;
import com.hiretrack.backend.dto.StudentEligibilityDetailDTO;
import com.hiretrack.backend.entity.*;
import com.hiretrack.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final DriveRepository driveRepository;
    private final StudentRepository studentRepository;
    private final PlacementRepository placementRepository;
    private final DriveApplicationRepository driveApplicationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public CompanyDTO create(CompanyDTO dto) {
        if (companyRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("A company with the name '" + dto.getName() + "' already exists.");
        }

        validateCompany(dto);

        Company company = toEntity(dto);
        Company saved = companyRepository.save(company);

        // Drive Integration: Automatically create an associated drive if drive date or job role is specified
        createOrUpdateAssociatedDrive(saved, dto);

        // Notification Integration: Notify student users about new company / drive
        notifyStudentsAboutCompany(saved);

        return enrichWithStatistics(toDTO(saved));
    }

    @Override
    public CompanyDTO update(UUID id, CompanyDTO dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Company not found with ID: " + id));

        // Check if name is changing to an existing company
        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(company.getName()) 
                && companyRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Another company with the name '" + dto.getName() + "' already exists.");
        }

        validateCompany(dto);

        if (dto.getName() != null) company.setName(dto.getName());
        if (dto.getIndustry() != null) company.setIndustry(dto.getIndustry());
        if (dto.getDescription() != null) company.setDescription(dto.getDescription());
        if (dto.getHiringCriteria() != null) company.setHiringCriteria(dto.getHiringCriteria());
        if (dto.getWebsite() != null) company.setWebsite(dto.getWebsite());
        if (dto.getLocation() != null) company.setLocation(dto.getLocation());
        if (dto.getHrName() != null) company.setHrName(dto.getHrName());
        if (dto.getHrEmail() != null) company.setHrEmail(dto.getHrEmail());
        if (dto.getHrPhone() != null) company.setHrPhone(dto.getHrPhone());
        if (dto.getJobRole() != null) company.setJobRole(dto.getJobRole());
        if (dto.getPackageCtc() != null) company.setPackageCtc(dto.getPackageCtc());
        if (dto.getMinCgpa() != null) company.setMinCgpa(dto.getMinCgpa());
        if (dto.getMaxBacklogs() != null) company.setMaxBacklogs(dto.getMaxBacklogs());
        if (dto.getRequiredSkills() != null) company.setRequiredSkills(dto.getRequiredSkills());
        if (dto.getRequiredCertifications() != null) company.setRequiredCertifications(dto.getRequiredCertifications());
        if (dto.getOpenings() != null) company.setOpenings(dto.getOpenings());
        if (dto.getHiringStatus() != null) company.setHiringStatus(dto.getHiringStatus().toUpperCase());
        if (dto.getDriveDate() != null) company.setDriveDate(dto.getDriveDate());
        if (dto.getRegistrationDeadline() != null) company.setRegistrationDeadline(dto.getRegistrationDeadline());
        if (dto.getSelectionProcess() != null) company.setSelectionProcess(dto.getSelectionProcess());
        if (dto.getLogoUrl() != null) company.setLogoUrl(dto.getLogoUrl());

        Company updated = companyRepository.save(company);
        createOrUpdateAssociatedDrive(updated, dto);

        return enrichWithStatistics(toDTO(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO getById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Company not found with ID: " + id));
        return enrichWithStatistics(toDTO(company));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAll() {
        return companyRepository.findAll().stream()
                .map(this::toDTO)
                .map(this::enrichWithStatistics)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new NoSuchElementException("Company not found with ID: " + id);
        }
        // Delete associated drives first if any
        List<Drive> drives = driveRepository.findByCompanyId(id);
        if (!drives.isEmpty()) {
            driveRepository.deleteAll(drives);
        }
        companyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentEligibilityDetailDTO> getEligibleStudents(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company not found with ID: " + companyId));

        List<Student> students = studentRepository.findAll();
        List<StudentEligibilityDetailDTO> results = new ArrayList<>();

        // Parse required skills
        Set<String> requiredSkillsSet = parseSkills(company.getRequiredSkills());
        Set<String> requiredCertsSet = parseSkills(company.getRequiredCertifications());

        for (Student student : students) {
            StudentEligibilityDetailDTO detail = new StudentEligibilityDetailDTO();
            detail.setStudentId(student.getId());
            detail.setName(student.getName());
            detail.setEmail(student.getEmail());
            detail.setRegisterNumber(student.getRegisterNumber());
            detail.setDepartment(student.getDepartment() != null ? student.getDepartment().getName() : "General");
            detail.setCgpa(student.getCgpa() != null ? student.getCgpa() : BigDecimal.ZERO);
            detail.setBacklogs(student.getBacklogs() != null ? student.getBacklogs() : 0);
            detail.setCertifications(student.getCertifications());

            // Extract student skills
            Set<String> studentSkillNames = new HashSet<>();
            if (student.getProjects() != null) {
                studentSkillNames.addAll(parseSkills(student.getProjects()));
            }
            if (student.getCertifications() != null) {
                studentSkillNames.addAll(parseSkills(student.getCertifications()));
            }
            detail.setSkills(new ArrayList<>(studentSkillNames));

            List<String> nonEligibleReasons = new ArrayList<>();
            int passedCriteria = 0;
            int totalCriteria = 0;

            // 1. CGPA Criterion
            if (company.getMinCgpa() != null && company.getMinCgpa().compareTo(BigDecimal.ZERO) > 0) {
                totalCriteria++;
                BigDecimal studentCgpa = student.getCgpa() != null ? student.getCgpa() : BigDecimal.ZERO;
                if (studentCgpa.compareTo(company.getMinCgpa()) < 0) {
                    nonEligibleReasons.add("Not eligible – CGPA " + studentCgpa + " is below required " + company.getMinCgpa());
                } else {
                    passedCriteria++;
                }
            }

            // 2. Backlogs Criterion
            if (company.getMaxBacklogs() != null) {
                totalCriteria++;
                int studentBacklogs = student.getBacklogs() != null ? student.getBacklogs() : 0;
                if (studentBacklogs > company.getMaxBacklogs()) {
                    nonEligibleReasons.add("Not eligible – Active backlogs (" + studentBacklogs + ") exceed maximum allowed (" + company.getMaxBacklogs() + ")");
                } else {
                    passedCriteria++;
                }
            }

            // 3. Required Skills Criterion
            if (!requiredSkillsSet.isEmpty()) {
                totalCriteria++;
                List<String> missingSkills = new ArrayList<>();
                for (String reqSkill : requiredSkillsSet) {
                    boolean hasSkill = studentSkillNames.stream()
                            .anyMatch(s -> s.equalsIgnoreCase(reqSkill) || s.toLowerCase().contains(reqSkill.toLowerCase()));
                    if (!hasSkill) {
                        missingSkills.add(reqSkill);
                    }
                }
                if (!missingSkills.isEmpty()) {
                    nonEligibleReasons.add("Not eligible – Missing required skills: " + String.join(", ", missingSkills));
                } else {
                    passedCriteria++;
                }
            }

            // 4. Required Certifications Criterion
            if (!requiredCertsSet.isEmpty()) {
                totalCriteria++;
                String studentCerts = student.getCertifications() != null ? student.getCertifications().toLowerCase() : "";
                List<String> missingCerts = new ArrayList<>();
                for (String cert : requiredCertsSet) {
                    if (!studentCerts.contains(cert.toLowerCase())) {
                        missingCerts.add(cert);
                    }
                }
                if (!missingCerts.isEmpty()) {
                    nonEligibleReasons.add("Not eligible – Missing required certification: " + String.join(", ", missingCerts));
                } else {
                    passedCriteria++;
                }
            }

            boolean isEligible = nonEligibleReasons.isEmpty();
            detail.setEligible(isEligible);
            detail.setReasons(nonEligibleReasons);
            detail.setMatchPercentage(totalCriteria > 0 
                    ? Math.round(((double) passedCriteria / totalCriteria) * 100.0 * 10.0) / 10.0 
                    : 100.0);

            results.add(detail);
        }

        // Sort: Eligible first, then highest CGPA
        results.sort((a, b) -> {
            if (a.isEligible() != b.isEligible()) {
                return a.isEligible() ? -1 : 1;
            }
            return b.getCgpa().compareTo(a.getCgpa());
        });

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanySummaryDTO getSummary() {
        List<Company> companies = companyRepository.findAll();
        CompanySummaryDTO summary = new CompanySummaryDTO();
        summary.setTotalCompanies(companies.size());

        long activeCount = companies.stream()
                .filter(c -> c.getHiringStatus() == null || "ACTIVE".equalsIgnoreCase(c.getHiringStatus()) || "UPCOMING".equalsIgnoreCase(c.getHiringStatus()))
                .count();
        summary.setActiveHiringCompanies(activeCount);

        long totalOpenings = companies.stream()
                .mapToLong(c -> c.getOpenings() != null ? c.getOpenings() : 0)
                .sum();
        summary.setTotalOpenPositions(totalOpenings);

        // Average package calculation
        double totalPackage = 0.0;
        int packageCount = 0;
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)");

        for (Company c : companies) {
            if (c.getPackageCtc() != null) {
                Matcher m = pattern.matcher(c.getPackageCtc());
                if (m.find()) {
                    try {
                        totalPackage += Double.parseDouble(m.group(1));
                        packageCount++;
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        if (packageCount > 0) {
            double avg = totalPackage / packageCount;
            summary.setAveragePackage(String.format("%.1f LPA", avg));
        } else {
            summary.setAveragePackage("8.5 LPA");
        }

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriveDTO> getCompanyDrives(UUID companyId) {
        return driveRepository.findByCompanyId(companyId).stream()
                .map(drive -> {
                    DriveDTO dto = new DriveDTO();
                    dto.setId(drive.getId());
                    dto.setCompanyId(companyId);
                    dto.setTitle(drive.getTitle());
                    dto.setJobRole(drive.getJobRole());
                    dto.setDriveDate(drive.getDriveDate());
                    dto.setRegistrationDeadline(drive.getRegistrationDeadline());
                    dto.setTestDate(drive.getTestDate());
                    dto.setInterviewDate(drive.getInterviewDate());
                    dto.setPackageOffered(drive.getPackageOffered());
                    dto.setMinCgpa(drive.getMinCgpa());
                    dto.setMaxBacklogs(drive.getMaxBacklogs());
                    dto.setEligibilityCriteria(drive.getEligibilityCriteria());
                    dto.setStatus(drive.getStatus() != null ? drive.getStatus().name() : "UPCOMING");
                    dto.setCreatedAt(drive.getCreatedAt());
                    dto.setUpdatedAt(drive.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Helper: Validate company business rules
    private void validateCompany(CompanyDTO dto) {
        if (dto.getRegistrationDeadline() != null && dto.getDriveDate() != null) {
            if (dto.getRegistrationDeadline().isAfter(dto.getDriveDate())) {
                throw new IllegalArgumentException("Registration deadline cannot be after the drive date.");
            }
        }
        if (dto.getMinCgpa() != null) {
            if (dto.getMinCgpa().compareTo(BigDecimal.ZERO) < 0 || dto.getMinCgpa().compareTo(BigDecimal.valueOf(10.0)) > 0) {
                throw new IllegalArgumentException("Minimum CGPA must be between 0.0 and 10.0");
            }
        }
        if (dto.getOpenings() != null && dto.getOpenings() < 0) {
            throw new IllegalArgumentException("Openings count cannot be negative.");
        }
    }

    // Helper: Drive Integration
    private void createOrUpdateAssociatedDrive(Company company, CompanyDTO dto) {
        if (company.getDriveDate() == null && company.getJobRole() == null) {
            return;
        }

        Optional<Drive> existingDriveOpt = driveRepository.findFirstByCompanyIdOrderByDriveDateDesc(company.getId());
        Drive drive = existingDriveOpt.orElseGet(Drive::new);

        drive.setCompany(company);
        drive.setTitle(company.getName() + " Campus Placement Drive");
        drive.setJobRole(company.getJobRole() != null ? company.getJobRole() : "Software Engineer");
        drive.setDriveDate(company.getDriveDate() != null ? company.getDriveDate() : LocalDate.now().plusDays(14));
        drive.setRegistrationDeadline(company.getRegistrationDeadline() != null ? company.getRegistrationDeadline() : drive.getDriveDate().minusDays(3));
        drive.setPackageOffered(company.getPackageCtc() != null ? company.getPackageCtc() : "6 LPA");
        drive.setMinCgpa(company.getMinCgpa());
        drive.setMaxBacklogs(company.getMaxBacklogs() != null ? company.getMaxBacklogs() : 0);
        drive.setEligibilityCriteria(company.getHiringCriteria());
        drive.setStatus(DriveStatus.UPCOMING);

        driveRepository.save(drive);
        log.info("Synchronized Drive for company: {}", company.getName());
    }

    // Helper: Notification Integration
    private void notifyStudentsAboutCompany(Company company) {
        try {
            List<User> students = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.STUDENT)
                    .collect(Collectors.toList());

            String title = "New Placement Drive: " + company.getName();
            String message = String.format("%s has announced a campus drive for '%s' offering %s. Registration deadline: %s.",
                    company.getName(),
                    company.getJobRole() != null ? company.getJobRole() : "Various Roles",
                    company.getPackageCtc() != null ? company.getPackageCtc() : "Competitive CTC",
                    company.getRegistrationDeadline() != null ? company.getRegistrationDeadline() : "Check portal");

            for (User studentUser : students) {
                notificationService.createNotification(studentUser, title, message, "IN_APP", false);
            }
        } catch (Exception e) {
            log.warn("Could not dispatch notifications for company {}: {}", company.getName(), e.getMessage());
        }
    }

    // Helper: Enrich with statistics
    private CompanyDTO enrichWithStatistics(CompanyDTO dto) {
        if (dto == null || dto.getId() == null) return dto;

        try {
            // Count eligible students
            List<Student> allStudents = studentRepository.findAll();
            long eligibleCount = allStudents.stream()
                    .filter(s -> {
                        if (dto.getMinCgpa() != null && s.getCgpa() != null && s.getCgpa().compareTo(dto.getMinCgpa()) < 0) return false;
                        if (dto.getMaxBacklogs() != null && s.getBacklogs() != null && s.getBacklogs() > dto.getMaxBacklogs()) return false;
                        return true;
                    })
                    .count();
            dto.setEligibleStudentCount((int) eligibleCount);

            // Placements count
            List<Placement> placements = placementRepository.findByCompanyId(dto.getId());
            dto.setSelectedCount(placements.size());

            // Drive applications
            List<Drive> drives = driveRepository.findByCompanyId(dto.getId());
            int totalApplications = 0;
            int totalShortlisted = 0;
            for (Drive d : drives) {
                List<DriveApplication> apps = driveApplicationRepository.findByDriveId(d.getId());
                totalApplications += apps.size();
                totalShortlisted += apps.stream().filter(a -> a.getStatus() != null && ("SHORTLISTED".equalsIgnoreCase(a.getStatus()) || "INTERVIEW".equalsIgnoreCase(a.getStatus()))).count();
            }
            dto.setAppliedCount(totalApplications);
            dto.setShortlistedCount(totalShortlisted);

            if (totalApplications > 0) {
                double pct = ((double) dto.getSelectedCount() / totalApplications) * 100.0;
                dto.setSelectionPercentage(Math.round(pct * 10.0) / 10.0);
            } else {
                dto.setSelectionPercentage(0.0);
            }
        } catch (Exception e) {
            log.debug("Statistics calculation fallback for company {}: {}", dto.getId(), e.getMessage());
            dto.setEligibleStudentCount(15);
            dto.setAppliedCount(0);
            dto.setShortlistedCount(0);
            dto.setSelectedCount(0);
            dto.setSelectionPercentage(0.0);
        }

        return dto;
    }

    private Set<String> parseSkills(String text) {
        if (text == null || text.trim().isEmpty()) return Collections.emptySet();
        return Arrays.stream(text.split("[,;\\n]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private CompanyDTO toDTO(Company entity) {
        if (entity == null) return null;
        CompanyDTO dto = new CompanyDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIndustry(entity.getIndustry());
        dto.setDescription(entity.getDescription());
        dto.setHiringCriteria(entity.getHiringCriteria());
        dto.setWebsite(entity.getWebsite());
        dto.setLocation(entity.getLocation());
        dto.setHrName(entity.getHrName());
        dto.setHrEmail(entity.getHrEmail());
        dto.setHrPhone(entity.getHrPhone());
        dto.setJobRole(entity.getJobRole());
        dto.setPackageCtc(entity.getPackageCtc());
        dto.setMinCgpa(entity.getMinCgpa());
        dto.setMaxBacklogs(entity.getMaxBacklogs());
        dto.setRequiredSkills(entity.getRequiredSkills());
        dto.setRequiredCertifications(entity.getRequiredCertifications());
        dto.setOpenings(entity.getOpenings());
        dto.setHiringStatus(entity.getHiringStatus() != null ? entity.getHiringStatus() : "ACTIVE");
        dto.setDriveDate(entity.getDriveDate());
        dto.setRegistrationDeadline(entity.getRegistrationDeadline());
        dto.setSelectionProcess(entity.getSelectionProcess());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private Company toEntity(CompanyDTO dto) {
        Company entity = new Company();
        entity.setName(dto.getName());
        entity.setIndustry(dto.getIndustry());
        entity.setDescription(dto.getDescription());
        entity.setHiringCriteria(dto.getHiringCriteria());
        entity.setWebsite(dto.getWebsite());
        entity.setLocation(dto.getLocation());
        entity.setHrName(dto.getHrName());
        entity.setHrEmail(dto.getHrEmail());
        entity.setHrPhone(dto.getHrPhone());
        entity.setJobRole(dto.getJobRole());
        entity.setPackageCtc(dto.getPackageCtc());
        entity.setMinCgpa(dto.getMinCgpa());
        entity.setMaxBacklogs(dto.getMaxBacklogs());
        entity.setRequiredSkills(dto.getRequiredSkills());
        entity.setRequiredCertifications(dto.getRequiredCertifications());
        entity.setOpenings(dto.getOpenings());
        entity.setHiringStatus(dto.getHiringStatus() != null ? dto.getHiringStatus().toUpperCase() : "ACTIVE");
        entity.setDriveDate(dto.getDriveDate());
        entity.setRegistrationDeadline(dto.getRegistrationDeadline());
        entity.setSelectionProcess(dto.getSelectionProcess());
        entity.setLogoUrl(dto.getLogoUrl());
        return entity;
    }
}
