-- AI Job Recommendation and Job Tracking System
-- MySQL Schema & Seed Data Script

CREATE DATABASE IF NOT EXISTS job_recommendation_db;
USE job_recommendation_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    headline VARCHAR(200) DEFAULT 'Aspiring Software Professional',
    location VARCHAR(100) DEFAULT 'Remote / Open',
    experience_years DECIMAL(4, 1) DEFAULT 1.0,
    preferred_work_mode VARCHAR(50) DEFAULT 'REMOTE', -- REMOTE, HYBRID, ONSITE, ANY
    bio TEXT,
    role VARCHAR(50) DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Skills Table
CREATE TABLE IF NOT EXISTS skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL -- Languages, Frameworks, Databases, Cloud & DevOps, Tools, AI/Data
);

-- 3. User Skills (Profile Skills)
CREATE TABLE IF NOT EXISTS user_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency_level VARCHAR(30) DEFAULT 'INTERMEDIATE', -- BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_skill (user_id, skill_id)
);

-- 4. Jobs Table
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    company VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    work_mode VARCHAR(50) NOT NULL DEFAULT 'REMOTE', -- REMOTE, HYBRID, ONSITE
    employment_type VARCHAR(50) NOT NULL DEFAULT 'FULL_TIME', -- FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    min_salary DECIMAL(10, 2),
    max_salary DECIMAL(10, 2),
    salary_currency VARCHAR(10) DEFAULT 'USD',
    min_experience_years DECIMAL(4, 1) DEFAULT 0.0,
    max_experience_years DECIMAL(4, 1) DEFAULT 5.0,
    description TEXT NOT NULL,
    responsibilities TEXT,
    eligibility TEXT,
    is_demo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Job Required Skills
CREATE TABLE IF NOT EXISTS job_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    is_required BOOLEAN DEFAULT TRUE, -- TRUE = Mandatory, FALSE = Nice-to-have
    weight DECIMAL(3, 2) DEFAULT 1.0,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY uq_job_skill (job_id, skill_id)
);

-- 6. Saved Jobs
CREATE TABLE IF NOT EXISTS saved_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_saved_job (user_id, job_id)
);

-- 7. Job Applications (Application Tracking System)
CREATE TABLE IF NOT EXISTS job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'INTERESTED', -- INTERESTED, APPLIED, ASSESSMENT, INTERVIEW, OFFER, REJECTED, WITHDRAWN
    applied_date DATE,
    application_deadline DATE,
    interview_date DATETIME,
    notes TEXT,
    next_steps TEXT,
    salary_offered DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_applied_job (user_id, job_id)
);

-- 8. Resume Analysis Table
CREATE TABLE IF NOT EXISTS resume_analyses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    parsed_text LONGTEXT,
    detected_skills_json TEXT, -- JSON array of detected skill names
    detected_experience_years DECIMAL(4, 1) DEFAULT 0.0,
    profile_summary TEXT,
    analyzed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 9. Learning Goals / Skill Gap Roadmaps
CREATE TABLE IF NOT EXISTS learning_goals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) DEFAULT 'General',
    target_job_title VARCHAR(150),
    roadmap_topics_json TEXT,
    recommended_resources_json TEXT,
    status VARCHAR(50) DEFAULT 'NOT_STARTED', -- NOT_STARTED, IN_PROGRESS, COMPLETED
    progress_percentage INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- SEED DATA: 50+ Core Skills
-- --------------------------------------------------------
INSERT IGNORE INTO skills (name, category) VALUES
-- Languages
('Java', 'Languages'),
('Python', 'Languages'),
('JavaScript', 'Languages'),
('TypeScript', 'Languages'),
('C++', 'Languages'),
('C#', 'Languages'),
('Go', 'Languages'),
('SQL', 'Languages'),
('HTML5', 'Languages'),
('CSS3', 'Languages'),
('Rust', 'Languages'),
('Kotlin', 'Languages'),
('Swift', 'Languages'),

-- Frameworks & Libraries
('React', 'Frameworks'),
('Spring Boot', 'Frameworks'),
('Node.js', 'Frameworks'),
('Express.js', 'Frameworks'),
('Angular', 'Frameworks'),
('Vue.js', 'Frameworks'),
('Next.js', 'Frameworks'),
('Django', 'Frameworks'),
('FastAPI', 'Frameworks'),
('Flask', 'Frameworks'),
('.NET Core', 'Frameworks'),

-- Databases & Caching
('MySQL', 'Databases'),
('PostgreSQL', 'Databases'),
('MongoDB', 'Databases'),
('Redis', 'Databases'),
('Elasticsearch', 'Databases'),
('Oracle DB', 'Databases'),

