package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.companyId = :companyId ORDER BY p.paymentDate DESC")
    List<Payment> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT p FROM Payment p WHERE p.companyId = :companyId AND p.status = :status")
    List<Payment> findByStatusInCompany(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT p FROM Payment p WHERE p.companyId = :companyId AND p.paymentNumber = :paymentNumber")
    Optional<Payment> findByPaymentNumberInCompany(@Param("companyId") Long companyId, @Param("paymentNumber") String paymentNumber);

    @Query("SELECT p FROM Payment p WHERE p.companyId = :companyId AND p.invoiceId = :invoiceId")
    List<Payment> findByInvoiceInCompany(@Param("companyId") Long companyId, @Param("invoiceId") Long invoiceId);

    @Query("SELECT p FROM Payment p WHERE p.companyId = :companyId AND p.billId = :billId")
    List<Payment> findByBillInCompany(@Param("companyId") Long companyId, @Param("billId") Long billId);

    @Query("SELECT p FROM Payment p WHERE p.companyId = :companyId AND p.paymentDate BETWEEN :startDate AND :endDate ORDER BY p.paymentDate DESC")
    List<Payment> findByDateRangeInCompany(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
