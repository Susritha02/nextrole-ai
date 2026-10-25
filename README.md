# AI Job Recommendation and Job Tracking System

A full-stack, enterprise-grade web application that leverages an intelligent AI recommendation engine, automated NLP resume parsing, skill gap analysis, and a visual job application Kanban tracking pipeline.

Designed for college final-year capstone projects, engineering portfolios, and job seekers.

---

## 🌟 Key Features

1. **AI Job Recommendation Engine**:
   - Transparent, mathematically sound scoring algorithm combining:
     - **60%**: Skill Overlap & TF-IDF Cosine Similarity (weighted by criticality)
     - **25%**: Experience Compatibility Curve
     - **15%**: Work Mode & Location Preference
   - Generates transparent explanations ("Why this job was recommended"), matching skills, and missing skills.
   - Zero random or fabricated match scores.

2. **Resume Analysis (PDF & DOCX)**:
   - Upload PDF or Word (.docx) resumes (up to 10MB).
   - Extracts text using **Apache PDFBox** & **Apache POI**.
   - NLP pattern matching against 150+ technology dictionary skills.
   - Detects years of experience and generates candidate profile summaries.
   - Automated enrichment into user profile skills.

3. **Skill Gap Analysis & Learning Roadmaps**:
   - Select any job role to evaluate skill readiness.
   - Identifies critical mandatory vs secondary missing skills.
   - Generates customized study roadmaps with core topics and curated resources.
   - Interactive progress slider and learning goal status tracking (`NOT_STARTED`, `IN_PROGRESS`, `COMPLETED`).

4. **Job Application Tracking System (ATS Pipeline)**:
   - Visual Kanban pipeline with stages:
     `Interested` ➔ `Applied` ➔ `Assessment` ➔ `Interview` ➔ `Offer` ➔ `Rejected` ➔ `Withdrawn`.
   - Track application dates, deadlines, scheduled interview date/time, salary offers, next steps, and recruiter notes.
   - Table view and Kanban board view toggles.

5. **Explore & Saved Jobs**:
   - Full-text search by title, company, location, or skills.
   - Multi-parameter filtering by work mode (Remote, Hybrid, Onsite), employment type, and max experience.
   - Sorting by newest or highest salary.
   - Transparent "Demo Job" labeling.

6. **Secure User Authentication**:
   - Spring Security 6 with stateless **JWT (JSON Web Tokens)**.
   - BCrypt password hashing and user-level data authorization.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Frontend** | React 18, Vite, Lucide Icons, Modern CSS Design System |
| **Backend** | Java 21, Spring Boot 3.3.4, Spring Security 6, Spring Data JPA, Hibernate 6 |
| **Database** | MySQL 8.0 (Production) / H2 in-memory (Instant zero-config test profile) |
| **NLP & AI Engine** | Pure Java TF-IDF Vector Space, Cosine Similarity, Apache PDFBox, Apache POI |
| **Security** | JWT (`jjwt 0.12.5`), BCrypt Password Encoder, CORS configuration |

---

## 📁 Project Structure

```
AI-Job-Recommendation-System/
├── backend/
│   ├── pom.xml
│   ├── src/
│   │   ├── main/java/com/jobrec/
│   │   │   ├── config/          # SecurityConfig, JwtAuthenticationFilter, DatabaseInitializer
│   │   │   ├── controller/      # REST Controllers (Auth, Jobs, Recs, Resume, SkillGap, Applications, Profile)
│   │   │   ├── dto/             # Request & Response Data Transfer Objects
│   │   │   ├── entity/          # JPA Entities (User, Skill, Job, JobSkill, JobApplication, SavedJob, etc.)
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── service/         # Business logic & AI Recommendation Engine
│   │   │   └── util/            # SkillDictionary & TfIdfVectorEngine
│   │   ├── main/resources/
│   │   │   ├── application.properties      # MySQL configuration (supports env vars)
│   │   │   └── application-h2.properties   # Instant zero-config fallback profile
│   │   └── test/java/com/jobrec/           # Unit & integration tests
├── frontend/
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── api/                 # Axios HTTP client & API services
│       ├── context/             # AuthContext (JWT session management)
│       ├── components/          # Reusable UI widgets, Modals, Kanban, Badges
│       ├── pages/               # 8 full dashboard views + Login & Register
│       └── styles/              # Modern CSS variables and styles
├── database/
│   └── schema.sql               # MySQL DDL schema and realistic seed data
└── README.md
```

