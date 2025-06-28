package com.gyaanbot.chatservice.dto;

public class ChatRequest {
    private String courseId;
    private String question;

    public ChatRequest(String courseId, String question) {
        this.courseId = courseId;
        this.question = question;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
