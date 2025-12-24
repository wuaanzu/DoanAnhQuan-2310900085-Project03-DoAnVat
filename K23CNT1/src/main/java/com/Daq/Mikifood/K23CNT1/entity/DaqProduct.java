package com.Daq.Mikifood.K23CNT1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaqProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "image", length = 255)
    private String image;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private DaqCategory category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<DaqOrderDetail> orderDetails;
}
