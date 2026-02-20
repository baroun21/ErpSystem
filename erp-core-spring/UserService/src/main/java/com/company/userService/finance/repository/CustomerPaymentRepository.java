package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.CustomerPayment;
import com.company.erp.erp.entites.finance.Company;
import com.company.erp.erp.entites.finance.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {
    Optional<CustomerPayment> findByPaymentNumber(String paymentNumber);
    List<CustomerPayment> findByCompany(Company company);
    List<CustomerPayment> findByCustomer(Customer customer);
    List<CustomerPayment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
}
