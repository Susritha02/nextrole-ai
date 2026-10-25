package com.jobrec.util;

import java.util.*;

public class SkillDictionary {

    public static class SkillMeta {
        private final String canonicalName;
        private final String category;
        private final List<String> aliases;
        private final List<String> topics;
        private final List<String> resources;

        public SkillMeta(String canonicalName, String category, List<String> aliases, List<String> topics, List<String> resources) {
            this.canonicalName = canonicalName;
            this.category = category;
            this.aliases = aliases;
            this.topics = topics;
            this.resources = resources;
        }

        public String getCanonicalName() { return canonicalName; }
        public String getCategory() { return category; }
        public List<String> getAliases() { return aliases; }
        public List<String> getTopics() { return topics; }
        public List<String> getResources() { return resources; }
    }

    private static final Map<String, SkillMeta> DICTIONARY = new LinkedHashMap<>();

    static {
        register("Java", "Languages",
                List.of("java", "core java", "java 17", "java 21", "jvm"),
                List.of("OOP Fundamentals", "Collections Framework", "Streams & Lambdas", "Concurrency & Multithreading", "JVM Internals"),
                List.of("Oracle Java Tutorials (docs.oracle.com)", "Baeldung Core Java Guides", "Java Brains YouTube Course"));

        register("Python", "Languages",
                List.of("python", "python3", "py"),
                List.of("Syntax & Data Structures", "Functional Programming & Decorators", "Virtual Environments", "AsyncIO", "OOP in Python"),
                List.of("Official Python Docs (python.org)", "Corey Schafer Python Series", "Real Python Tutorials"));

        register("JavaScript", "Languages",
                List.of("javascript", "js", "ecmascript", "es6", "es6+"),
                List.of("ES6+ Syntax", "Promises & Async/Await", "DOM Manipulation", "Event Loop & Closures", "Modern Modules"),
                List.of("MDN Web Docs JavaScript", "JavaScript.info", "Namaste JavaScript by Akshay Saini"));

        register("TypeScript", "Languages",
                List.of("typescript", "ts"),
                List.of("Static Types & Interfaces", "Generics", "Type Narrowing & Unions", "TSConfig Setup", "React with TypeScript"),
                List.of("Official TypeScript Handbook", "Total TypeScript by Matt Pocock"));

        register("React", "Frameworks",
                List.of("react", "react.js", "reactjs"),
                List.of("JSX & Components", "Hooks (useState, useEffect, useMemo)", "State Management (Context/Redux)", "React Router", "Performance Optimization"),
                List.of("React Official Documentation (react.dev)", "freeCodeCamp React Course", "Jack Herrington React Tutorials"));

        register("Spring Boot", "Frameworks",
                List.of("spring boot", "springboot", "spring framework", "spring"),
                List.of("Spring Core & IoC Container", "REST Controllers & Validation", "Spring Data JPA & Hibernate", "Spring Security & JWT", "Microservices Architecture"),
                List.of("Spring.io Official Guides", "Baeldung Spring Boot Tutorials", "Amigoscode Spring Boot Masterclass"));

        register("Node.js", "Frameworks",
                List.of("node", "node.js", "nodejs"),
                List.of("Node Runtime & Event Loop", "Express Framework", "Buffer & Streams", "Authentication & Middleware", "NPM Ecosystem"),
                List.of("Node.js Official Documentation", "The Odin Project Node Course"));

        register("SQL", "Databases",
                List.of("sql", "structured query language", "relational database"),
                List.of("SELECT Queries & Joins", "Aggregations & Grouping", "Subqueries & CTEs", "Indexes & Performance Tuning", "Transactions & ACID"),
                List.of("SQLZoo Interactive Tutorial", "Mode Analytics SQL School", "W3Schools SQL"));

        register("MySQL", "Databases",
                List.of("mysql", "my-sql"),
                List.of("Schema Design & Normalization", "Foreign Keys & Constraints", "Query Optimization & EXPLAIN", "Stored Procedures & Triggers"),
                List.of("MySQL Reference Manual", "Brad Traversy MySQL Crash Course"));

        register("PostgreSQL", "Databases",
                List.of("postgresql", "postgres", "psql"),
                List.of("Data Types & JSONB", "Complex Joins & Window Functions", "Connection Pooling", "Indexing (B-Tree, GIN)"),
                List.of("PostgreSQL Official Documentation", "Postgresqltutorial.com"));

        register("MongoDB", "Databases",
                List.of("mongodb", "mongo", "nosql"),
                List.of("Document Model & Collections", "CRUD Operations", "Aggregation Pipeline", "Mongoose ODM"),
                List.of("MongoDB University (learn.mongodb.com)", "freeCodeCamp MongoDB Guide"));

        register("Docker", "Cloud & DevOps",
                List.of("docker", "container", "containers", "containerization"),
                List.of("Dockerfile Best Practices", "Images & Registries", "Docker Compose Multi-Container", "Volumes & Networking"),
                List.of("Docker Official Get Started Docs", "TechWorld with Nana Docker Course"));

        register("Kubernetes", "Cloud & DevOps",
                List.of("kubernetes", "k8s"),
                List.of("Pods, Deployments, Services", "ConfigMaps & Secrets", "Ingress Controllers", "Helm Charts"),
                List.of("Kubernetes.io Documentation", "Mumshad Mannambeth Kubernetes for Beginners"));

        register("AWS", "Cloud & DevOps",
                List.of("aws", "amazon web services", "ec2", "s3", "lambda"),
                List.of("EC2, S3, RDS Basics", "IAM & CloudWatch", "Serverless Lambda & API Gateway", "VPC & Security Groups"),
                List.of("AWS Skill Builder", "Stephane Maarek AWS Certified Cloud Practitioner"));

        register("Git", "Cloud & DevOps",
                List.of("git", "github", "version control", "gitlab"),
                List.of("Branching & Merging", "Resolving Conflicts", "Git Flow Workflow", "Rebase & Stash"),
                List.of("Pro Git Book (git-scm.com/book)", "GitHub Skills Interactive"));

        register("RESTful APIs", "Architecture",
                List.of("rest", "restful", "rest api", "restful api", "rest apis", "restful apis"),
                List.of("HTTP Methods & Status Codes", "Resource Naming Conventions", "Idempotence & Caching", "API Versioning & OpenAPI/Swagger"),
                List.of("Restfulapi.net Best Practices", "RFC 7231 HTTP Semantics"));

        register("Microservices", "Architecture",
                List.of("microservices", "microservice architecture", "distributed systems"),
                List.of("Decomposition Patterns", "API Gateway & Service Discovery", "Event-Driven Architecture", "Circuit Breakers & Resilience"),
                List.of("Microservices.io by Chris Richardson", "Martin Fowler Microservices Guide"));

        register("Machine Learning", "AI & Data",
                List.of("machine learning", "ml", "supervised learning", "scikit-learn"),
                List.of("Regression & Classification", "Feature Engineering", "Model Evaluation & Cross-Validation", "Random Forest & Gradient Boosting"),
                List.of("Andrew Ng Machine Learning Specialization", "Scikit-Learn Documentation"));

        register("Natural Language Processing", "AI & Data",
                List.of("nlp", "natural language processing", "text mining", "nltk", "spacy"),
                List.of("Tokenization & Lemmatization", "TF-IDF & Embeddings", "Transformers & HuggingFace", "Named Entity Recognition"),
                List.of("Hugging Face NLP Course", "Stanford CS224N NLP Course"));

        register("PyTorch", "AI & Data",
                List.of("pytorch", "torch"),
                List.of("Tensors & Autograd", "Neural Network Modules", "Training Loops & Optimizers", "Transfer Learning"),
                List.of("PyTorch Official Tutorials (pytorch.org/tutorials)", "Deep Learning with PyTorch Book"));

        register("FastAPI", "Frameworks",
                List.of("fastapi", "fast api"),
                List.of("Pydantic Schemas", "Dependency Injection", "Async Endpoints", "Automatic OpenAPI Docs"),
                List.of("FastAPI Official Documentation (tiangolo.com)", "TestDriven.io FastAPI Guide"));

        register("HTML5", "Languages",
                List.of("html", "html5", "semantic html"),
                List.of("Semantic Elements", "Forms & Input Types", "Accessibility (ARIA)", "Web Storage API"),
                List.of("MDN Web Docs HTML", "W3C Web Standards"));

        register("CSS3", "Languages",
                List.of("css", "css3", "sass", "scss", "tailwind"),
                List.of("Box Model & Selectors", "Flexbox & CSS Grid", "Responsive Design & Media Queries", "Transitions & Keyframe Animations"),
                List.of("Kevin Powell CSS Channel", "CSS-Tricks Flexbox & Grid Guides"));

        register("CI/CD", "Cloud & DevOps",
                List.of("ci/cd", "continuous integration", "continuous deployment", "jenkins", "github actions"),
                List.of("Automated Testing in Pipeline", "Build Artifacts & Versioning", "Deployment Automation", "Secret Management in CI"),
                List.of("GitHub Actions Documentation", "GitLab CI/CD Guide"));

        register("Unit Testing", "Methodology",
                List.of("unit test", "unit testing", "junit", "mockito", "jest", "pytest"),
                List.of("Test Driven Development (TDD)", "Mocking & Stubs", "Assertions & Edge Cases", "Code Coverage Analysis"),
                List.of("JUnit 5 User Guide", "Martin Fowler Practical Test Pyramid"));
    }

