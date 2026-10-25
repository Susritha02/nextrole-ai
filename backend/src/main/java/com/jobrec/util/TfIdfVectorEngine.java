package com.jobrec.util;

import java.util.*;

public class TfIdfVectorEngine {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are",
            "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but",
            "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from",
            "further", "had", "has", "have", "having", "he", "her", "here", "hers", "herself", "him",
            "himself", "his", "how", "i", "if", "in", "into", "is", "it", "its", "itself", "just",
            "me", "more", "most", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only",
            "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she",
            "should", "so", "some", "such", "than", "that", "the", "their", "theirs", "them",
            "themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too",
            "under", "until", "up", "very", "was", "we", "were", "what", "when", "where", "which",
            "while", "who", "whom", "why", "with", "would", "you", "your", "yours", "yourself", "yourselves"
    ));

    public static List<String> tokenize(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9#+.]", " ");
        String[] tokens = cleaned.split("\\s+");
        List<String> result = new ArrayList<>();
        for (String t : tokens) {
            String token = t.trim();
            if (token.length() > 1 && !STOP_WORDS.contains(token)) {
                result.add(token);
            }
        }
        return result;
    }

    public static Map<String, Double> computeTf(List<String> tokens) {
        Map<String, Double> tf = new HashMap<>();
        if (tokens.isEmpty()) return tf;
        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0.0) + 1.0);
        }
        int total = tokens.size();
        for (Map.Entry<String, Double> entry : tf.entrySet()) {
            entry.setValue(entry.getValue() / total);
        }
        return tf;
    }

    public static double computeCosineSimilarity(String doc1, String doc2) {
        List<String> tokens1 = tokenize(doc1);
        List<String> tokens2 = tokenize(doc2);
        if (tokens1.isEmpty() || tokens2.isEmpty()) return 0.0;

        Map<String, Double> tf1 = computeTf(tokens1);
        Map<String, Double> tf2 = computeTf(tokens2);

        Set<String> allTokens = new HashSet<>();
        allTokens.addAll(tf1.keySet());
        allTokens.addAll(tf2.keySet());

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (String token : allTokens) {
            double v1 = tf1.getOrDefault(token, 0.0);
            double v2 = tf2.getOrDefault(token, 0.0);

            int df = (tf1.containsKey(token) ? 1 : 0) + (tf2.containsKey(token) ? 1 : 0);
            // Standard smoothed IDF: 1.0 + ln( (N + 1) / (df + 1) )
            double idf = 1.0 + Math.log(3.0 / (1.0 + df));

            double tfIdf1 = v1 * idf;
            double tfIdf2 = v2 * idf;

            dotProduct += tfIdf1 * tfIdf2;
            normA += tfIdf1 * tfIdf1;
            normB += tfIdf2 * tfIdf2;
        }

        if (normA == 0.0 || normB == 0.0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
