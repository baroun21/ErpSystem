package com.company.erp.finance.domain.repository;

import com.company.erp.finance.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.companyId = :companyId ORDER BY c.name")
    List<Customer> findAllInCompany(@Param("companyId") Long companyId);
}
