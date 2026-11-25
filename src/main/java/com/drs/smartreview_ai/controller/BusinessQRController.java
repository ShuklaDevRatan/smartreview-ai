package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.service.BusinessQRService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessQRController {

    private final BusinessQRService businessQRService;

    @GetMapping("/{businessId}/qr/download")
    public ResponseEntity<byte[]> downloadBusinessQR(
            @PathVariable Long businessId,
            @AuthenticationPrincipal User user) {

        byte[] qrImage = businessQRService.downloadBusinessQR(
                businessId,
                user.getId()
        );

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=business-" + businessId + "-qr.png"
                )
                .body(qrImage);
    }
}