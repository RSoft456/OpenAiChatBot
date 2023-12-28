package com.example.chatbotopenai;
import java.util.List;

public class ApiRequest {
    private String model;
    private List<ChatMessage> messages;

    public ApiRequest(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

    public static class ChatMessage {
        private String role;
        private String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
