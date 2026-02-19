package com.company.erp.procurement.domain.repository;

import com.company.erp.procurement.domain.entity.Rfq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RfqRepository extends JpaRepository<Rfq, Long> {

    @Query("SELECT r FROM Rfq r WHERE r.companyId = :companyId AND r.rfqNumber = :number")
    Optional<Rfq> findByNumberInCompany(@Param("companyId") Long companyId, @Param("number") String number);

    @Query("SELECT r FROM Rfq r WHERE r.companyId = :companyId ORDER BY r.requestedDate DESC")
    List<Rfq> findAllInCompany(@Param("companyId") Long companyId);
}
