package com.drs.smartreview_ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRequest {

    @NotBlank(message = "Business name is required")
    private String name;

    @NotBlank(message = "Business category is required")
    private String category;

    private String description;

    private String address;

    private String contactNumber;

    private String googleReviewUrl;
}