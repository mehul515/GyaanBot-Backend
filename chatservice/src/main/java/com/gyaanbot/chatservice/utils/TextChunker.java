package com.gyaanbot.chatservice.utils;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    public static List<String> chunkText(String text, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();

        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            String chunk = text.substring(start, end).trim();

            chunks.add(chunk);

            start += (chunkSize - overlap);
        }

        return chunks;
    }
}
