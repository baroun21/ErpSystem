package com.company.erp.erp.entites;


import com.company.erp.erp.enums.BonusType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "BONUSES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(TenantEntityListener.class)
public class Bonus extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "bonus_seq", sequenceName = "BONUSES_SEQ", allocationSize = 1), generator = "bonus_seq"
    @Column(name = "BONUS_ID")
    private Long bonusId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "BONUS_TYPE", length = 50)
    private BonusType bonusType;


    @Column(name = "AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "DATE_GRANTED")
    private LocalDate dateGranted;  //, insertable = false
}
