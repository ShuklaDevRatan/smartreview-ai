package com.drs.smartreview_ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewSuggestionResponse {

    private String businessName;

    private List<String> suggestions;

    private String googleReviewUrl;
}