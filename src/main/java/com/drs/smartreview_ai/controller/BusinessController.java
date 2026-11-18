package com.drs.smartreview_ai.controller;

import com.drs.smartreview_ai.dto.BusinessRequest;
import com.drs.smartreview_ai.dto.BusinessResponse;
import com.drs.smartreview_ai.service.BusinessService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<BusinessResponse> createBusiness(
            @Valid @RequestBody BusinessRequest request,
            @RequestParam Long ownerId) {

        BusinessResponse response =
                businessService.createBusiness(request, ownerId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponse> getBusinessById(
            @PathVariable Long id,
            @RequestParam Long ownerId) {

        return ResponseEntity.ok(
                businessService.getBusinessById(id, ownerId)
        );
    }

    @GetMapping
    public ResponseEntity<java.util.List<BusinessResponse>> getAllBusinesses(
            @RequestParam Long ownerId) {

        return ResponseEntity.ok(
                businessService.getAllBusinesses(ownerId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponse> updateBusiness(
            @PathVariable Long id,
            @RequestParam Long ownerId,
            @Valid @RequestBody BusinessRequest request) {

        BusinessResponse response =
                businessService.updateBusiness(id, ownerId, request);

        return ResponseEntity.ok(response);
    }

    
}