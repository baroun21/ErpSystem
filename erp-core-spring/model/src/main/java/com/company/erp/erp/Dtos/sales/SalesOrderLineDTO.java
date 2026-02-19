package com.company.erp.erp.Dtos.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderLineDTO {
    private Long lineId;
    private String companyId;
    private Long salesOrderId;
    private Integer lineNumber;
    private String productId;
    private String productName;
    private String productSku;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;
    private String promisedDeliveryDate;
    private BigDecimal deliveredQuantity;
    private String notes;
}
