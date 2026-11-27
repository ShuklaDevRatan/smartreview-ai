package com.drs.smartreview_ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRequest {

    @NotBlank(message = "Business name is required")
    @Size(max = 150, message = "Business name cannot exceed 150 characters")
    private String name;

    @NotBlank(message = "Business category is required")
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Pattern(
            regexp = "^[0-9+()\\-\\s]{7,15}$",
            message = "Invalid contact number"
    )
    private String contactNumber;

    @Size(max = 500, message = "Google Review URL cannot exceed 500 characters")
    @Pattern(
            regexp = "^https://.*$",
            message = "Google Review URL must use HTTPS"
    )
    private String googleReviewUrl;
}