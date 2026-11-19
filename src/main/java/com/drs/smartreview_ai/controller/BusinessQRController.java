package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.service.BusinessQRService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/businesses")
public class BusinessQRController {

    private final BusinessQRService businessQRService;

    public BusinessQRController(BusinessQRService businessQRService) {
        this.businessQRService = businessQRService;
    }

    @GetMapping("/{businessId}/qr/download")
    public ResponseEntity<byte[]> downloadBusinessQR(
            @PathVariable Long businessId,
            @RequestParam Long ownerId) {

        // Owner verify karke QR image generate kar rahe hain
        byte[] qrImage = businessQRService.downloadBusinessQR(businessId, ownerId);

        // PNG image ko download response mein return kar rahe hain
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=business-" + businessId + "-qr.png"
                )
                .body(qrImage);
    }
}