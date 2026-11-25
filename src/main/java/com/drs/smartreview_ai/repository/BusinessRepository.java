package com.drs.smartreview_ai.repository;

import java.util.List;
import java.util.Optional;
import com.drs.smartreview_ai.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByIdAndOwnerId(Long id, Long ownerId);
    List<Business> findAllByOwner_Id(Long ownerId);
}