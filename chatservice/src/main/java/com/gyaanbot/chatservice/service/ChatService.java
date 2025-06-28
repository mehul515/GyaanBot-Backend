package com.gyaanbot.chatservice.service;

import com.gyaanbot.chatservice.client.CourseClient;
import com.gyaanbot.chatservice.client.HuggingFaceClient;
import com.gyaanbot.chatservice.dto.CourseDocumentDTO;
import com.gyaanbot.chatservice.utils.PdfProcessor;
import com.gyaanbot.chatservice.utils.PromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private PdfProcessor pdfProcessor;

    @Autowired
    private PromptBuilder promptBuilder;

    @Autowired
    private HuggingFaceClient huggingFaceClient;

    public String answerQuestion(String userId, String role, Long courseId, String question) {
        // 1. Get documents for course
        List<CourseDocumentDTO> docs = courseClient.getDocuments(courseId);

        // 2. Extract text
        Map<String, String> extractedChunks = pdfProcessor.extractChunks(docs);

        // 3. Build prompt
        String prompt = promptBuilder.buildPrompt(extractedChunks, question);

        // 4. Call Hugging Face API
        return huggingFaceClient.getAnswerFromModel(prompt);
    }
}

