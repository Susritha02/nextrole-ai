package com.jobrec.config;

import com.jobrec.entity.*;
import com.jobrec.repository.*;
import com.jobrec.util.SkillDictionary;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final JobSkillRepository jobSkillRepository;
    private final UserSkillRepository userSkillRepository;
    private final JobApplicationRepository applicationRepository;
    private final SavedJobRepository savedJobRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
            UserRepository userRepository,
            SkillRepository skillRepository,
            JobRepository jobRepository,
            JobSkillRepository jobSkillRepository,
            UserSkillRepository userSkillRepository,
            JobApplicationRepository applicationRepository,
            SavedJobRepository savedJobRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.jobSkillRepository = jobSkillRepository;
        this.userSkillRepository = userSkillRepository;
        this.applicationRepository = applicationRepository;
        this.savedJobRepository = savedJobRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Initialize Skills
        if (skillRepository.count() == 0) {
            initSkills();
        }

        // 2. Initialize Jobs
        if (jobRepository.count() == 0) {
            initJobs();
        }

        // 3. Initialize Demo User & Sample Applications
        if (!userRepository.existsByEmail("demo@example.com")) {
            initDemoUser();
        }
    }

    private void initSkills() {
        for (SkillDictionary.SkillMeta meta : SkillDictionary.getAllSkills()) {
            skillRepository.save(new Skill(meta.getCanonicalName(), meta.getCategory()));
        }
        // Additional useful skills
        List<String[]> extra = List.of(
                new String[]{"Angular", "Frameworks"},
                new String[]{"Vue.js", "Frameworks"},
                new String[]{"Next.js", "Frameworks"},
                new String[]{"Django", "Frameworks"},
                new String[]{"Flask", "Frameworks"},
                new String[]{".NET Core", "Frameworks"},
                new String[]{"Redis", "Databases"},
                new String[]{"Elasticsearch", "Databases"},
                new String[]{"GraphQL", "Architecture"},
                new String[]{"System Design", "Architecture"},
                new String[]{"Agile / Scrum", "Methodology"}
        );
        for (String[] item : extra) {
            if (skillRepository.findByNameIgnoreCase(item[0]).isEmpty()) {
                skillRepository.save(new Skill(item[0], item[1]));
            }
        }
    }

    private void initJobs() {
        // Job 1
        Job j1 = createJob(
                "Full Stack Java & React Developer",
                "CloudScale Technologies",
                "New York, NY",
                "REMOTE",
                "FULL_TIME",
                BigDecimal.valueOf(85000),
                BigDecimal.valueOf(115000),
                1.0,
                3.0,
                "Join our core product team building scalable enterprise SaaS workflows using modern React frontend and Spring Boot microservices backend.",
                "Design and implement RESTful APIs; Build responsive UI features with React; Write automated unit tests; Collaborate with agile product squads.",
                "Bachelors in CS or relevant experience; 1+ years experience in Java and React; Solid grasp of relational databases."
        );
        addSkillToJob(j1, "Java", true, 1.0);
        addSkillToJob(j1, "Spring Boot", true, 1.0);
        addSkillToJob(j1, "React", true, 1.0);
        addSkillToJob(j1, "JavaScript", true, 0.9);
        addSkillToJob(j1, "MySQL", true, 0.8);
        addSkillToJob(j1, "RESTful APIs", true, 0.9);
        addSkillToJob(j1, "Docker", false, 0.6);
        addSkillToJob(j1, "Git", true, 0.7);

        // Job 2
        Job j2 = createJob(
                "Junior Frontend Developer (React)",
                "PixelCraft Interactive",
                "Austin, TX",
                "HYBRID",
                "FULL_TIME",
                BigDecimal.valueOf(65000),
                BigDecimal.valueOf(85000),
                0.5,
                2.0,
                "Deliver responsive, accessible, pixel-perfect user experiences for our consumer-facing web portals using React, CSS3, and JavaScript.",
                "Convert Figma prototypes to responsive React components; Integrate REST APIs; Optimize client-side bundle performance and accessibility.",
                "Proficiency with React, HTML5, CSS3, JavaScript, Git; Portfolio of front-end projects; Strong communication and team-player mindset."
        );
        addSkillToJob(j2, "React", true, 1.0);
        addSkillToJob(j2, "JavaScript", true, 1.0);
        addSkillToJob(j2, "HTML5", true, 0.9);
        addSkillToJob(j2, "CSS3", true, 0.9);
        addSkillToJob(j2, "TypeScript", false, 0.7);
        addSkillToJob(j2, "Git", true, 0.7);

        // Job 3
        Job j3 = createJob(
                "Backend Java Engineer (Spring Boot)",
                "Apex Financial Systems",
                "San Francisco, CA",
                "HYBRID",
                "FULL_TIME",
                BigDecimal.valueOf(95000),
                BigDecimal.valueOf(130000),
                2.0,
                5.0,
                "Architect robust, secure financial transaction processing pipelines with Spring Boot, MySQL, and Kafka/Redis caching.",
                "Design highly reliable backend endpoints; Optimize SQL queries and database schemas; Ensure bank-grade API security and authentication standards.",
                "2+ years in Java, Spring Boot, Spring Security, Hibernate; Solid grasp of MySQL indexing, transactions, and RESTful architectures."
        );
        addSkillToJob(j3, "Java", true, 1.0);
        addSkillToJob(j3, "Spring Boot", true, 1.0);
        addSkillToJob(j3, "MySQL", true, 0.9);
        addSkillToJob(j3, "RESTful APIs", true, 0.9);
        addSkillToJob(j3, "Microservices", true, 0.8);
        addSkillToJob(j3, "Unit Testing", true, 0.7);

        // Job 4
        Job j4 = createJob(
                "AI / Machine Learning Engineer",
                "CognitiveMind Labs",
                "Seattle, WA",
                "REMOTE",
                "FULL_TIME",
                BigDecimal.valueOf(110000),
                BigDecimal.valueOf(150000),
                2.0,
                4.0,
                "Work on applied NLP and predictive recommendation models for our customer intelligence platform.",
                "Build and evaluate NLP pipelines and recommendation engines; Fine-tune open source models; Deploy inference endpoints with FastAPI and Docker.",
                "Strong knowledge of Python, PyTorch, Scikit-learn; Experience with vector embeddings and recommendation algorithms."
        );
        addSkillToJob(j4, "Python", true, 1.0);
        addSkillToJob(j4, "Machine Learning", true, 1.0);
        addSkillToJob(j4, "Natural Language Processing", true, 0.9);
        addSkillToJob(j4, "PyTorch", true, 0.8);
        addSkillToJob(j4, "FastAPI", false, 0.7);
        addSkillToJob(j4, "Docker", false, 0.6);

        // Job 5
        Job j5 = createJob(
                "DevOps & Cloud Infrastructure Engineer",
                "Skyline Cloud Solutions",
                "Remote",
                "REMOTE",
                "FULL_TIME",
                BigDecimal.valueOf(90000),
                BigDecimal.valueOf(125000),
                2.0,
                4.0,
                "Help manage cloud deployment infrastructure, automated CI/CD pipelines, and containerized microservices on AWS.",
                "Manage AWS cloud resources and Terraform infrastructure; Maintain Kubernetes clusters and Docker containers; Build GitHub Actions workflows.",
                "Hands-on experience with Docker, Kubernetes, AWS, CI/CD, and Linux bash scripting."
        );
        addSkillToJob(j5, "Docker", true, 1.0);
        addSkillToJob(j5, "Kubernetes", true, 1.0);
        addSkillToJob(j5, "AWS", true, 1.0);
        addSkillToJob(j5, "CI/CD", true, 0.9);
        addSkillToJob(j5, "Git", true, 0.8);

        // Job 6
        Job j6 = createJob(
                "Associate Python Backend Developer",
                "DataStream Analytics",
                "Boston, MA",
                "ONSITE",
                "FULL_TIME",
                BigDecimal.valueOf(70000),
                BigDecimal.valueOf(90000),
                0.0,
                2.0,
                "Great entry to junior role for developers keen on Python, data processing, and API development for analytics reporting.",
                "Develop backend endpoints with Python; Process batch data with Pandas; Connect and query PostgreSQL and MySQL databases.",
                "Proficiency in Python, SQL, REST APIs; Familiarity with Git; Strong analytical problem-solving skills."
        );
        addSkillToJob(j6, "Python", true, 1.0);
        addSkillToJob(j6, "SQL", true, 0.9);
        addSkillToJob(j6, "FastAPI", true, 0.8);
        addSkillToJob(j6, "Git", true, 0.7);

        // Job 7
        Job j7 = createJob(
                "Software Engineer Intern (Summer 2026)",
                "NextGen Innovations",
                "Chicago, IL",
                "HYBRID",
                "INTERNSHIP",
                BigDecimal.valueOf(45000),
                BigDecimal.valueOf(60000),
                0.0,
                1.0,
                "Exciting internship program for university students to work alongside senior engineers on production features.",
                "Contribute code to core web application; Participate in daily standups and code reviews; Implement unit tests and bug fixes.",
                "Currently enrolled in CS or IT program; Familiarity with either Java or JavaScript/React; Eager to learn modern engineering practices."
        );
        addSkillToJob(j7, "Java", false, 0.8);
        addSkillToJob(j7, "JavaScript", false, 0.8);
        addSkillToJob(j7, "SQL", true, 0.7);
        addSkillToJob(j7, "Git", true, 0.8);

        // Job 8
        Job j8 = createJob(
                "Full Stack Web Developer (Node.js & React)",
                "VentureWorks Digital",
                "Denver, CO",
                "REMOTE",
                "FULL_TIME",
                BigDecimal.valueOf(80000),
                BigDecimal.valueOf(105000),
                1.5,
                3.5,
                "Join a high-growth agile team developing interactive dashboards and real-time collaboration tools.",
                "Build frontend interfaces using React and backend services using Node.js & Express; Manage MongoDB and Redis data stores.",
                "Experience in Node.js, React, JavaScript/TypeScript, and RESTful service design."
        );
        addSkillToJob(j8, "Node.js", true, 1.0);
        addSkillToJob(j8, "React", true, 1.0);
        addSkillToJob(j8, "JavaScript", true, 1.0);
        addSkillToJob(j8, "MongoDB", true, 0.8);
        addSkillToJob(j8, "RESTful APIs", true, 0.8);
    }

    private Job createJob(String title, String company, String location, String workMode, String employmentType,
                          BigDecimal minSal, BigDecimal maxSal, Double minExp, Double maxExp,
                          String description, String responsibilities, String eligibility) {
        Job job = new Job();
        job.setTitle(title);
        job.setCompany(company);
        job.setLocation(location);
        job.setWorkMode(workMode);
        job.setEmploymentType(employmentType);
        job.setMinSalary(minSal);
        job.setMaxSalary(maxSal);
        job.setMinExperienceYears(minExp);
        job.setMaxExperienceYears(maxExp);
        job.setDescription(description);
        job.setResponsibilities(responsibilities);
        job.setEligibility(eligibility);
        job.setIsDemo(true);
        return jobRepository.save(job);
    }

    private void addSkillToJob(Job job, String skillName, boolean isRequired, double weight) {
        Skill skill = skillRepository.findByNameIgnoreCase(skillName).orElseGet(() -> {
            Skill s = new Skill(skillName, "General");
            return skillRepository.save(s);
        });
        JobSkill js = new JobSkill(job, skill, isRequired, weight);
        jobSkillRepository.save(js);
    }

    private void initDemoUser() {
        User user = new User();
        user.setFullName("Alex Morgan");
        user.setEmail("demo@example.com");
        user.setPasswordHash(passwordEncoder.encode("Password@123"));
        user.setHeadline("Junior Full Stack Developer | Java & React Enthusiast");
        user.setLocation("New York, NY");
        user.setExperienceYears(1.5);
        user.setPreferredWorkMode("REMOTE");
        user.setBio("Motivated software engineering graduate with hands-on experience in building full-stack web applications using React, Spring Boot, and MySQL.");
        final User savedUser = userRepository.save(user);

        // Add profile skills
        List<String> skills = List.of("Java", "React", "JavaScript", "HTML5", "CSS3", "Git", "SQL");
        for (String sName : skills) {
            Skill s = skillRepository.findByNameIgnoreCase(sName).orElse(null);
            if (s != null) {
                userSkillRepository.save(new UserSkill(savedUser, s, "INTERMEDIATE"));
            }
        }

        // Demo saved job
        Optional<Job> j2 = jobRepository.findById(2L);
        j2.ifPresent(job -> savedJobRepository.save(new SavedJob(savedUser, job)));

        // Demo applications
        Optional<Job> j1 = jobRepository.findById(1L);
        if (j1.isPresent()) {
            JobApplication app1 = new JobApplication();
            app1.setUser(savedUser);
            app1.setJob(j1.get());
            app1.setStatus(JobApplication.Status.APPLIED);
            app1.setAppliedDate(LocalDate.now().minusDays(10));
            app1.setApplicationDeadline(LocalDate.now().plusDays(15));
            app1.setNotes("Submitted application via company portal. Referred by college alumnus.");
            app1.setNextSteps("Prepare for preliminary phone screen on core Java and React hooks.");
            applicationRepository.save(app1);
        }

        if (j2.isPresent()) {
            JobApplication app2 = new JobApplication();
            app2.setUser(savedUser);
            app2.setJob(j2.get());
            app2.setStatus(JobApplication.Status.INTERVIEW);
            app2.setAppliedDate(LocalDate.now().minusDays(15));
            app2.setApplicationDeadline(LocalDate.now().plusDays(5));
            app2.setInterviewDate(LocalDateTime.now().plusDays(3).withHour(15).withMinute(0));
            app2.setNotes("Passed preliminary screening. Interviewing with Lead Frontend Architect.");
            app2.setNextSteps("Review CSS Flexbox/Grid, React state management, and portfolio project.");
            applicationRepository.save(app2);
        }
    }
}
