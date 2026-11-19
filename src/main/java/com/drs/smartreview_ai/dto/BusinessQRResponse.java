package com.drs.smartreview_ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BusinessQRResponse {

    private Long id;
    private String qrCode;
    private String qrUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
}