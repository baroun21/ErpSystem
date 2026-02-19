package com.company.erp.erp.Dtos.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderDTO {
    private Long salesOrderId;
    private String companyId;
    private String orderNumber;
    private String customerId;
    private String customerName;
    private LocalDateTime orderDate;
    private LocalDateTime requiredDeliveryDate;
    private LocalDateTime promisedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private String shippingAddress;
    private String billingAddress;
    private Long referenceOpportunityId;
    private String notes;
    private List<SalesOrderLineDTO> orderLines;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
