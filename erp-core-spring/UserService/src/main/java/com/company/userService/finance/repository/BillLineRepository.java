package com.company.userService.finance.repository;

import com.company.erp.erp.entites.finance.BillLine;
import com.company.erp.erp.entites.finance.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillLineRepository extends JpaRepository<BillLine, Long> {
    List<BillLine> findByBill(Bill bill);
}
