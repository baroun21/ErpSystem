package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.status = :status ORDER BY q.quoteDate DESC")
    List<Quotation> findByCompanyAndStatus(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.lead.id = :leadId ORDER BY q.quoteDate DESC")
    List<Quotation> findByCompanyAndLead(@Param("companyId") Long companyId, @Param("leadId") Long leadId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.status = 'DRAFT' ORDER BY q.quoteDate")
    List<Quotation> findDraftQuotes(@Param("companyId") Long companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.expiryDate < CURRENT_DATE AND q.status NOT IN ('CONVERTED_TO_ORDER', 'REJECTED')")
    List<Quotation> findExpiredQuotes(@Param("companyId") Long companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.quoteDate BETWEEN :startDate AND :endDate ORDER BY q.quoteDate DESC")
    List<Quotation> findByDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(q.totalAmount) FROM Quotation q WHERE q.companyId = :companyId AND q.status = 'ACCEPTED'")
    BigDecimal calculateAcceptedQuotesValue(@Param("companyId") Long companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.approvedBy IS NOT NULL ORDER BY q.approvalDate DESC")
    List<Quotation> findApprovedQuotes(@Param("companyId") Long companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.convertedToSalesOrderId IS NULL AND q.status = 'ACCEPTED' ORDER BY q.quoteDate")
    List<Quotation> findConvertibleQuotes(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(q) FROM Quotation q WHERE q.companyId = :companyId AND q.status = :status")
    Long countByStatus(@Param("companyId") Long companyId, @Param("status") String status);
}
