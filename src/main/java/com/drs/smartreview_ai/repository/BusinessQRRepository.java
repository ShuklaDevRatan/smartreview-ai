package com.drs.smartreview_ai.repository;

import com.drs.smartreview_ai.entity.BusinessQR;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessQRRepository extends JpaRepository<BusinessQR, Long> {

    Optional<BusinessQR> findByBusinessId(Long businessId);

    Optional<BusinessQR> findByQrCode(String qrCode);

    Optional<BusinessQR> findByBusiness_IdAndBusiness_Owner_Id(
            Long businessId,
            Long ownerId
    );

    @EntityGraph(attributePaths = "business")
    Optional<BusinessQR> findWithBusinessByQrCode(String qrCode);
}