-- Cloud & DevOps
('Docker', 'Cloud & DevOps'),
('Kubernetes', 'Cloud & DevOps'),
('AWS', 'Cloud & DevOps'),
('Azure', 'Cloud & DevOps'),
('Google Cloud', 'Cloud & DevOps'),
('CI/CD', 'Cloud & DevOps'),
('Git', 'Cloud & DevOps'),
('GitHub Actions', 'Cloud & DevOps'),
('Terraform', 'Cloud & DevOps'),
('Linux', 'Cloud & DevOps'),

-- AI & Data Science
('Machine Learning', 'AI & Data'),
('Deep Learning', 'AI & Data'),
('Natural Language Processing', 'AI & Data'),
('TensorFlow', 'AI & Data'),
('PyTorch', 'AI & Data'),
('Pandas', 'AI & Data'),
('NumPy', 'AI & Data'),
('Data Analysis', 'AI & Data'),

-- Architecture & Concepts
('RESTful APIs', 'Architecture'),
('Microservices', 'Architecture'),
('GraphQL', 'Architecture'),
('System Design', 'Architecture'),
('Agile / Scrum', 'Methodology'),
('Unit Testing', 'Methodology');

-- --------------------------------------------------------
-- SEED DATA: Realistic Jobs
-- --------------------------------------------------------
INSERT IGNORE INTO jobs (id, title, company, location, work_mode, employment_type, min_salary, max_salary, min_experience_years, max_experience_years, description, responsibilities, eligibility, is_demo) VALUES
(1, 'Full Stack Java & React Developer', 'CloudScale Technologies', 'New York, NY', 'REMOTE', 'FULL_TIME', 85000.00, 115000.00, 1.0, 3.0,
'Join our product engineering squad building scalable enterprise SaaS workflows using modern React frontend and Spring Boot microservices backend.',
'Design and implement RESTful APIs using Spring Boot; Build responsive UI features with React and TypeScript; Write clean unit tests; Collaborate with agile product teams.',
'Bachelors in Computer Science or related degree; 1+ years experience in Java and modern JavaScript frameworks; Good understanding of relational databases.',
TRUE),

(2, 'Junior Frontend Developer (React)', 'PixelCraft Interactive', 'Austin, TX', 'HYBRID', 'FULL_TIME', 65000.00, 85000.00, 0.5, 2.0,
'We are looking for a motivated Frontend Developer proficient in React, CSS3, and JavaScript to deliver pixel-perfect user experiences for consumer web portals.',
'Convert UI/UX Figma prototypes to responsive React components; Integrate REST APIs; Optimize client-side bundle performance and accessibility.',
'Proficiency with React, HTML5, CSS3, JavaScript, Git; Portfolio of front-end projects; Strong communication and team-player mindset.',
TRUE),

(3, 'Backend Java Engineer (Spring Boot)', 'Apex Financial Systems', 'San Francisco, CA', 'HYBRID', 'FULL_TIME', 95000.00, 130000.00, 2.0, 5.0,
'Apex is seeking a Backend Engineer to build robust, secure financial transaction processing pipelines with Spring Boot, MySQL, and Kafka/Redis caching.',
'Architect highly reliable backend endpoints; Optimize SQL queries and database schemas; Ensure bank-grade API security and authentication standards.',
'2+ years in Java 17+, Spring Boot, Spring Security, Hibernate; Solid grasp of MySQL indexing, transactions, and RESTful architectures.',
TRUE),

(4, 'AI / Machine Learning Engineer', 'CognitiveMind Labs', 'Seattle, WA', 'REMOTE', 'FULL_TIME', 110000.00, 150000.00, 2.0, 4.0,
'Work at the forefront of applied NLP and predictive recommendation models for our customer intelligence platform.',
'Build and evaluate NLP pipelines and recommendation engines; Fine-tune open source models; Deploy model inference endpoints with FastAPI and Docker.',
'Strong knowledge of Python, PyTorch/TensorFlow, Pandas, Scikit-learn; Experience with vector embeddings and recommendation algorithms.',
TRUE),

(5, 'DevOps & Cloud Engineer', 'Skyline Cloud Solutions', 'Remote', 'REMOTE', 'FULL_TIME', 90000.00, 125000.00, 2.0, 4.0,
'Help manage cloud deployment infrastructure, automated CI/CD pipelines, and containerized microservices.',
'Manage AWS cloud resources and Terraform infrastructure as code; Maintain Kubernetes clusters and Docker containers; Build GitHub Actions workflows.',
'Hands-on experience with Docker, Kubernetes, AWS, Terraform, CI/CD, and Linux bash scripting.',
TRUE),

