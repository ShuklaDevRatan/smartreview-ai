package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.exception.AIResponseException;
import com.drs.smartreview_ai.repository.BusinessQRRepository;
import org.springframework.stereotype.Service;
import com.drs.smartreview_ai.dto.ReviewSuggestionResponse;
import com.drs.smartreview_ai.entity.Business;
import com.drs.smartreview_ai.entity.BusinessQR;
import com.drs.smartreview_ai.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class ReviewService {

    private final BusinessQRRepository businessQRRepository;
    private final AIService aiService;

    public ReviewService(
            BusinessQRRepository businessQRRepository,
            AIService aiService) {

        this.businessQRRepository = businessQRRepository;
        this.aiService = aiService;
    }

    public ReviewSuggestionResponse generateReviewSuggestions(String qrCode) {

        // QR identifier se database mein QR find kar rahe hain
        BusinessQR businessQR = businessQRRepository.findWithBusinessByQrCode(qrCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("QR Code not found"));

        // Inactive QR ke liye suggestions generate nahi karenge
        if (!businessQR.getIsActive()) {
            throw new ResourceNotFoundException("QR Code is inactive");
        }

        // QR se associated business ki details nikal rahe hain
        Business business = businessQR.getBusiness();

        // Gemini ke liye business-specific prompt prepare kar rahe hain
        String prompt = """
        Generate exactly 5 natural and engaging Google review drafts
        for the following business.

        Business Name: %s
        Category: %s
        Description: %s

        Requirements:

        1. Generate exactly 2 reviews in English and 3 in Hinglish.
        2. Each review should contain approximately 25-40 words.
        3. Write in a natural, conversational human style.
        4. Reviews should sound like genuine customer-written text,
           not advertisements or promotional slogans.
        5. Give every review a different writing style and structure.
        6. Avoid repeating the business name unnecessarily.
        7. Avoid generic one-line statements.
        8. Do not invent personal experiences, product quality,
           pricing, staff behaviour or service claims.
        9. Use only the provided business information.
        10. Return only 5 reviews, one per line.
        11. Do not include numbering or explanations.
        """.formatted(
                business.getName(),
                business.getCategory(),
                business.getDescription()
        );

        // Gemini se suggestions generate karwa rahe hain
        String aiResponse = aiService.generateResponse(prompt);

        // Check kar rahe hain ki AI ne empty response toh nahi diya
        if (aiResponse == null || aiResponse.isBlank()) {
            throw new AIResponseException("AI returned an empty response");
        }

        // AI ke response ko individual lines mein divide kar rahe hain
        List<String> suggestions = aiResponse.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                // Numbering aur bullet symbols remove kar rahe hain
                .map(line -> line.replaceFirst("^(\\d+[.)]?\\s*|[-*•]\\s*)", ""))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();

        // Check kar rahe hain ki AI ne exactly 5 suggestions di hain
        if (suggestions.size() != 5) {
            throw new AIResponseException(
                    "AI returned an invalid number of suggestions"
            );
        }



        // Response DTO prepare kar rahe hain
        ReviewSuggestionResponse response = new ReviewSuggestionResponse();

        response.setBusinessName(business.getName());
        response.setSuggestions(suggestions);
        response.setGoogleReviewUrl(business.getGoogleReviewUrl());

        return response;

    }
}