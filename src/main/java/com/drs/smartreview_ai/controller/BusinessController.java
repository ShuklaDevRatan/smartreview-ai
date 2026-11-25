package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.dto.BusinessRequest;
import com.drs.smartreview_ai.dto.BusinessResponse;
import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.enums.BusinessStatus;
import com.drs.smartreview_ai.service.BusinessService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    // Create Business
    @PostMapping
    public ResponseEntity<BusinessResponse> createBusiness(
            @Valid @RequestBody BusinessRequest request,
            @AuthenticationPrincipal User user) {

        BusinessResponse response =
                businessService.createBusiness(request, user.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get Business By ID
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponse> getBusinessById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                businessService.getBusinessById(id, user.getId())
        );
    }

    // Get All Businesses
    @GetMapping
    public ResponseEntity<List<BusinessResponse>> getAllBusinesses(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                businessService.getAllBusinesses(user.getId())
        );
    }

    // Update Business
    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponse> updateBusiness(
            @PathVariable Long id,
            @Valid @RequestBody BusinessRequest request,
            @AuthenticationPrincipal User user) {

        BusinessResponse response =
                businessService.updateBusiness(id, user.getId(), request);

        return ResponseEntity.ok(response);
    }

    // Update Business Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<BusinessResponse> updateBusinessStatus(
            @PathVariable Long id,
            @RequestParam BusinessStatus status,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                businessService.updateBusinessStatus(
                        id, user.getId(), status
                )
        );
    }
}