package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long> {

    @Query("SELECT c FROM ChartOfAccount c WHERE c.companyId = :companyId AND c.accountCode = :code")
    Optional<ChartOfAccount> findByCodeInCompany(@Param("companyId") Long companyId, @Param("code") String code);

    @Query("SELECT c FROM ChartOfAccount c WHERE c.companyId = :companyId ORDER BY c.accountCode")
    List<ChartOfAccount> findAllInCompany(@Param("companyId") Long companyId);
}
