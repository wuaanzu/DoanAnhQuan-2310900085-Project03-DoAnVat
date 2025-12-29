package com.Daq.Mikifood.K23CNT1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String image;

    // Tính thành tiền
    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }
}
