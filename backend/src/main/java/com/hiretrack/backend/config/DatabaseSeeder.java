package com.hiretrack.backend.config;

import com.hiretrack.backend.entity.*;
import com.hiretrack.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final DriveRepository driveRepository;
    private final PlacementRepository placementRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final TrainingScoreRepository trainingScoreRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking database seed status...");
        Random random = new Random();
        String defaultPassword = passwordEncoder.encode("password");

        // 1. Create Departments
        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            String[] deptNames = {"Computer Science", "Information Technology", "Electronics and Communication", "Mechanical", "Electrical"};
            String[] deptCodes = {"CSE", "IT", "ECE", "MECH", "EEE"};
            for (int i = 0; i < deptNames.length; i++) {
                Department dept = new Department();
                dept.setName(deptNames[i]);
                dept.setCode(deptCodes[i]);
                departments.add(departmentRepository.save(dept));
            }
        }

        // 2. Create Core Users (Admin, PO, HOD)
        createUser("System Admin", "admin@hiretrack.com", defaultPassword, Role.ADMIN, null);
        createUser("Placement Officer", "po@hiretrack.com", defaultPassword, Role.PLACEMENT_OFFICER, null);
        createUser("HOD Computer Science", "hod.cs@hiretrack.com", defaultPassword, Role.HOD, departments.get(0));

        // 3. Create or Enrich Companies (Realistic industry leaders)
        seedOrEnrichCompanies();

        // 4. Create Students (if not enough)
        if (studentRepository.count() < 30) {
            List<Student> students = new ArrayList<>();
            String[] firstNames = {"Aarav", "Vivaan", "Aditya", "Vihaan", "Arjun", "Sai", "Reyansh", "Ayaan", "Krishna", "Ishaan", "Shaurya", "Atharv", "Ananya", "Diya", "Navya", "Kavya", "Isha", "Riya", "Aarohi", "Avni", "Sara", "Myra", "Sana", "Meera", "Aisha", "Priya", "Rahul", "Neha", "Rohan", "Sneha"};
            String[] lastNames = {"Sharma", "Verma", "Patil", "Desai", "Joshi", "Singh", "Kumar", "Gupta", "Nair", "Reddy", "Rao", "Menon"};
            
            for (int i = 0; i < 30; i++) {
                Department dept = departments.get(random.nextInt(departments.size()));
                
                String studentName = firstNames[i] + " " + lastNames[random.nextInt(lastNames.length)];
                User user = new User();
                String email = "student" + (i + 1) + "@hiretrack.com";
                user.setEmail(email);
                user.setFullName(studentName);
                user.setPassword(defaultPassword);
                user.setRole(Role.STUDENT);
                user.setDepartment(dept);
                user.setIsActive(true);
                user = userRepository.save(user);

                Student student = new Student();
                student.setUser(user);
                student.setName(studentName);
                student.setRegisterNumber("REG2024" + String.format("%03d", i + 1));
                student.setEmail(email);
                student.setPhone("98765" + String.format("%05d", i));
                student.setDepartment(dept);
                
                // Generate CGPA between 6.0 and 9.8
                double cgpa = 6.0 + (3.8 * random.nextDouble());
                student.setCgpa(BigDecimal.valueOf(Math.round(cgpa * 100.0) / 100.0));
                student.setBacklogs(random.nextInt(3)); // 0 to 2 backlogs
                student.setGraduationYear(2024);
                student.setBatchYear(2024);
                student.setProfileCompletion(70 + random.nextInt(31)); // 70 to 100
                
                // Skills & Projects
                student.setCertifications("AWS Certified Cloud Practitioner, Oracle Certified Java Associate");
                student.setProjects("Full Stack E-commerce Web Application using React and Spring Boot, Real-time Chat App using WebSockets and Redis");
                
                students.add(studentRepository.save(student));
            }

            // 5. Create Drives for companies
            List<Company> allCompanies = companyRepository.findAll();
            for (int i = 0; i < Math.min(5, allCompanies.size()); i++) {
                Company c = allCompanies.get(i);
                Drive drive = new Drive();
                drive.setCompany(c);
                drive.setTitle(c.getName() + " Campus Recruitment 2024");
                drive.setJobRole(c.getJobRole() != null ? c.getJobRole() : "Software Engineer");
                
                if (i < 2) {
                    drive.setDriveDate(LocalDate.now().minusDays(20 - (i * 10)));
                    drive.setRegistrationDeadline(drive.getDriveDate().minusDays(5));
                    drive.setStatus(DriveStatus.COMPLETED);
                } else {
                    drive.setDriveDate(LocalDate.now().plusDays((i * 8)));
                    drive.setRegistrationDeadline(drive.getDriveDate().minusDays(4));
                    drive.setStatus(DriveStatus.REGISTRATION_OPEN);
                }
                
                drive.setPackageOffered(c.getPackageCtc() != null ? c.getPackageCtc() : "7.5 LPA");
                drive.setMinCgpa(c.getMinCgpa() != null ? c.getMinCgpa() : BigDecimal.valueOf(7.0));
                drive.setMaxBacklogs(c.getMaxBacklogs() != null ? c.getMaxBacklogs() : 0);
                driveRepository.save(drive);
            }

            // 6. Create Placements
            List<Drive> allDrives = driveRepository.findAll();
            for (Drive drive : allDrives) {
                if (drive.getStatus() == DriveStatus.COMPLETED) {
                    int placementsForDrive = 3 + random.nextInt(3);
                    for (int j = 0; j < placementsForDrive; j++) {
                        Student placedStudent = students.get(random.nextInt(students.size()));
                        if (placementRepository.findAll().stream().anyMatch(p -> p.getStudent().getId().equals(placedStudent.getId()))) {
                            continue;
                        }
                        
                        Placement placement = new Placement();
                        placement.setStudent(placedStudent);
                        placement.setCompany(drive.getCompany());
                        placement.setDrive(drive);
                        placement.setDepartment(placedStudent.getDepartment());
                        placement.setBatch(2024);
                        placement.setJobRole(drive.getJobRole());
                        placement.setPlacementDate(drive.getDriveDate().plusDays(2));
                        placement.setOfferDate(drive.getDriveDate().plusDays(2));
                        
                        String pkgStr = drive.getPackageOffered().replaceAll("[^0-9.]", "");
                        BigDecimal pkgVal = pkgStr.isEmpty() ? BigDecimal.valueOf(6.5) : new BigDecimal(pkgStr);
                        placement.setPackageOffered(pkgVal);
                        placement.setPackageLpa(pkgVal);
                        
                        placementRepository.save(placement);
                    }
                }
            }

            // 7. Training Scores
            TrainingProgram program = new TrainingProgram();
            program.setTitle("Pre-Placement Training 2024");
            program.setDescription("Intensive aptitude, coding, and interview prep.");
            program.setStartDate(LocalDate.now().minusMonths(2));
            program.setEndDate(LocalDate.now().minusMonths(1));
            program = trainingProgramRepository.save(program);

            for (Student student : students) {
                TrainingScore score = new TrainingScore();
                score.setStudent(student);
                score.setTrainingProgram(program);
                
                int bApt = 45 + random.nextInt(25);
                score.setBeforeAptitude(bApt);
                score.setAfterAptitude(bApt + 15 + random.nextInt(15));
                
                int bCod = 35 + random.nextInt(30);
                score.setBeforeCoding(bCod);
                score.setAfterCoding(bCod + 20 + random.nextInt(15));
                
                int bInt = 50 + random.nextInt(20);
                score.setBeforeInterview(bInt);
                score.setAfterInterview(bInt + 15 + random.nextInt(15));
                
                trainingScoreRepository.save(score);
            }
        }

        log.info("Database checking and seeding completed successfully.");
    }

    private void seedOrEnrichCompanies() {
        record CompanySeedData(String name, String industry, String location, String website, String hrName, String hrEmail, String hrPhone, String jobRole, String pkg, double cgpa, int backlogs, String skills, String certs, int openings, String status, int daysToDrive, String selection) {}

        List<CompanySeedData> seedList = List.of(
            new CompanySeedData("Zoho Corporation", "Software Product", "Chennai, Tamil Nadu", "https://www.zoho.com", "Suresh Kumar", "recruitment@zohocorp.com", "+91 98401 23456", "Software Developer", "8.5 LPA", 7.0, 0, "Java, C++, Data Structures, SQL, REST APIs", "Oracle Certified Associate", 45, "ACTIVE", 12, "Online Aptitude & Coding -> Advanced Programming Round -> Technical Interview -> HR Round"),
            new CompanySeedData("Tata Consultancy Services", "IT Services & Consulting", "Mumbai, Maharashtra", "https://www.tcs.com", "Priya Sharma", "campus.tcs@tcs.com", "+91 98200 67890", "Digital Software Engineer", "7.0 LPA", 6.5, 1, "Java, Python, SQL, Web Technologies, Git", "Any cloud or coding certificate", 120, "ACTIVE", 18, "TCS NQT Test -> Technical Interview -> Managerial Round -> HR Interview"),
            new CompanySeedData("Infosys", "IT Services & Consulting", "Bengaluru, Karnataka", "https://www.infosys.com", "Anand Verma", "campus.hires@infosys.com", "+91 98110 54321", "Specialist Programmer", "9.5 LPA", 7.5, 0, "Java, Spring Boot, React, Python, Cloud Basics", "AWS Cloud Practitioner, Azure Fundamentals", 60, "ACTIVE", 25, "HackWithInfy / Coding Assessment -> Technical Deep Dive -> HR Discussion"),
            new CompanySeedData("Accenture", "Management & Technology Consulting", "Bengaluru, Karnataka", "https://www.accenture.com", "Meera Nair", "campus.accenture@accenture.com", "+91 98860 11223", "Associate Software Engineer", "6.5 LPA", 6.0, 1, "Java, SQL, Agile Methodologies, JavaScript", "Scrum Master, Java Essentials", 80, "ACTIVE", 30, "Cognitive & Technical Assessment -> Coding Test -> Communication Assessment -> Interview"),
            new CompanySeedData("Amazon Web Services", "Cloud & Internet Technology", "Hyderabad, Telangana", "https://aws.amazon.com", "Vikram Rathore", "aws-campus@amazon.com", "+91 99490 99887", "Cloud Support Associate", "14.0 LPA", 7.8, 0, "Linux, Networking, Python, AWS Cloud, Troubleshooting", "AWS Solutions Architect Associate", 25, "UPCOMING", 40, "Online Assessment (Debugging & Coding) -> System & Networking Interview -> Behavioral Interview"),
            new CompanySeedData("Microsoft", "Technology & Software", "Hyderabad, Telangana", "https://www.microsoft.com", "Deepak Roy", "campus-india@microsoft.com", "+91 97170 33445", "Software Engineer", "18.0 LPA", 8.0, 0, "Algorithms, Data Structures, C++, Java, System Design", "Azure Developer Associate", 15, "UPCOMING", 45, "Online Coding Round -> 3 Rounds of Technical Interviews -> AA (As Appropriate) Round"),
            new CompanySeedData("Wipro Technologies", "IT Services & Consulting", "Bengaluru, Karnataka", "https://www.wipro.com", "Sneha Rao", "campus@wipro.com", "+91 98450 77665", "Project Engineer", "6.0 LPA", 6.0, 1, "Java, C#, SQL, HTML5, CSS3", "Python / Java Certificate", 70, "ACTIVE", 15, "Online Assessment -> Technical Interview -> HR Round")
        );

        for (CompanySeedData seed : seedList) {
            Company company = companyRepository.findByNameIgnoreCase(seed.name()).orElse(null);
            if (company == null) {
                company = new Company();
                company.setName(seed.name());
            }

            // Populate / update fields
            company.setIndustry(seed.industry());
            company.setLocation(seed.location());
            company.setWebsite(seed.website());
            company.setHrName(seed.hrName());
            company.setHrEmail(seed.hrEmail());
            company.setHrPhone(seed.hrPhone());
            company.setJobRole(seed.jobRole());
            company.setPackageCtc(seed.pkg());
            company.setMinCgpa(BigDecimal.valueOf(seed.cgpa()));
            company.setMaxBacklogs(seed.backlogs());
            company.setRequiredSkills(seed.skills());
            company.setRequiredCertifications(seed.certs());
            company.setOpenings(seed.openings());
            company.setHiringStatus(seed.status());
            company.setDriveDate(LocalDate.now().plusDays(seed.daysToDrive()));
            company.setRegistrationDeadline(company.getDriveDate().minusDays(4));
            company.setSelectionProcess(seed.selection());
            company.setDescription(seed.name() + " is a premier recruiting partner offering high-growth engineering and technology roles.");
            company.setHiringCriteria("Min CGPA " + seed.cgpa() + ", Max Backlogs: " + seed.backlogs() + ", Skills: " + seed.skills());

            Company saved = companyRepository.save(company);

            // Ensure drive exists
            if (driveRepository.findByCompanyId(saved.getId()).isEmpty()) {
                Drive drive = new Drive();
                drive.setCompany(saved);
                drive.setTitle(saved.getName() + " Campus Placement Drive");
                drive.setJobRole(saved.getJobRole());
                drive.setDriveDate(saved.getDriveDate());
                drive.setRegistrationDeadline(saved.getRegistrationDeadline());
                drive.setPackageOffered(saved.getPackageCtc());
                drive.setMinCgpa(saved.getMinCgpa());
                drive.setMaxBacklogs(saved.getMaxBacklogs());
                drive.setEligibilityCriteria(saved.getHiringCriteria());
                drive.setStatus(DriveStatus.UPCOMING);
                driveRepository.save(drive);
            }
        }
    }

    private void createUser(String fullName, String email, String password, Role role, Department department) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setDepartment(department);
        user.setIsActive(true);
        userRepository.save(user);
    }
}
