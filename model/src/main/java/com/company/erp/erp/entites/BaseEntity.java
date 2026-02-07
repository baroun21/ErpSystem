package com.company.erp.erp.entites;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
// This definition allows us to auto-filter "WHERE company_id = ?" later
@FilterDef(name = "companyFilter", parameters = @ParamDef(name = "companyId", type = Long.class))
@Filter(name = "companyFilter", condition = "COMPANY_ID = :companyId")
@EntityListeners(TenantEntityListener.class) // Hooks up the auto-set logic
public abstract class BaseEntity {

    @Column(name = "COMPANY_ID", nullable = false, updatable = false)
    private Long companyId;
}