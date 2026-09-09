-- HireTrack AI Production Schema
-- Compatible with PostgreSQL 13+

-- Drop existing tables if re-running
DROP TABLE IF EXISTS audit_logs CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS alumni CASCADE;
DROP TABLE IF EXISTS training_scores CASCADE;
DROP TABLE IF EXISTS training_programs CASCADE;
DROP TABLE IF EXISTS skills_gap_analysis CASCADE;
DROP TABLE IF EXISTS ai_placement_predictions CASCADE;
DROP TABLE IF EXISTS ai_placement_readiness CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS interviews CASCADE;
DROP TABLE IF EXISTS resumes CASCADE;
DROP TABLE IF EXISTS company_required_skills CASCADE;
DROP TABLE IF EXISTS student_skills CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS placements CASCADE;
DROP TABLE IF EXISTS drive_applications CASCADE;
DROP TABLE IF EXISTS drives CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS reports CASCADE;

DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS drive_status CASCADE;
DROP TYPE IF EXISTS app_status CASCADE;
DROP TYPE IF EXISTS notif_type CASCADE;

-- ENUMS
CREATE TYPE user_role AS ENUM ('ADMIN', 'PLACEMENT_OFFICER', 'STUDENT', 'HOD');
CREATE TYPE drive_status AS ENUM ('UPCOMING', 'REGISTRATION_OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE app_status AS ENUM ('APPLIED', 'SHORTLISTED', 'INTERVIEW', 'SELECTED', 'REJECTED');
CREATE TYPE notif_type AS ENUM ('IN_APP', 'EMAIL', 'BOTH');

-- CORE TABLES
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    register_number VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    department_id UUID REFERENCES departments(id) ON DELETE SET NULL,
    cgpa DECIMAL(4,2) DEFAULT 0.0 CHECK (cgpa >= 0 AND cgpa <= 10),
    backlogs INTEGER DEFAULT 0 CHECK (backlogs >= 0),
    certifications TEXT,
    projects TEXT,
    profile_completion INTEGER DEFAULT 0 CHECK (profile_completion >= 0 AND profile_completion <= 100),
    graduation_year INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL,
    industry VARCHAR(100),
    hiring_criteria TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE drives (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID REFERENCES companies(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    job_role VARCHAR(255) NOT NULL,
    drive_date DATE NOT NULL,
    registration_deadline DATE NOT NULL,
    test_date DATE,
    interview_date DATE,
    package VARCHAR(100) NOT NULL,
    min_cgpa DECIMAL(4,2) DEFAULT 0.0,
    max_backlogs INTEGER DEFAULT 0,
    eligibility_criteria TEXT,
    status drive_status DEFAULT 'UPCOMING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE drive_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    drive_id UUID REFERENCES drives(id) ON DELETE CASCADE,
    status app_status DEFAULT 'APPLIED',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, drive_id)
);

CREATE TABLE placements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    company_id UUID REFERENCES companies(id) ON DELETE CASCADE,
    drive_id UUID REFERENCES drives(id) ON DELETE SET NULL,
    package_offered DECIMAL(10,2) NOT NULL,
    job_role VARCHAR(255) NOT NULL,
    placement_date DATE NOT NULL,
    department_id UUID REFERENCES departments(id) ON DELETE SET NULL,
    batch INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE student_skills (
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    skill_id UUID REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, skill_id)
);

CREATE TABLE company_required_skills (
    drive_id UUID REFERENCES drives(id) ON DELETE CASCADE,
    skill_id UUID REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (drive_id, skill_id)
);

CREATE TABLE resumes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    ats_score INTEGER DEFAULT 0 CHECK (ats_score >= 0 AND ats_score <= 100),
    missing_keywords TEXT,
    missing_skills TEXT,
    feedback TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE interviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    drive_id UUID REFERENCES drives(id) ON DELETE SET NULL,
    interview_type VARCHAR(100) NOT NULL,
    date TIMESTAMP NOT NULL,
    questions TEXT,
    answers TEXT,
    technical_score INTEGER CHECK (technical_score >= 0 AND technical_score <= 100),
    hr_score INTEGER CHECK (hr_score >= 0 AND hr_score <= 100),
    aptitude_score INTEGER CHECK (aptitude_score >= 0 AND aptitude_score <= 100),
    feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    type notif_type DEFAULT 'IN_APP',
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    scheduled_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE training_programs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE training_scores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    training_program_id UUID REFERENCES training_programs(id) ON DELETE CASCADE,
    before_aptitude INTEGER,
    after_aptitude INTEGER,
    before_coding INTEGER,
    after_coding INTEGER,
    before_interview INTEGER,
    after_interview INTEGER,
    improvement_percentage DECIMAL(5,2),
    UNIQUE(student_id, training_program_id)
);

CREATE TABLE courses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    platform VARCHAR(100),
    url TEXT
);

