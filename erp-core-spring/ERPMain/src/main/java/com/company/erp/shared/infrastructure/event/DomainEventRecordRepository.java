package com.company.erp.shared.infrastructure.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomainEventRecordRepository extends JpaRepository<DomainEventRecord, Long> {

    List<DomainEventRecord> findByCompanyIdOrderByOccurredAtDesc(Long companyId);
}

