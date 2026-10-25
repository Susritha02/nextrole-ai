package com.jobrec.controller;

import com.jobrec.dto.ResumeAnalysisResponse;
import com.jobrec.entity.User;
import com.jobrec.service.AuthService;
import com.jobrec.service.ResumeParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeParserService resumeParserService;
    private final AuthService authService;

    public ResumeController(ResumeParserService resumeParserService, AuthService authService) {
        this.resumeParserService = resumeParserService;
        this.authService = authService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeAnalysisResponse> uploadResume(@RequestParam("file") MultipartFile file) throws Exception {
        User user = authService.getCurrentAuthenticatedUser();
        ResumeAnalysisResponse response = resumeParserService.parseAndAnalyzeResume(file, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    public ResponseEntity<ResumeAnalysisResponse> getLatestResumeAnalysis() {
        User user = authService.getCurrentAuthenticatedUser();
        return resumeParserService.getLatestAnalysis(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
