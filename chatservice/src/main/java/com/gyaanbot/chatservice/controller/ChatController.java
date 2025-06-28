package com.gyaanbot.chatservice.controller;

import com.gyaanbot.chatservice.client.CourseClient;
import com.gyaanbot.chatservice.dto.ChatRequest;
import com.gyaanbot.chatservice.dto.ChatResponse;
import com.gyaanbot.chatservice.security.JwtUserDetails;
import com.gyaanbot.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private CourseClient courseClient;

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        JwtUserDetails user = (JwtUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String response = chatService.answerQuestion(
                user.getUserId(), user.getRole(), Long.parseLong(request.getCourseId()), request.getQuestion()
        );

        return ResponseEntity.ok(new ChatResponse(response));
    }

}
