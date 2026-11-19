package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.dto.BusinessQRScanResponse;
import com.drs.smartreview_ai.service.BusinessQRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicQRController {

    private final BusinessQRService businessQRService;

    public PublicQRController(BusinessQRService businessQRService) {
        this.businessQRService = businessQRService;
    }

    @GetMapping("/q/{qrCode}")
    public ResponseEntity<BusinessQRScanResponse> scanBusinessQR(
            @PathVariable String qrCode) {

        // QR scan request process karke business details return kar rahe hain
        BusinessQRScanResponse response =
                businessQRService.scanBusinessQR(qrCode);

        return ResponseEntity.ok(response);
    }
}