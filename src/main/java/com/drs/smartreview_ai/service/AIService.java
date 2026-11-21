package com.drs.smartreview_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chatClient;

    public AIService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generateResponse(String prompt) {

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
