import os
import re

base_pkg = "com.hiretrack.backend"
base_dir = "src/main/java/com/hiretrack/backend"

entities = {
    "User": {
        "table": "users",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "email", "type": "String", "annotation": "@Column(unique = true, nullable = false)"},
            {"name": "password", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "role", "type": "Role", "annotation": "@Enumerated(EnumType.STRING) @Column(nullable = false)"},
            {"name": "isActive", "type": "Boolean", "annotation": "@Column(name = \"is_active\")"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp @Column(name = \"created_at\", updatable = false)"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp @Column(name = \"updated_at\")"}
        ]
    },
    "Department": {
        "table": "departments",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "name", "type": "String", "annotation": "@Column(unique = true, nullable = false)"},
            {"name": "code", "type": "String", "annotation": "@Column(unique = true, nullable = false)"}
        ]
    },
    "Student": {
        "table": "students",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "user", "type": "User", "annotation": "@OneToOne @JoinColumn(name = \"user_id\", unique = true)"},
            {"name": "name", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "registerNumber", "type": "String", "annotation": "@Column(name = \"register_number\", unique = true, nullable = false)"},
            {"name": "email", "type": "String", "annotation": "@Column(unique = true, nullable = false)"},
            {"name": "phone", "type": "String", "annotation": ""},
            {"name": "department", "type": "Department", "annotation": "@ManyToOne @JoinColumn(name = \"department_id\")"},
            {"name": "cgpa", "type": "java.math.BigDecimal", "annotation": ""},
            {"name": "backlogs", "type": "Integer", "annotation": ""},
            {"name": "certifications", "type": "String", "annotation": ""},
            {"name": "projects", "type": "String", "annotation": ""},
            {"name": "profileCompletion", "type": "Integer", "annotation": "@Column(name = \"profile_completion\")"},
            {"name": "graduationYear", "type": "Integer", "annotation": "@Column(name = \"graduation_year\", nullable = false)"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp"}
        ]
    },
    "Company": {
        "table": "companies",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "name", "type": "String", "annotation": "@Column(unique = true, nullable = false)"},
            {"name": "industry", "type": "String", "annotation": ""},
            {"name": "hiringCriteria", "type": "String", "annotation": "@Column(name = \"hiring_criteria\")"},
            {"name": "description", "type": "String", "annotation": ""},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp"}
        ]
    },
    "Drive": {
        "table": "drives",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "company", "type": "Company", "annotation": "@ManyToOne @JoinColumn(name = \"company_id\")"},
            {"name": "title", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "jobRole", "type": "String", "annotation": "@Column(name = \"job_role\", nullable = false)"},
            {"name": "driveDate", "type": "java.time.LocalDate", "annotation": "@Column(name = \"drive_date\", nullable = false)"},
            {"name": "registrationDeadline", "type": "java.time.LocalDate", "annotation": "@Column(name = \"registration_deadline\", nullable = false)"},
            {"name": "testDate", "type": "java.time.LocalDate", "annotation": "@Column(name = \"test_date\")"},
            {"name": "interviewDate", "type": "java.time.LocalDate", "annotation": "@Column(name = \"interview_date\")"},
            {"name": "packageOffered", "type": "String", "annotation": "@Column(name = \"package\", nullable = false)"},
            {"name": "minCgpa", "type": "java.math.BigDecimal", "annotation": "@Column(name = \"min_cgpa\")"},
            {"name": "maxBacklogs", "type": "Integer", "annotation": "@Column(name = \"max_backlogs\")"},
            {"name": "eligibilityCriteria", "type": "String", "annotation": "@Column(name = \"eligibility_criteria\")"},
            {"name": "status", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp"}
        ]
    },
    "DriveApplication": {
        "table": "drive_applications",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@ManyToOne @JoinColumn(name = \"student_id\")"},
            {"name": "drive", "type": "Drive", "annotation": "@ManyToOne @JoinColumn(name = \"drive_id\")"},
            {"name": "status", "type": "String", "annotation": ""},
            {"name": "appliedAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "Placement": {
        "table": "placements",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@ManyToOne @JoinColumn(name = \"student_id\")"},
            {"name": "company", "type": "Company", "annotation": "@ManyToOne @JoinColumn(name = \"company_id\")"},
            {"name": "drive", "type": "Drive", "annotation": "@ManyToOne @JoinColumn(name = \"drive_id\")"},
            {"name": "packageOffered", "type": "java.math.BigDecimal", "annotation": "@Column(name = \"package_offered\", nullable = false)"},
            {"name": "jobRole", "type": "String", "annotation": "@Column(name = \"job_role\", nullable = false)"},
            {"name": "placementDate", "type": "java.time.LocalDate", "annotation": "@Column(name = \"placement_date\", nullable = false)"},
            {"name": "department", "type": "Department", "annotation": "@ManyToOne @JoinColumn(name = \"department_id\")"},
            {"name": "batch", "type": "Integer", "annotation": "@Column(nullable = false)"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "Skill": {
        "table": "skills",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "name", "type": "String", "annotation": "@Column(unique = true, nullable = false)"}
        ]
    },
    "Resume": {
        "table": "resumes",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@ManyToOne @JoinColumn(name = \"student_id\")"},
            {"name": "atsScore", "type": "Integer", "annotation": "@Column(name = \"ats_score\")"},
            {"name": "missingKeywords", "type": "String", "annotation": "@Column(name = \"missing_keywords\")"},
            {"name": "missingSkills", "type": "String", "annotation": "@Column(name = \"missing_skills\")"},
            {"name": "feedback", "type": "String", "annotation": ""},
            {"name": "uploadedAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "Interview": {
        "table": "interviews",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@ManyToOne @JoinColumn(name = \"student_id\")"},
            {"name": "drive", "type": "Drive", "annotation": "@ManyToOne @JoinColumn(name = \"drive_id\")"},
            {"name": "interviewType", "type": "String", "annotation": "@Column(name = \"interview_type\", nullable = false)"},
            {"name": "date", "type": "LocalDateTime", "annotation": "@Column(nullable = false)"},
            {"name": "questions", "type": "String", "annotation": ""},
            {"name": "answers", "type": "String", "annotation": ""},
            {"name": "technicalScore", "type": "Integer", "annotation": "@Column(name = \"technical_score\")"},
            {"name": "hrScore", "type": "Integer", "annotation": "@Column(name = \"hr_score\")"},
            {"name": "aptitudeScore", "type": "Integer", "annotation": "@Column(name = \"aptitude_score\")"},
            {"name": "feedback", "type": "String", "annotation": ""},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "Notification": {
        "table": "notifications",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "user", "type": "User", "annotation": "@ManyToOne @JoinColumn(name = \"user_id\")"},
            {"name": "type", "type": "String", "annotation": ""},
            {"name": "title", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "message", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "isRead", "type": "Boolean", "annotation": "@Column(name = \"is_read\")"},
            {"name": "scheduledTime", "type": "LocalDateTime", "annotation": "@Column(name = \"scheduled_time\")"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "TrainingProgram": {
        "table": "training_programs",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "title", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "description", "type": "String", "annotation": ""},
            {"name": "startDate", "type": "java.time.LocalDate", "annotation": "@Column(name = \"start_date\")"},
            {"name": "endDate", "type": "java.time.LocalDate", "annotation": "@Column(name = \"end_date\")"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "TrainingScore": {
        "table": "training_scores",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@ManyToOne @JoinColumn(name = \"student_id\")"},
            {"name": "trainingProgram", "type": "TrainingProgram", "annotation": "@ManyToOne @JoinColumn(name = \"training_program_id\")"},
            {"name": "beforeAptitude", "type": "Integer", "annotation": "@Column(name = \"before_aptitude\")"},
            {"name": "afterAptitude", "type": "Integer", "annotation": "@Column(name = \"after_aptitude\")"},
            {"name": "beforeCoding", "type": "Integer", "annotation": "@Column(name = \"before_coding\")"},
            {"name": "afterCoding", "type": "Integer", "annotation": "@Column(name = \"after_coding\")"},
            {"name": "beforeInterview", "type": "Integer", "annotation": "@Column(name = \"before_interview\")"},
            {"name": "afterInterview", "type": "Integer", "annotation": "@Column(name = \"after_interview\")"},
            {"name": "improvementPercentage", "type": "java.math.BigDecimal", "annotation": "@Column(name = \"improvement_percentage\")"}
        ]
    },
    "Course": {
        "table": "courses",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "name", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "platform", "type": "String", "annotation": ""},
            {"name": "url", "type": "String", "annotation": ""}
        ]
    },
    "Alumni": {
        "table": "alumni",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@OneToOne @JoinColumn(name = \"student_id\", unique = true)"},
            {"name": "currentCompany", "type": "String", "annotation": "@Column(name = \"current_company\")"},
            {"name": "currentRole", "type": "String", "annotation": "@Column(name = \"current_role\")"},
            {"name": "packageOffered", "type": "java.math.BigDecimal", "annotation": "@Column(name = \"package\")"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "AuditLog": {
        "table": "audit_logs",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "user", "type": "User", "annotation": "@ManyToOne @JoinColumn(name = \"user_id\")"},
            {"name": "action", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "entity", "type": "String", "annotation": "@Column(nullable = false)"},
            {"name": "entityId", "type": "UUID", "annotation": "@Column(name = \"entity_id\")"},
            {"name": "timestamp", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "Report": {
        "table": "reports",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "generatedBy", "type": "User", "annotation": "@ManyToOne @JoinColumn(name = \"generated_by\")"},
            {"name": "reportType", "type": "String", "annotation": "@Column(name = \"report_type\", nullable = false)"},
            {"name": "dataUrl", "type": "String", "annotation": "@Column(name = \"data_url\")"},
            {"name": "createdAt", "type": "LocalDateTime", "annotation": "@CreationTimestamp"}
        ]
    },
    "AiPlacementReadiness": {
        "table": "ai_placement_readiness",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@OneToOne @JoinColumn(name = \"student_id\", unique = true)"},
            {"name": "score", "type": "Integer", "annotation": ""},
            {"name": "readinessBreakdown", "type": "String", "annotation": "@Column(name = \"readiness_breakdown\")"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp"}
        ]
    },
    "AiPlacementPrediction": {
        "table": "ai_placement_predictions",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@OneToOne @JoinColumn(name = \"student_id\", unique = true)"},
            {"name": "successProbability", "type": "java.math.BigDecimal", "annotation": "@Column(name = \"success_probability\")"},
            {"name": "confidence", "type": "java.math.BigDecimal", "annotation": ""},
            {"name": "strengths", "type": "String", "annotation": ""},
            {"name": "weaknesses", "type": "String", "annotation": ""},
            {"name": "improvementSuggestions", "type": "String", "annotation": "@Column(name = \"improvement_suggestions\")"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp"}
        ]
    },
    "SkillsGapAnalysis": {
        "table": "skills_gap_analysis",
        "fields": [
            {"name": "id", "type": "UUID", "annotation": "@Id @GeneratedValue"},
            {"name": "student", "type": "Student", "annotation": "@ManyToOne @JoinColumn(name = \"student_id\")"},
            {"name": "targetRole", "type": "String", "annotation": "@Column(name = \"target_role\")"},
            {"name": "missingSkills", "type": "String", "annotation": "@Column(name = \"missing_skills\")"},
            {"name": "recommendedCourses", "type": "String", "annotation": "@Column(name = \"recommended_courses\")"},
            {"name": "learningRoadmap", "type": "String", "annotation": "@Column(name = \"learning_roadmap\")"},
            {"name": "updatedAt", "type": "LocalDateTime", "annotation": "@UpdateTimestamp"}
        ]
    }
}

def create_dirs():
    dirs = ['entity', 'repository', 'dto', 'mapper', 'service', 'controller', 'security', 'exception', 'config']
    for d in dirs:
        os.makedirs(os.path.join(base_dir, d), exist_ok=True)

def gen_entity(name, config):
    code = f"package {base_pkg}.entity;\n\n"
    code += "import jakarta.persistence.*;\nimport lombok.Data;\nimport org.hibernate.annotations.CreationTimestamp;\nimport org.hibernate.annotations.UpdateTimestamp;\nimport java.time.LocalDateTime;\nimport java.util.UUID;\n\n"
    code += "@Data\n@Entity\n@Table(name = \"" + config["table"] + "\")\n"
    code += f"public class {name} {{\n"
    
    for f in config["fields"]:
        if f["annotation"]:
            code += f"    {f['annotation']}\n"
        code += f"    private {f['type']} {f['name']};\n"
        
    code += "}\n"
    with open(os.path.join(base_dir, "entity", f"{name}.java"), "w") as f:
        f.write(code)

def gen_repository(name):
    code = f"package {base_pkg}.repository;\n\n"
    code += f"import {base_pkg}.entity.{name};\n"
    code += "import org.springframework.data.jpa.repository.JpaRepository;\nimport org.springframework.stereotype.Repository;\nimport java.util.UUID;\n\n"
    code += "@Repository\n"
    code += f"public interface {name}Repository extends JpaRepository<{name}, UUID> {{\n"
    code += "}\n"
    with open(os.path.join(base_dir, "repository", f"{name}Repository.java"), "w") as f:
        f.write(code)

def gen_dto(name, config):
    code = f"package {base_pkg}.dto;\n\n"
    code += "import lombok.Data;\nimport java.time.LocalDateTime;\nimport java.util.UUID;\n\n"
    code += "@Data\n"
    code += f"public class {name}DTO {{\n"
    for f in config["fields"]:
        t = f["type"]
        if t in ["User", "Department", "Student", "Company", "Drive", "TrainingProgram"]:
            t = "UUID"
            code += f"    private {t} {f['name']}Id;\n"
        else:
            code += f"    private {t} {f['name']};\n"
    code += "}\n"
    with open(os.path.join(base_dir, "dto", f"{name}DTO.java"), "w") as f:
        f.write(code)

def gen_service(name):
    code = f"package {base_pkg}.service;\n\n"
    code += f"import {base_pkg}.entity.{name};\n"
    code += f"import {base_pkg}.dto.{name}DTO;\n"
    code += "import java.util.List;\nimport java.util.UUID;\n\n"
    code += f"public interface {name}Service {{\n"
    code += f"    {name}DTO create({name}DTO dto);\n"
    code += f"    {name}DTO update(UUID id, {name}DTO dto);\n"
    code += f"    {name}DTO getById(UUID id);\n"
    code += f"    List<{name}DTO> getAll();\n"
    code += f"    void delete(UUID id);\n"
    code += "}\n"
    with open(os.path.join(base_dir, "service", f"{name}Service.java"), "w") as f:
        f.write(code)
        
    code = f"package {base_pkg}.service;\n\n"
    code += f"import {base_pkg}.entity.{name};\n"
    code += f"import {base_pkg}.dto.{name}DTO;\n"
    code += f"import {base_pkg}.repository.{name}Repository;\n"
    code += "import org.springframework.stereotype.Service;\n"
    code += "import lombok.RequiredArgsConstructor;\n"
    code += "import java.util.List;\nimport java.util.UUID;\n\n"
    code += "@Service\n@RequiredArgsConstructor\n"
    code += f"public class {name}ServiceImpl implements {name}Service {{\n"
    code += f"    private final {name}Repository repository;\n\n"
    code += f"    @Override\n    public {name}DTO create({name}DTO dto) {{ return null; }}\n"
    code += f"    @Override\n    public {name}DTO update(UUID id, {name}DTO dto) {{ return null; }}\n"
    code += f"    @Override\n    public {name}DTO getById(UUID id) {{ return null; }}\n"
    code += f"    @Override\n    public List<{name}DTO> getAll() {{ return List.of(); }}\n"
    code += f"    @Override\n    public void delete(UUID id) {{}}\n"
    code += "}\n"
    with open(os.path.join(base_dir, "service", f"{name}ServiceImpl.java"), "w") as f:
        f.write(code)

def gen_controller(name):
    code = f"package {base_pkg}.controller;\n\n"
    code += f"import {base_pkg}.dto.{name}DTO;\n"
    code += f"import {base_pkg}.service.{name}Service;\n"
    code += "import org.springframework.http.ResponseEntity;\n"
    code += "import org.springframework.web.bind.annotation.*;\n"
    code += "import lombok.RequiredArgsConstructor;\n"
    code += "import java.util.List;\nimport java.util.UUID;\n\n"
    code += "@RestController\n@RequestMapping(\"/api/" + name.lower() + "s\")\n@RequiredArgsConstructor\n"
    code += f"public class {name}Controller {{\n"
    code += f"    private final {name}Service service;\n\n"
    code += f"    @PostMapping\n    public ResponseEntity<{name}DTO> create(@RequestBody {name}DTO dto) {{\n        return ResponseEntity.ok(service.create(dto));\n    }}\n\n"
    code += f"    @GetMapping(\"/{{id}}\")\n    public ResponseEntity<{name}DTO> getById(@PathVariable UUID id) {{\n        return ResponseEntity.ok(service.getById(id));\n    }}\n\n"
    code += f"    @GetMapping\n    public ResponseEntity<List<{name}DTO>> getAll() {{\n        return ResponseEntity.ok(service.getAll());\n    }}\n\n"
    code += f"    @PutMapping(\"/{{id}}\")\n    public ResponseEntity<{name}DTO> update(@PathVariable UUID id, @RequestBody {name}DTO dto) {{\n        return ResponseEntity.ok(service.update(id, dto));\n    }}\n\n"
    code += f"    @DeleteMapping(\"/{{id}}\")\n    public ResponseEntity<Void> delete(@PathVariable UUID id) {{\n        service.delete(id);\n        return ResponseEntity.noContent().build();\n    }}\n"
    code += "}\n"
    with open(os.path.join(base_dir, "controller", f"{name}Controller.java"), "w") as f:
        f.write(code)

create_dirs()
for name, config in entities.items():
    gen_entity(name, config)
    gen_repository(name)
    gen_dto(name, config)
    gen_service(name)
    gen_controller(name)

print("Backend generation completed successfully.")