(6, 'Associate Python Backend Developer', 'DataStream Analytics', 'Boston, MA', 'ONSITE', 'FULL_TIME', 70000.00, 90000.00, 0.0, 2.0,
'Great entry to junior role for developers keen on Python, data scraping, and API development for analytics reporting.',
'Develop backend endpoints with Python (FastAPI/Django); Process batch data with Pandas; Connect and query PostgreSQL and MySQL databases.',
'Proficiency in Python, SQL, REST APIs; Familiarity with Git and Docker; Strong analytical problem-solving skills.',
TRUE),

(7, 'Software Engineer Intern (Summer 2026)', 'NextGen Innovations', 'Chicago, IL', 'HYBRID', 'INTERNSHIP', 45000.00, 60000.00, 0.0, 1.0,
'Exciting internship program for university students to work alongside senior engineers on production features.',
'Contribute code to core web application; Participate in daily standups and code reviews; Implement unit tests and bug fixes.',
'Currently enrolled in CS or IT program; Familiarity with either Java or JavaScript/React; Eager to learn modern engineering practices.',
TRUE),

(8, 'Full Stack Web Developer (Node.js & Vue)', 'VentureWorks Digital', 'Denver, CO', 'REMOTE', 'FULL_TIME', 80000.00, 105000.00, 1.5, 3.5,
'Join a high-growth agile team developing interactive dashboards and real-time collaboration tools.',
'Build frontend interfaces using Vue.js/React and backend services using Node.js & Express; Manage MongoDB and Redis data stores.',
'Experience in Node.js, JavaScript/TypeScript, modern UI frameworks, and RESTful service design.',
TRUE);

-- --------------------------------------------------------
-- SEED DATA: Job Skills Mappings
-- --------------------------------------------------------
-- Job 1: Full Stack Java & React
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(1, (SELECT id FROM skills WHERE name = 'Java'), TRUE, 1.0),
(1, (SELECT id FROM skills WHERE name = 'Spring Boot'), TRUE, 1.0),
(1, (SELECT id FROM skills WHERE name = 'React'), TRUE, 1.0),
(1, (SELECT id FROM skills WHERE name = 'JavaScript'), TRUE, 0.9),
(1, (SELECT id FROM skills WHERE name = 'MySQL'), TRUE, 0.8),
(1, (SELECT id FROM skills WHERE name = 'RESTful APIs'), TRUE, 0.9),
(1, (SELECT id FROM skills WHERE name = 'Docker'), FALSE, 0.6),
(1, (SELECT id FROM skills WHERE name = 'Git'), TRUE, 0.7);

-- Job 2: Junior Frontend Developer (React)
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(2, (SELECT id FROM skills WHERE name = 'React'), TRUE, 1.0),
(2, (SELECT id FROM skills WHERE name = 'JavaScript'), TRUE, 1.0),
(2, (SELECT id FROM skills WHERE name = 'HTML5'), TRUE, 0.9),
(2, (SELECT id FROM skills WHERE name = 'CSS3'), TRUE, 0.9),
(2, (SELECT id FROM skills WHERE name = 'TypeScript'), FALSE, 0.7),
(2, (SELECT id FROM skills WHERE name = 'Git'), TRUE, 0.7);

-- Job 3: Backend Java Engineer
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(3, (SELECT id FROM skills WHERE name = 'Java'), TRUE, 1.0),
(3, (SELECT id FROM skills WHERE name = 'Spring Boot'), TRUE, 1.0),
(3, (SELECT id FROM skills WHERE name = 'MySQL'), TRUE, 0.9),
(3, (SELECT id FROM skills WHERE name = 'RESTful APIs'), TRUE, 0.9),
(3, (SELECT id FROM skills WHERE name = 'Microservices'), TRUE, 0.8),
(3, (SELECT id FROM skills WHERE name = 'Redis'), FALSE, 0.6),
(3, (SELECT id FROM skills WHERE name = 'Unit Testing'), TRUE, 0.7);

-- Job 4: AI / Machine Learning Engineer
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(4, (SELECT id FROM skills WHERE name = 'Python'), TRUE, 1.0),
(4, (SELECT id FROM skills WHERE name = 'Machine Learning'), TRUE, 1.0),
(4, (SELECT id FROM skills WHERE name = 'Natural Language Processing'), TRUE, 0.9),
(4, (SELECT id FROM skills WHERE name = 'PyTorch'), TRUE, 0.8),
(4, (SELECT id FROM skills WHERE name = 'Pandas'), TRUE, 0.8),
(4, (SELECT id FROM skills WHERE name = 'FastAPI'), FALSE, 0.7),
(4, (SELECT id FROM skills WHERE name = 'Docker'), FALSE, 0.6);

