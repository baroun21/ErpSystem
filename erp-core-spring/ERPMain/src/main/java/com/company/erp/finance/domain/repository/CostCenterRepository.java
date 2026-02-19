package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long> {

    @Query("SELECT cc FROM CostCenter cc WHERE cc.companyId = :companyId")
    List<CostCenter> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT cc FROM CostCenter cc WHERE cc.companyId = :companyId AND cc.code = :code")
    Optional<CostCenter> findByCodeInCompany(@Param("companyId") Long companyId, @Param("code") String code);

    @Query("SELECT cc FROM CostCenter cc WHERE cc.companyId = :companyId AND cc.active = true")
    List<CostCenter> findActiveCostCentersInCompany(@Param("companyId") Long companyId);

    @Query("SELECT cc FROM CostCenter cc WHERE cc.companyId = :companyId AND cc.parentId = :parentId")
    List<CostCenter> findByParentInCompany(@Param("companyId") Long companyId, @Param("parentId") Long parentId);
}
