package com.gyaanbot.chatservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HuggingFaceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${huggingface.api.url}")
    private String huggingFaceApiUrl;

    @Value("${huggingface.api.token}")
    private String huggingFaceApiToken;

    public String getAnswerFromModel(String prompt) {


        Map<String, Object> request = Map.of(
                "inputs", prompt,
                "parameters", Map.of(
                        "temperature", 0.7,
                        "max_new_tokens", 300
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(huggingFaceApiToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    huggingFaceApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            String body = response.getBody();
            System.out.println("Raw Bot Response: " + body);

            if (body == null) return "❌ No response body.";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);
            JsonNode textNode = root.get(0).get("generated_text");

            if (textNode == null) return "❌ 'generated_text' not found.";

            String fullText = textNode.asText();

            // ✅ Extract only what comes after <|assistant|>
            String[] parts = fullText.split("<\\|assistant\\|>");
            if (parts.length > 1) {
                return parts[1].trim();
            }

            return "❌ Assistant response not found.";

        } catch (Exception e) {
            return "[Error contacting model: " + e.getMessage() + "]";
        }
    }

}
