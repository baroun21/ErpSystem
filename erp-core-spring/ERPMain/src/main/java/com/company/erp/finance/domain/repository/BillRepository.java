package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE b.companyId = :companyId AND b.billNumber = :number")
    Optional<Bill> findByNumberInCompany(@Param("companyId") Long companyId, @Param("number") String number);

    @Query("SELECT b FROM Bill b WHERE b.companyId = :companyId ORDER BY b.billDate DESC")
    List<Bill> findAllInCompany(@Param("companyId") Long companyId);

    @Query("SELECT b FROM Bill b WHERE b.companyId = :companyId AND b.dueDate BETWEEN :startDate AND :endDate " +
           "AND (b.status IS NULL OR b.status <> 'PAID') ORDER BY b.dueDate ASC")
    List<Bill> findUpcomingPayments(@Param("companyId") Long companyId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);
}
