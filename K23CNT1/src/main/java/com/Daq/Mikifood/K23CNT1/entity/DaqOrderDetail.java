package com.Daq.Mikifood.K23CNT1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaqOrderDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private DaqOrder order;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private DaqProduct product;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}
