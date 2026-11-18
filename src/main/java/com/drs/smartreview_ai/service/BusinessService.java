package com.drs.smartreview_ai.service;

import com.drs.smartreview_ai.dto.BusinessRequest;
import com.drs.smartreview_ai.dto.BusinessResponse;
import com.drs.smartreview_ai.entity.Business;
import com.drs.smartreview_ai.entity.User;
import com.drs.smartreview_ai.enums.BusinessStatus;
import com.drs.smartreview_ai.exception.ResourceNotFoundException;
import com.drs.smartreview_ai.repository.BusinessRepository;
import com.drs.smartreview_ai.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public BusinessService(
            BusinessRepository businessRepository,
            UserRepository userRepository) {

        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    public BusinessResponse createBusiness(BusinessRequest request, Long ownerId) {

        // Find business owner
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create Business entity
        Business business = new Business();

        business.setName(request.getName());
        business.setCategory(request.getCategory());
        business.setDescription(request.getDescription());
        business.setAddress(request.getAddress());
        business.setContactNumber(request.getContactNumber());
        business.setGoogleReviewUrl(request.getGoogleReviewUrl());

        business.setStatus(BusinessStatus.ACTIVE);
        business.setOwner(owner);

        // Save business
        Business savedBusiness = businessRepository.save(business);

        // Convert entity into response DTO
        return mapToBusinessResponse(savedBusiness);
    }

    public BusinessResponse getBusinessById(Long businessId, Long ownerId) {

        Business business = businessRepository
                .findByIdAndOwnerId(businessId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        return mapToBusinessResponse(business);
    }

    public List<BusinessResponse> getAllBusinesses(Long ownerId) {

        return businessRepository.findAll()
                .stream()
                .filter(business -> business.getOwner().getId().equals(ownerId))
                .map(this::mapToBusinessResponse)
                .toList();
    }

    public BusinessResponse updateBusiness(
            Long businessId,
            Long ownerId,
            BusinessRequest request) {

        Business business = businessRepository
                .findByIdAndOwnerId(businessId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        business.setName(request.getName());
        business.setCategory(request.getCategory());
        business.setDescription(request.getDescription());
        business.setAddress(request.getAddress());
        business.setContactNumber(request.getContactNumber());
        business.setGoogleReviewUrl(request.getGoogleReviewUrl());

        Business updatedBusiness = businessRepository.save(business);

        return mapToBusinessResponse(updatedBusiness);
    }

    public BusinessResponse updateBusinessStatus(
            Long businessId,
            Long ownerId,
            BusinessStatus status) {

        Business business = businessRepository
                .findByIdAndOwnerId(businessId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

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