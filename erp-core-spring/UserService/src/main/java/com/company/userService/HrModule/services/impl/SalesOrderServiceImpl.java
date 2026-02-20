package com.company.userService.HrModule.services.impl;

import com.company.erp.erp.Dtos.sales.SalesOrderDTO;
import com.company.erp.erp.entites.sales.SalesOrder;
import com.company.erp.mapper.sales.SalesOrderMapper;
import com.company.userService.HrModule.repositories.SalesOrderRepository;
import com.company.userService.HrModule.services.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderMapper salesOrderMapper;

    @Override
    public SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO) {
        log.info("Creating sales order for company: {}", salesOrderDTO.getCompanyId());
        SalesOrder order = salesOrderMapper.toEntity(salesOrderDTO);
        order.setCreatedAt(LocalDateTime.now());
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        if (order.getPaymentStatus() == null) {
            order.setPaymentStatus("UNPAID");
        }
        SalesOrder saved = salesOrderRepository.save(order);
        return salesOrderMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalesOrderDTO> getSalesOrderById(String companyId, Long orderId) {
        return salesOrderRepository.findById(orderId)
            .filter(order -> order.getCompanyId().equals(companyId))
            .map(salesOrderMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getSalesOrdersByStatus(String companyId, String status) {
        log.info("Fetching sales orders for company: {} with status: {}", companyId, status);
        return salesOrderRepository.findByCompanyAndStatus(companyId, status)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getSalesOrdersByCustomer(String companyId, String customerId) {
        log.info("Fetching sales orders for company: {} and customer: {}", companyId, customerId);
        return salesOrderRepository.findByCompanyAndCustomer(companyId, customerId)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getSalesOrdersByDateRange(String companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.findByDateRange(companyId, startDate, endDate)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getOverdueOrders(String companyId) {
        log.info("Fetching overdue orders for company: {}", companyId);
        return salesOrderRepository.findOverdueOrders(companyId)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getUnpaidOrders(String companyId) {
        log.info("Fetching unpaid orders for company: {}", companyId);
        return salesOrderRepository.findUnpaidOrders(companyId)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getHighValueOrders(String companyId, BigDecimal minAmount) {
        return salesOrderRepository.findHighValueOrders(companyId, minAmount)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }

    @Override
    public SalesOrderDTO updateSalesOrder(String companyId, Long orderId, SalesOrderDTO salesOrderDTO) {
        log.info("Updating sales order: {} for company: {}", orderId, companyId);
        SalesOrder order = salesOrderRepository.findById(orderId)
            .filter(o -> o.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Sales order not found or access denied"));
        
        salesOrderMapper.updateEntityFromDTO(salesOrderDTO, order);
        order.setUpdatedAt(LocalDateTime.now());
        SalesOrder updated = salesOrderRepository.save(order);
        return salesOrderMapper.toDTO(updated);
    }

    @Override
    public SalesOrderDTO confirmOrder(String companyId, Long orderId) {
        log.info("Confirming sales order: {}", orderId);
        SalesOrder order = salesOrderRepository.findById(orderId)
            .filter(o -> o.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Sales order not found or access denied"));
        
        order.setStatus("CONFIRMED");
        order.setUpdatedAt(LocalDateTime.now());
        SalesOrder updated = salesOrderRepository.save(order);
        return salesOrderMapper.toDTO(updated);
    }

    @Override
    public SalesOrderDTO shipOrder(String companyId, Long orderId) {
        log.info("Shipping sales order: {}", orderId);
        SalesOrder order = salesOrderRepository.findById(orderId)
            .filter(o -> o.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Sales order not found or access denied"));
        
        order.setStatus("SHIPPED");
        order.setUpdatedAt(LocalDateTime.now());
        SalesOrder updated = salesOrderRepository.save(order);
        return salesOrderMapper.toDTO(updated);
    }

    @Override
    public SalesOrderDTO deliverOrder(String companyId, Long orderId) {
        log.info("Delivering sales order: {}", orderId);
        SalesOrder order = salesOrderRepository.findById(orderId)
            .filter(o -> o.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Sales order not found or access denied"));
        
        order.setStatus("DELIVERED");
        order.setActualDeliveryDate(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        SalesOrder updated = salesOrderRepository.save(order);
        return salesOrderMapper.toDTO(updated);
    }

    @Override
    public SalesOrderDTO cancelOrder(String companyId, Long orderId) {
        log.info("Cancelling sales order: {}", orderId);
        SalesOrder order = salesOrderRepository.findById(orderId)
            .filter(o -> o.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Sales order not found or access denied"));
        
        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());
        SalesOrder updated = salesOrderRepository.save(order);
        return salesOrderMapper.toDTO(updated);
    }

    @Override
    public void deleteSalesOrder(String companyId, Long orderId) {
        log.info("Deleting sales order: {} for company: {}", orderId, companyId);
        SalesOrder order = salesOrderRepository.findById(orderId)
            .filter(o -> o.getCompanyId().equals(companyId))
            .orElseThrow(() -> new RuntimeException("Sales order not found or access denied"));
        salesOrderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateCustomerLifetimeValue(String companyId, String customerId) {
        log.info("Calculating lifetime value for customer: {}", customerId);
        return salesOrderRepository.calculateCustomerLifetimeValue(companyId, customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateRevenue(String companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.calculateRevenue(companyId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countCustomerOrders(String companyId, String customerId) {
        return salesOrderRepository.countCustomerOrders(companyId, customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderDTO> getOrdersByOpportunity(String companyId, Long opportunityId) {
        return salesOrderRepository.findByOpportunity(companyId, opportunityId)
            .stream()
            .map(salesOrderMapper::toDTO)
            .toList();
    }
}
