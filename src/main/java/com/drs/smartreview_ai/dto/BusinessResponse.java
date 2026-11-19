package com.drs.smartreview_ai.dto;

import com.drs.smartreview_ai.enums.BusinessStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessResponse {

    private Long id;
    private String name;
    private String category;
    private String description;
    private String address;
    private String contactNumber;
    private String googleReviewUrl;
    private BusinessStatus status;
    private BusinessQRResponse qr;
    private Long ownerId;
}