CREATE TABLE alumni (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID UNIQUE REFERENCES students(id) ON DELETE CASCADE,
    current_company VARCHAR(255),
    current_role VARCHAR(255),
    package DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(255) NOT NULL,
    entity VARCHAR(100) NOT NULL,
    entity_id UUID,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    generated_by UUID REFERENCES users(id) ON DELETE SET NULL,
    report_type VARCHAR(100) NOT NULL,
    data_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- AI DATA TABLES
CREATE TABLE ai_placement_readiness (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID UNIQUE REFERENCES students(id) ON DELETE CASCADE,
    score INTEGER CHECK (score >= 0 AND score <= 100),
    readiness_breakdown JSONB,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_placement_predictions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID UNIQUE REFERENCES students(id) ON DELETE CASCADE,
    success_probability DECIMAL(5,2) CHECK (success_probability >= 0 AND success_probability <= 100),
    confidence DECIMAL(5,2),
    strengths TEXT,
    weaknesses TEXT,
    improvement_suggestions TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skills_gap_analysis (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID REFERENCES students(id) ON DELETE CASCADE,
    target_role VARCHAR(255),
    missing_skills TEXT,
    recommended_courses TEXT,
    learning_roadmap JSONB,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- INDEXES
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_students_dept ON students(department_id);
CREATE INDEX idx_students_cgpa ON students(cgpa);
CREATE INDEX idx_drives_date ON drives(drive_date);
CREATE INDEX idx_placements_company ON placements(company_id);
CREATE INDEX idx_placements_batch ON placements(batch);

-- ==========================================
-- SAMPLE DATA INSERTION
-- ==========================================

-- 1. Departments
INSERT INTO departments (id, name, code) VALUES 
(gen_random_uuid(), 'Computer Science and Engineering', 'CSE'),
(gen_random_uuid(), 'Information Technology', 'IT'),
(gen_random_uuid(), 'Electronics and Communication', 'ECE');

-- 2. Users
INSERT INTO users (id, email, password, role) VALUES 
(gen_random_uuid(), 'admin@college.edu', '$2a$10$xyz', 'ADMIN'),
(gen_random_uuid(), 'po@college.edu', '$2a$10$xyz', 'PLACEMENT_OFFICER'),
(gen_random_uuid(), 'john.doe@student.edu', '$2a$10$xyz', 'STUDENT');

-- 3. Skills
INSERT INTO skills (id, name) VALUES 
(gen_random_uuid(), 'Java'),
(gen_random_uuid(), 'Python'),
(gen_random_uuid(), 'React'),
(gen_random_uuid(), 'Spring Boot'),
(gen_random_uuid(), 'SQL'),
(gen_random_uuid(), 'Machine Learning');

-- 4. Companies
INSERT INTO companies (id, name, industry, hiring_criteria, description) VALUES 
(gen_random_uuid(), 'TechCorp', 'IT Services', 'Minimum 7.0 CGPA, No Active Backlogs', 'Global IT consulting and services provider'),
(gen_random_uuid(), 'InnoSoft', 'Product', 'Strong problem solving skills, React & Java', 'Innovative software products company');

-- 5. Students (Linked to Users and Departments)
INSERT INTO students (id, user_id, name, register_number, email, phone, department_id, cgpa, backlogs, profile_completion, graduation_year)
SELECT gen_random_uuid(), u.id, 'John Doe', 'REG1001', u.email, '1234567890', d.id, 8.5, 0, 80, 2024
FROM users u, departments d
WHERE u.email = 'john.doe@student.edu' AND d.code = 'CSE';

-- 6. Student Skills
INSERT INTO student_skills (student_id, skill_id)
SELECT s.id, sk.id
FROM students s, skills sk
WHERE s.register_number = 'REG1001' AND sk.name IN ('Java', 'Spring Boot', 'React');

-- 7. Drives (Linked to Companies)
INSERT INTO drives (id, company_id, title, job_role, drive_date, registration_deadline, package, min_cgpa, max_backlogs, status)
SELECT gen_random_uuid(), c.id, 'TechCorp Campus Drive 2024', 'Software Engineer', CURRENT_DATE + INTERVAL '10 days', CURRENT_DATE + INTERVAL '5 days', '8 LPA', 7.0, 1, 'UPCOMING'
FROM companies c WHERE c.name = 'TechCorp';

-- 8. Company Required Skills
INSERT INTO company_required_skills (drive_id, skill_id)
SELECT d.id, sk.id
FROM drives d, skills sk
WHERE d.title = 'TechCorp Campus Drive 2024' AND sk.name IN ('Java', 'SQL');

-- 9. Placements (Mock past placement data)
INSERT INTO placements (id, student_id, company_id, package_offered, job_role, placement_date, department_id, batch)
SELECT gen_random_uuid(), s.id, c.id, 12.5, 'SDE-1', CURRENT_DATE - INTERVAL '30 days', s.department_id, 2023
FROM students s, companies c
WHERE s.register_number = 'REG1001' AND c.name = 'InnoSoft';

-- 10. Notifications
INSERT INTO notifications (user_id, title, message, type)
SELECT u.id, 'Welcome to HireTrack AI', 'Please complete your profile to get placement predictions.', 'IN_APP'
FROM users u WHERE u.role = 'STUDENT';
