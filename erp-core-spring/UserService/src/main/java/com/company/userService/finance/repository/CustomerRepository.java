package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.Customer;
import com.company.erp.erp.entites.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCode(String code);
    List<Customer> findByCompany(Company company);
    List<Customer> findByCompanyAndIsActive(Company company, Boolean isActive);
}
