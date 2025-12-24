package com.Daq.Mikifood.K23CNT1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaqOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private DaqStaff staff;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "total_money", precision = 12, scale = 2)
    private BigDecimal totalMoney;
    
    @Column(name = "status", length = 50)
    private String status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<DaqOrderDetail> orderDetails;
}
