package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/test")
    public String testAI(@RequestParam String prompt) {

        // User ka prompt AI service ko bhej rahe hain
        return aiService.generateResponse(prompt);
    }
}