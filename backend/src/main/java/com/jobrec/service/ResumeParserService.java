package com.jobrec.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobrec.dto.ResumeAnalysisResponse;
import com.jobrec.entity.ResumeAnalysis;
import com.jobrec.entity.Skill;
import com.jobrec.entity.User;
import com.jobrec.entity.UserSkill;
import com.jobrec.repository.ResumeAnalysisRepository;
import com.jobrec.repository.SkillRepository;
import com.jobrec.repository.UserSkillRepository;
import com.jobrec.util.SkillDictionary;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ResumeParserService {

    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final ObjectMapper objectMapper;

    public ResumeParserService(ResumeAnalysisRepository resumeAnalysisRepository,
                               SkillRepository skillRepository,
                               UserSkillRepository userSkillRepository,
                               ObjectMapper objectMapper) {
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ResumeAnalysisResponse parseAndAnalyzeResume(MultipartFile file, User user) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File name cannot be determined");
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        String extractedText;
        if ("pdf".equals(extension)) {
            extractedText = extractTextFromPdf(file.getInputStream());
        } else if ("docx".equals(extension)) {
            extractedText = extractTextFromDocx(file.getInputStream());
        } else if ("txt".equals(extension)) {
            extractedText = new String(file.getBytes());
        } else {
            throw new IllegalArgumentException("Unsupported file type. Please upload a PDF or DOCX file.");
        }

        if (extractedText.isBlank()) {
            throw new IllegalArgumentException("Could not extract any readable text from the uploaded document.");
        }

        // 1. Detect skills via SkillDictionary
        List<String> detectedSkills = SkillDictionary.extractSkillsFromText(extractedText);

        // 2. Detect experience years via regex
        double detectedExperience = extractExperienceYears(extractedText);

        // 3. Generate summary
        String summary = generateProfileSummary(extractedText, detectedSkills, detectedExperience);

        // 4. Save ResumeAnalysis entity
        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setUser(user);
        analysis.setFileName(originalFilename);
        analysis.setParsedText(extractedText.length() > 10000 ? extractedText.substring(0, 10000) : extractedText);
        analysis.setDetectedSkillsJson(objectMapper.writeValueAsString(detectedSkills));
        analysis.setDetectedExperienceYears(detectedExperience);
        analysis.setProfileSummary(summary);
        analysis = resumeAnalysisRepository.save(analysis);

        // 5. Automatically enrich UserSkills (if not already present)
        for (String skillName : detectedSkills) {
            Skill skill = skillRepository.findByNameIgnoreCase(skillName).orElseGet(() ->
                    skillRepository.save(new Skill(skillName, "General"))
            );
            if (userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId()).isEmpty()) {
                userSkillRepository.save(new UserSkill(user, skill, "INTERMEDIATE"));
            }
        }

        // Return DTO
        ResumeAnalysisResponse response = new ResumeAnalysisResponse();
        response.setId(analysis.getId());
        response.setFileName(analysis.getFileName());
        response.setDetectedSkills(detectedSkills);
        response.setDetectedExperienceYears(detectedExperience);
        response.setProfileSummary(summary);
        response.setAnalyzedAt(analysis.getAnalyzedAt());
        return response;
    }

    public Optional<ResumeAnalysisResponse> getLatestAnalysis(User user) {
        return resumeAnalysisRepository.findFirstByUserIdOrderByAnalyzedAtDesc(user.getId())
                .map(this::toResponse);
    }

    private String extractTextFromPdf(InputStream inputStream) throws Exception {
        byte[] bytes = inputStream.readAllBytes();
        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDocx(InputStream inputStream) throws Exception {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph p : document.getParagraphs()) {
                sb.append(p.getText()).append("\n");
            }
            return sb.toString();
        }
    }

    private double extractExperienceYears(String text) {
        // Look for patterns like "3+ years", "2.5 years of experience", "4 yrs experience"
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\+?\\s*(years?|yrs?)\\b(\\s+of\\s+experience)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        double maxYears = 0.0;
        while (matcher.find()) {
            try {
                double val = Double.parseDouble(matcher.group(1));
                if (val > maxYears && val <= 40.0) { // Reasonable upper bound
                    maxYears = val;
                }
            } catch (NumberFormatException ignored) {}
        }
        return maxYears > 0 ? maxYears : 1.0; // Default to 1.0 if not detected
    }

    private String generateProfileSummary(String text, List<String> skills, double expYears) {
        StringBuilder sb = new StringBuilder();
        if (expYears > 0) {
            sb.append("Experienced candidate with approximately ").append(expYears).append(" year(s) in technical roles. ");
        } else {
            sb.append("Early-career candidate or recent graduate. ");
        }

        if (!skills.isEmpty()) {
            sb.append("Strong competencies detected in ").append(skills.stream().limit(6).collect(Collectors.joining(", ")));
            if (skills.size() > 6) {
                sb.append(", and ").append(skills.size() - 6).append(" additional technologies.");
            } else {
                sb.append(".");
            }
        } else {
            sb.append("General computing and software development skills identified.");
        }
        return sb.toString();
    }

    private ResumeAnalysisResponse toResponse(ResumeAnalysis analysis) {
        ResumeAnalysisResponse resp = new ResumeAnalysisResponse();
        resp.setId(analysis.getId());
        resp.setFileName(analysis.getFileName());
        resp.setDetectedExperienceYears(analysis.getDetectedExperienceYears());
        resp.setProfileSummary(analysis.getProfileSummary());
        resp.setAnalyzedAt(analysis.getAnalyzedAt());

        try {
            List<String> skills = objectMapper.readValue(analysis.getDetectedSkillsJson(), new TypeReference<List<String>>() {});
            resp.setDetectedSkills(skills);
        } catch (Exception e) {
            resp.setDetectedSkills(Collections.emptyList());
        }
        return resp;
    }
}
