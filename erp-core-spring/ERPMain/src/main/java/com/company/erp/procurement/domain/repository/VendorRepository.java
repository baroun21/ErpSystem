package com.company.erp.procurement.domain.repository;

import com.company.erp.procurement.domain.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("SELECT v FROM Vendor v WHERE v.companyId = :companyId AND v.name = :name")
    Optional<Vendor> findByNameInCompany(@Param("companyId") Long companyId, @Param("name") String name);

    @Query("SELECT v FROM Vendor v WHERE v.companyId = :companyId ORDER BY v.name")
    List<Vendor> findAllInCompany(@Param("companyId") Long companyId);
}
