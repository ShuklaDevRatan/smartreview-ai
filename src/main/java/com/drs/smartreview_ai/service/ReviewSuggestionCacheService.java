package com.drs.smartreview_ai.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ReviewSuggestionCacheService {

    private final AIService aiService;

    public ReviewSuggestionCacheService(AIService aiService) {
        this.aiService = aiService;
    }

    @Cacheable(
            cacheNames = "reviewSuggestions",
            key = "#qrCode",
            sync = true
    )
    public String generateCachedSuggestions(String qrCode, String prompt) {
        return aiService.generateResponse(prompt);
    }
}