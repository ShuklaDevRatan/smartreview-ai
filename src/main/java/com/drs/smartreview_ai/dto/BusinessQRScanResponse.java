package com.drs.smartreview_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessQRScanResponse {
    private String businessName;
    private String category;
    private String description;
    private String address;
    private String googleReviewUrl;
    private Long totalScans;
}
