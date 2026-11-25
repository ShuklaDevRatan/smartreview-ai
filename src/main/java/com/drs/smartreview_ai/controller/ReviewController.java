package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.dto.ReviewSuggestionResponse;
import com.drs.smartreview_ai.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://10.76.125.212:5173"
})
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{qrCode}/suggestions")
    public ResponseEntity<ReviewSuggestionResponse> generateSuggestions(
            @PathVariable String qrCode) {

        // QR code ke basis par review suggestions generate kar rahe hain
        ReviewSuggestionResponse response =
                reviewService.generateReviewSuggestions(qrCode);

        return ResponseEntity.ok(response);
    }
}