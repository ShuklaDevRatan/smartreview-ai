package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.exception.AIResponseException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chatClient;

    public AIService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generateResponse(String prompt) {

        try {
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            if (response == null || response.isBlank()) {
                throw new AIResponseException(
                        "AI service returned an empty response."
                );
            }

            return response;

        } catch (AIResponseException exception) {
            throw exception;

        } catch (Exception exception) {
            throw new AIResponseException(
                    "Unable to generate review suggestions right now. Please try again.",
                    exception
            );
        }
    }
}