package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.status = :status ORDER BY o.opportunityValue DESC")
    List<Opportunity> findByCompanyAndStatus(@Param("companyId") String companyId, @Param("status") String status);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.pipelineStage.stageId = :stageId ORDER BY o.probabilityPercentage DESC")
    List<Opportunity> findByCompanyAndStage(@Param("companyId") String companyId, @Param("stageId") Long stageId);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.lead.leadId = :leadId ORDER BY o.createdAt DESC")
    List<Opportunity> findByCompanyAndLead(@Param("companyId") String companyId, @Param("leadId") Long leadId);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.expectedCloseDate BETWEEN :startDate AND :endDate ORDER BY o.expectedCloseDate")
    List<Opportunity> findByCloseDateRange(@Param("companyId") String companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.status = 'OPEN' AND (o.opportunityValue * o.probabilityPercentage / 100.0) > 0 ORDER BY (o.opportunityValue * o.probabilityPercentage / 100.0) DESC")
    List<Opportunity> findForecasting(@Param("companyId") String companyId);

    @Query("SELECT SUM(o.opportunityValue * o.probabilityPercentage / 100.0) FROM Opportunity o WHERE o.companyId = :companyId AND o.status = 'OPEN'")
    BigDecimal calculateRevenueForecast(@Param("companyId") String companyId);

    @Query("SELECT o FROM Opportunity o WHERE o.companyId = :companyId AND o.probabilityPercentage >= :minProbability ORDER BY o.opportunityValue DESC")
    List<Opportunity> findHighProbabilityOpportunities(@Param("companyId") String companyId, @Param("minProbability") Integer minProbability);

    @Query("SELECT COUNT(o) FROM Opportunity o WHERE o.companyId = :companyId AND o.status = 'OPEN'")
    Long countOpenOpportunities(@Param("companyId") String companyId);
}
