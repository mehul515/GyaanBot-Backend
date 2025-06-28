package com.gyaanbot.chatservice.utils;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PromptBuilder {

    private static final String SYSTEM_PROMPT =
            "<|system|>\nYou are a helpful AI assistant. Answer the user's question using only the provided course content. " +
                    "If the answer isn't in the content, say \"I don't know\".\n";

    public String buildPrompt(Map<String, String> docTextMap, String question) {
        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append("\n<|user|>\nHere is the course content:\n---\n");

        for (Map.Entry<String, String> entry : docTextMap.entrySet()) {
            userPrompt.append("📝 ").append(entry.getKey()).append(":\n");
            userPrompt.append(entry.getValue()).append("\n---\n");
        }

        userPrompt.append("\nQuestion: ").append(question).append("\n\n<|assistant|>");

        return SYSTEM_PROMPT + userPrompt;
    }

}