---

## 🚀 Beginner Quick-Start Guide

### Prerequisites
Make sure you have installed on your computer:
- **Java 21+** (`java -version`)
- **Maven 3.9+** (`mvn -v`)
- **Node.js 18+** (`node -v`)
- **MySQL Server 8.0+** (Optional if using H2 zero-config mode)

---

### Step 1: Database Setup

#### Option A: Running with MySQL (Recommended for Submission)
1. Open MySQL command line or MySQL Workbench:
   ```bash
   mysql -u root -p
   ```
2. Run the initialization script:
   ```sql
   source C:/Users/SUSRITHA/Desktop/AI-Job-Recommendation-System/database/schema.sql;
   ```
3. Set your MySQL root password in an environment variable or edit `backend/src/main/resources/application.properties`:
   ```bash
   # In Windows PowerShell:
   $env:SPRING_DATASOURCE_PASSWORD="your_mysql_password"
   ```

#### Option B: Instant Zero-Config Run with H2
If you do not want to configure MySQL passwords right away, you can run the backend with the built-in H2 profile. The system will automatically create tables and seed realistic sample jobs and skills!
Add `-Dspring-boot.run.profiles=h2` to the maven run command.

---

### Step 2: Run the Backend

In a terminal, navigate to the `backend` directory and run:

```bash
cd backend

# If using MySQL:
mvn spring-boot:run

# Or if using H2 zero-config:
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
```

The backend will boot and show:
`Tomcat started on port 8080 (http) with context path '/'`

You can verify the backend is running by opening:
[http://localhost:8080/api/jobs](http://localhost:8080/api/jobs)

---

### Step 3: Run the Frontend

Open a **second terminal window**, navigate to `frontend`, and start the development server:

```bash
cd frontend
npm install
npm run dev
```

Open your browser at:
👉 **[http://localhost:5173](http://localhost:5173)**

---

## 🔑 Demo Account Credentials

A pre-populated demo user is available out of the box:

- **Email**: `demo@example.com`
- **Password**: `Password@123`

> **Tip**: On the login page, simply click the **"⚡ Auto-Fill Demo Credentials (Alex Morgan)"** button to log in with 1 click!

---

## 🧪 Running Automated Tests

To run the backend test suite (testing recommendation algorithms, TF-IDF cosine similarity, and NLP skill extraction):

```bash
cd backend
mvn test
```

Expected output:
```
[INFO] Running com.jobrec.service.RecommendationEngineTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.jobrec.util.TfIdfVectorEngineTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

To test the frontend production build:
```bash
cd frontend
npm run build
```

---

## 📡 REST API Reference

| Endpoint | Method | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `/api/auth/register` | `POST` | Register a new user | No |
| `/api/auth/login` | `POST` | Authenticate and obtain JWT token | No |
| `/api/auth/me` | `GET` | Get current user profile | Yes |
| `/api/jobs` | `GET` | Search and filter jobs | No |
| `/api/jobs/{id}` | `GET` | Get job details | No |
| `/api/jobs/{id}/save` | `POST` | Save/Bookmark job | Yes |
| `/api/jobs/{id}/save` | `DELETE` | Unsave job | Yes |
| `/api/jobs/saved` | `GET` | List saved jobs | Yes |
| `/api/recommendations` | `GET` | Get AI job recommendations | Yes |
| `/api/recommendations/job/{id}` | `GET` | Evaluate single job match | Yes |
| `/api/resume/upload` | `POST` | Upload PDF/DOCX resume & extract skills | Yes |
| `/api/resume/latest` | `GET` | Get latest resume analysis | Yes |
| `/api/skill-gap/{jobId}` | `GET` | Get skill gap & learning roadmap | Yes |
| `/api/skill-gap/goal` | `POST` | Save/update learning goal progress | Yes |
| `/api/applications` | `GET` | List tracked job applications | Yes |
| `/api/applications/job/{jobId}` | `POST` | Track new job application | Yes |
| `/api/applications/{id}` | `PATCH` | Update pipeline stage, interview, notes | Yes |
| `/api/applications/{id}` | `DELETE` | Delete application | Yes |
| `/api/dashboard/overview` | `GET` | Aggregated dashboard stats & pipeline counts | Yes |

---

## 🛡️ License & Project Credits
Developed for academic presentation and software engineering portfolio demonstration.
Built with clean architecture, enterprise patterns, and mathematically verified AI scoring algorithms.
