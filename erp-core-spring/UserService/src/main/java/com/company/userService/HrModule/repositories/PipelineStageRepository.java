package com.company.userService.HrModule.repositories;

import com.company.erp.erp.entites.sales.PipelineStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PipelineStageRepository extends JpaRepository<PipelineStage, Long> {

    @Query("SELECT p FROM PipelineStage p WHERE p.companyId = :companyId AND p.active = true ORDER BY p.stageOrder")
    List<PipelineStage> findActiveStages(@Param("companyId") Long companyId);

    @Query("SELECT p FROM PipelineStage p WHERE p.companyId = :companyId ORDER BY p.stageOrder")
    List<PipelineStage> findByCompanyOrderByStage(@Param("companyId") Long companyId);

    @Query("SELECT p FROM PipelineStage p WHERE p.companyId = :companyId AND p.stageName = :stageName")
    PipelineStage findByCompanyAndName(@Param("companyId") Long companyId, @Param("stageName") String stageName);

    @Query("SELECT p FROM PipelineStage p WHERE p.companyId = :companyId AND p.stageOrder = (SELECT MAX(p2.stageOrder) FROM PipelineStage p2 WHERE p2.companyId = :companyId)")
    PipelineStage findFinalStage(@Param("companyId") Long companyId);

    @Query("SELECT p FROM PipelineStage p WHERE p.companyId = :companyId AND p.conversionProbability >= :minProbability ORDER BY p.stageOrder")
    List<PipelineStage> findStagesByMinProbability(@Param("companyId") Long companyId, @Param("minProbability") Integer minProbability);
}
