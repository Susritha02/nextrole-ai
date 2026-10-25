package com.jobrec.util;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TfIdfVectorEngineTest {

    @Test
    void testTokenizeFiltersStopwords() {
        String text = "The quick brown fox is jumping over the lazy dog";
        List<String> tokens = TfIdfVectorEngine.tokenize(text);
        assertFalse(tokens.contains("the"));
        assertFalse(tokens.contains("is"));
        assertFalse(tokens.contains("over"));
        assertTrue(tokens.contains("quick"));
        assertTrue(tokens.contains("brown"));
        assertTrue(tokens.contains("fox"));
    }

    @Test
    void testCosineSimilarityIdenticalDocs() {
        String doc1 = "Java Spring Boot Microservices REST APIs MySQL";
        String doc2 = "Java Spring Boot Microservices REST APIs MySQL";
        double sim = TfIdfVectorEngine.computeCosineSimilarity(doc1, doc2);
        assertTrue(sim > 0.95, "Identical documents should yield near 1.0 cosine similarity");
    }

    @Test
    void testCosineSimilarityDifferentDocs() {
        String doc1 = "Java Spring Boot backend developer";
        String doc2 = "Cooking pasta with tomato sauce and basil";
        double sim = TfIdfVectorEngine.computeCosineSimilarity(doc1, doc2);
        assertEquals(0.0, sim, 0.01, "Completely disjoint documents should yield 0.0 similarity");
    }

    @Test
    void testSkillExtraction() {
        String resume = "Experienced software engineer skilled in Java, Spring Boot, React, and MySQL database.";
        List<String> skills = SkillDictionary.extractSkillsFromText(resume);
        assertTrue(skills.contains("Java"));
        assertTrue(skills.contains("Spring Boot"));
        assertTrue(skills.contains("React"));
        assertTrue(skills.contains("MySQL"));
        assertFalse(skills.contains("Python"));
    }
}
