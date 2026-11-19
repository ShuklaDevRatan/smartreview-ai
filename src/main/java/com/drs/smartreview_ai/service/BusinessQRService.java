package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.dto.BusinessQRResponse;
import com.drs.smartreview_ai.dto.BusinessQRScanResponse;
import com.drs.smartreview_ai.entity.Business;
import com.drs.smartreview_ai.entity.BusinessQR;
import com.drs.smartreview_ai.exception.ResourceNotFoundException;
import com.drs.smartreview_ai.repository.BusinessQRRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BusinessQRService {

    @Value("${app.base-url}")
    private String baseUrl;

    private final BusinessQRRepository businessQRRepository;

    public BusinessQRService(BusinessQRRepository businessQRRepository) {
        this.businessQRRepository = businessQRRepository;
    }

    private String generateQrCode() {
        return UUID.randomUUID().toString();
    }

    public BusinessQRResponse createBusinessQR(Business business) {

        BusinessQR businessQR = new BusinessQR();

        // Unique QR identifier generate kar rahe hain
        businessQR.setQrCode(generateQrCode());

        businessQR.setBusiness(business);
        businessQR.setIsActive(true);
        businessQR.setCreatedAt(LocalDateTime.now());

        // QR details database mein save kar rahe hain
        BusinessQR savedQR = businessQRRepository.save(businessQR);

        // Saved entity ko DTO mein convert karke return kar rahe hain
        return mapToBusinessQRResponse(savedQR);
    }

    private String generateQrUrl(String qrCode) {

        return baseUrl + "/q/" + qrCode;

    }

    private BusinessQRResponse mapToBusinessQRResponse(BusinessQR businessQR) {

        BusinessQRResponse response = new BusinessQRResponse();

        // Entity se basic QR details DTO mein map kar rahe hain
        response.setId(businessQR.getId());
        response.setQrCode(businessQR.getQrCode());
        response.setIsActive(businessQR.getIsActive());
        response.setCreatedAt(businessQR.getCreatedAt());

        // QR URL dynamically generate kar rahe hain
        response.setQrUrl(generateQrUrl(businessQR.getQrCode()));

        return response;
    }




    private byte[] generateQrImage(String qrUrl) {
        try {
            // QRCodeWriter QR generate karne ke liye use hota hai
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // URL ko QR Code pattern (BitMatrix) mein convert kar rahe hain
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrUrl,
                    BarcodeFormat.QR_CODE,
                    300, // QR ki width
                    300  // QR ki height
            );
            // Image ko memory mein bytes ke form mein store karne ke liye
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // BitMatrix ko PNG image mein convert kar rahe hain
            MatrixToImageWriter.writeToStream(
                    bitMatrix,
                    "PNG",
                    outputStream
            );

            // Generated PNG image ke bytes return kar rahe hain
            return outputStream.toByteArray();

        } catch (WriterException | IOException exception) {

            // QR generate karte waqt error aaye toh exception throw karenge
            throw new RuntimeException("Failed to generate QR image", exception);
        }
    }

    public byte[] downloadBusinessQR(Long businessId, Long ownerId) {

        // Business aur owner ID ke basis par QR fetch kar rahe hain
        BusinessQR businessQR = businessQRRepository
                .findByBusiness_IdAndBusiness_Owner_Id(businessId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business QR not found"));

        // Existing QR identifier se URL generate kar rahe hain
        String qrUrl = generateQrUrl(businessQR.getQrCode());

        // URL ko PNG image mein convert karke return kar rahe hain
        return generateQrImage(qrUrl);
    }

    @Transactional
    public BusinessQRScanResponse scanBusinessQR(String qrCode) {

        // QR identifier se database mein QR search kar rahe hain
        BusinessQR businessQR = businessQRRepository.findByQrCode(qrCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("QR Code not found"));

        // Inactive QR ko scan karne ki permission nahi denge
        if (!businessQR.getIsActive()) {
            throw new ResourceNotFoundException("QR Code is inactive");
        }

        // Total scan count increase kar rahe hain
        businessQR.setScanCount(businessQR.getScanCount() + 1);

        businessQRRepository.save(businessQR);

        // Associated business ki details fetch kar rahe hain
        Business business = businessQR.getBusiness();

        // Business details ko response DTO mein map kar rahe hain
        BusinessQRScanResponse response = new BusinessQRScanResponse();

        response.setBusinessName(business.getName());
        response.setCategory(business.getCategory());
        response.setDescription(business.getDescription());
        response.setAddress(business.getAddress());
        response.setGoogleReviewUrl(business.getGoogleReviewUrl());
        response.setTotalScans(businessQR.getScanCount());

        return response;
    }
}