    private static void register(String name, String category, List<String> aliases, List<String> topics, List<String> resources) {
        DICTIONARY.put(name.toLowerCase(), new SkillMeta(name, category, aliases, topics, resources));
    }

    public static Collection<SkillMeta> getAllSkills() {
        return DICTIONARY.values();
    }

    public static Optional<SkillMeta> findSkill(String input) {
        if (input == null) return Optional.empty();
        String lower = input.trim().toLowerCase();
        if (DICTIONARY.containsKey(lower)) {
            return Optional.of(DICTIONARY.get(lower));
        }
        for (SkillMeta meta : DICTIONARY.values()) {
            if (meta.getCanonicalName().equalsIgnoreCase(lower)) return Optional.of(meta);
            for (String alias : meta.getAliases()) {
                if (alias.equalsIgnoreCase(lower)) return Optional.of(meta);
            }
        }
        return Optional.empty();
    }

    public static List<String> extractSkillsFromText(String rawText) {
        if (rawText == null || rawText.isBlank()) return Collections.emptyList();
        String text = " " + rawText.toLowerCase().replaceAll("[^a-z0-9#+.]", " ") + " ";
        Set<String> matched = new LinkedHashSet<>();

        for (SkillMeta meta : DICTIONARY.values()) {
            for (String alias : meta.getAliases()) {
                // Ensure boundary matching so "java" doesn't match "javascript" incorrectly
                String pattern = "(?i).*\\b" + java.util.regex.Pattern.quote(alias.toLowerCase()) + "\\b.*";
                if (text.matches(pattern)) {
                    matched.add(meta.getCanonicalName());
                    break;
                }
            }
        }
        return new ArrayList<>(matched);
    }
}
