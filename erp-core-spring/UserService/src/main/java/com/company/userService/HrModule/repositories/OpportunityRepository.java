package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.status = :status ORDER BY o.opportunityValue DESC")
    List<Opportunity> findByCompanyAndStatus(@Param("companyId") Long companyId, @Param("status") String status);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.pipelineStage.id = :stageId ORDER BY o.probabilityPercentage DESC")
    List<Opportunity> findByCompanyAndStage(@Param("companyId") Long companyId, @Param("stageId") Long stageId);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.lead.id = :leadId ORDER BY o.createdAt DESC")
    List<Opportunity> findByCompanyAndLead(@Param("companyId") Long companyId, @Param("leadId") Long leadId);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.expectedCloseDate BETWEEN :startDate AND :endDate ORDER BY o.expectedCloseDate")
    List<Opportunity> findByCloseDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.status = 'OPEN' AND (o.opportunityValue * o.probabilityPercentage / 100.0) > 0 ORDER BY (o.opportunityValue * o.probabilityPercentage / 100.0) DESC")
    List<Opportunity> findForecasting(@Param("companyId") Long companyId);

    @Query("SELECT SUM(o.opportunityValue * o.probabilityPercentage / 100.0) FROM Opportunity o WHERE o.companyId = :companyId AND o.status = 'OPEN'")
    BigDecimal calculateRevenueForecast(@Param("companyId") Long companyId);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.probabilityPercentage >= :minProbability ORDER BY o.opportunityValue DESC")
    List<Opportunity> findHighProbabilityOpportunities(@Param("companyId") Long companyId, @Param("minProbability") Integer minProbability);

    @Query("SELECT COUNT(o) FROM Opportunity o WHERE o.companyId = :companyId AND o.status = 'OPEN'")
    Long countOpenOpportunities(@Param("companyId") Long companyId);
}