-- Job 5: DevOps & Cloud Engineer
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(5, (SELECT id FROM skills WHERE name = 'Docker'), TRUE, 1.0),
(5, (SELECT id FROM skills WHERE name = 'Kubernetes'), TRUE, 1.0),
(5, (SELECT id FROM skills WHERE name = 'AWS'), TRUE, 1.0),
(5, (SELECT id FROM skills WHERE name = 'CI/CD'), TRUE, 0.9),
(5, (SELECT id FROM skills WHERE name = 'Linux'), TRUE, 0.8),
(5, (SELECT id FROM skills WHERE name = 'Terraform'), FALSE, 0.7);

-- Job 6: Associate Python Backend Developer
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(6, (SELECT id FROM skills WHERE name = 'Python'), TRUE, 1.0),
(6, (SELECT id FROM skills WHERE name = 'SQL'), TRUE, 0.9),
(6, (SELECT id FROM skills WHERE name = 'FastAPI'), TRUE, 0.8),
(6, (SELECT id FROM skills WHERE name = 'Git'), TRUE, 0.7),
(6, (SELECT id FROM skills WHERE name = 'Pandas'), FALSE, 0.6);

-- Job 7: Software Engineer Intern
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(7, (SELECT id FROM skills WHERE name = 'Java'), FALSE, 0.8),
(7, (SELECT id FROM skills WHERE name = 'JavaScript'), FALSE, 0.8),
(7, (SELECT id FROM skills WHERE name = 'SQL'), TRUE, 0.7),
(7, (SELECT id FROM skills WHERE name = 'Git'), TRUE, 0.8);

-- Job 8: Full Stack Web Developer (Node.js & Vue)
INSERT IGNORE INTO job_skills (job_id, skill_id, is_required, weight) VALUES
(8, (SELECT id FROM skills WHERE name = 'Node.js'), TRUE, 1.0),
(8, (SELECT id FROM skills WHERE name = 'JavaScript'), TRUE, 1.0),
(8, (SELECT id FROM skills WHERE name = 'Vue.js'), TRUE, 0.9),
(8, (SELECT id FROM skills WHERE name = 'MongoDB'), TRUE, 0.8),
(8, (SELECT id FROM skills WHERE name = 'RESTful APIs'), TRUE, 0.8);

-- --------------------------------------------------------
-- SEED DATA: Demo User (Password: Password@123)
-- BCrypt hash for 'Password@123': $2a$10$wN9a3x3M5jV61q72bKgeeuM3a03H2tHhBwJkX8oYnflhN95Xv9g6a
-- --------------------------------------------------------
INSERT IGNORE INTO users (id, full_name, email, password_hash, headline, location, experience_years, preferred_work_mode, bio, role) VALUES
(1, 'Alex Morgan', 'demo@example.com', '$2a$10$wN9a3x3M5jV61q72bKgeeuM3a03H2tHhBwJkX8oYnflhN95Xv9g6a',
'Junior Full Stack Developer | Java & React Enthusiast', 'New York, NY', 1.5, 'REMOTE',
'Motivated software engineering graduate with hands-on experience in building full-stack web applications using React, Spring Boot, and MySQL.', 'ROLE_USER');

-- Demo User Skills
INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level) VALUES
(1, (SELECT id FROM skills WHERE name = 'Java'), 'INTERMEDIATE'),
(1, (SELECT id FROM skills WHERE name = 'React'), 'INTERMEDIATE'),
(1, (SELECT id FROM skills WHERE name = 'JavaScript'), 'INTERMEDIATE'),
(1, (SELECT id FROM skills WHERE name = 'HTML5'), 'ADVANCED'),
(1, (SELECT id FROM skills WHERE name = 'CSS3'), 'ADVANCED'),
(1, (SELECT id FROM skills WHERE name = 'Git'), 'INTERMEDIATE'),
(1, (SELECT id FROM skills WHERE name = 'SQL'), 'INTERMEDIATE');

-- Demo User Saved Job
INSERT IGNORE INTO saved_jobs (user_id, job_id) VALUES
(1, 2);

-- Demo User Application
INSERT IGNORE INTO job_applications (user_id, job_id, status, applied_date, application_deadline, interview_date, notes, next_steps) VALUES
(1, 1, 'APPLIED', '2026-10-15', '2026-11-01', NULL, 'Submitted application via company portal. Referred by college alumnus.', 'Prepare for preliminary phone screen on core Java and React hooks.'),
(1, 2, 'INTERVIEW', '2026-10-10', '2026-10-30', '2026-10-28 15:00:00', 'Passed technical screening! Interviewing with Frontend Tech Lead.', 'Review CSS Flexbox/Grid, React state management, and portfolio project walkthrough.');
