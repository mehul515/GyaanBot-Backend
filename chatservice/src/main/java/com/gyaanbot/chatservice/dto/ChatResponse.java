package com.gyaanbot.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ChatResponse {
    private String response;

    public ChatResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
