package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s WHERE s.companyId = :companyId ORDER BY s.name")
    List<Supplier> findAllInCompany(@Param("companyId") Long companyId);
}
