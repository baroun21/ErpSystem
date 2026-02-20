package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.status = :status ORDER BY q.quoteDate DESC")
    List<Quotation> findByCompanyAndStatus(@Param("companyId") String companyId, @Param("status") String status);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.lead.leadId = :leadId ORDER BY q.quoteDate DESC")
    List<Quotation> findByCompanyAndLead(@Param("companyId") String companyId, @Param("leadId") Long leadId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.status = 'DRAFT' ORDER BY q.quoteDate")
    List<Quotation> findDraftQuotes(@Param("companyId") String companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.expiryDate < CURRENT_DATE AND q.status NOT IN ('CONVERTED_TO_ORDER', 'REJECTED')")
    List<Quotation> findExpiredQuotes(@Param("companyId") String companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.quoteDate BETWEEN :startDate AND :endDate ORDER BY q.quoteDate DESC")
    List<Quotation> findByDateRange(@Param("companyId") String companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(q.totalAmount) FROM Quotation q WHERE q.companyId = :companyId AND q.status = 'ACCEPTED'")
    BigDecimal calculateAcceptedQuotesValue(@Param("companyId") String companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.approvedBy IS NOT NULL ORDER BY q.approvalDate DESC")
    List<Quotation> findApprovedQuotes(@Param("companyId") String companyId);

    @Query("SELECT q FROM Quotation q WHERE q.companyId = :companyId AND q.convertedToSalesOrderId IS NULL AND q.status = 'ACCEPTED' ORDER BY q.quoteDate")
    List<Quotation> findConvertibleQuotes(@Param("companyId") String companyId);

    @Query("SELECT COUNT(q) FROM Quotation q WHERE q.companyId = :companyId AND q.status = :status")
    Long countByStatus(@Param("companyId") String companyId, @Param("status") String status);
}
