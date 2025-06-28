package com.gyaanbot.chatservice.utils;

import java.util.List;

public class SimilarityUtils {

    public static float cosineSimilarity(List<Float> a, List<Float> b) {
        float dot = 0f, normA = 0f, normB = 0f;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return (float) (dot / (Math.sqrt(normA) * Math.sqrt(normB)));
    }
}
