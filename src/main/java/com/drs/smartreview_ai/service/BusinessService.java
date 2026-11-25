package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.dto.BusinessQRResponse;
import com.drs.smartreview_ai.dto.BusinessRequest;
import com.drs.smartreview_ai.dto.BusinessResponse;
import com.drs.smartreview_ai.entity.Business;
import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.enums.BusinessStatus;
import com.drs.smartreview_ai.exception.ResourceNotFoundException;
import com.drs.smartreview_ai.repository.BusinessRepository;
import com.drs.smartreview_ai.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessQRService businessQRService;

    public BusinessService(
            BusinessRepository businessRepository,
            UserRepository userRepository,
            BusinessQRService businessQRService) {

        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.businessQRService = businessQRService;
    }

    @Transactional
    public BusinessResponse createBusiness(
            BusinessRequest request,
            Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Business business = new Business();

        business.setName(request.getName());
        business.setCategory(request.getCategory());
        business.setDescription(request.getDescription());
        business.setAddress(request.getAddress());
        business.setContactNumber(request.getContactNumber());
        business.setGoogleReviewUrl(request.getGoogleReviewUrl());
        business.setStatus(BusinessStatus.ACTIVE);
        business.setOwner(owner);

        Business savedBusiness = businessRepository.save(business);

        BusinessQRResponse qrResponse =
                businessQRService.createBusinessQR(savedBusiness);

        BusinessResponse response =
                mapToBusinessResponse(savedBusiness);

        response.setQr(qrResponse);

        return response;
    }

    public BusinessResponse getBusinessById(
            Long businessId,
            Long ownerId) {

        Business business = businessRepository
                .findByIdAndOwnerId(businessId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        return mapToBusinessResponse(business);
    }

    public List<BusinessResponse> getAllBusinesses(Long ownerId) {

        return businessRepository.findAllByOwner_Id(ownerId)
                .stream()
                .map(this::mapToBusinessResponse)
                .toList();
    }

    @Transactional
    public BusinessResponse updateBusiness(
            Long businessId,
            Long ownerId,
            BusinessRequest request) {

        Business business = businessRepository
                .findByIdAndOwnerId(businessId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        business.setName(request.getName());
        business.setCategory(request.getCategory());
        business.setDescription(request.getDescription());
        business.setAddress(request.getAddress());
        business.setContactNumber(request.getContactNumber());
        business.setGoogleReviewUrl(request.getGoogleReviewUrl());

        Business updatedBusiness = businessRepository.save(business);

        return mapToBusinessResponse(updatedBusiness);
    }

    @Transactional
    public BusinessResponse updateBusinessStatus(
            Long businessId,
            Long ownerId,
            BusinessStatus status) {

        Business business = businessRepository
                .findByIdAndOwnerId(businessId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        business.setStatus(status);

        Business updatedBusiness = businessRepository.save(business);

        return mapToBusinessResponse(updatedBusiness);
    }

    private BusinessResponse mapToBusinessResponse(Business business) {

        BusinessResponse response = new BusinessResponse();

        response.setId(business.getId());
        response.setName(business.getName());
        response.setCategory(business.getCategory());
        response.setDescription(business.getDescription());
        response.setAddress(business.getAddress());
        response.setContactNumber(business.getContactNumber());
        response.setGoogleReviewUrl(business.getGoogleReviewUrl());
        response.setStatus(business.getStatus());
        response.setOwnerId(business.getOwner().getId());

        return response;
    }
}