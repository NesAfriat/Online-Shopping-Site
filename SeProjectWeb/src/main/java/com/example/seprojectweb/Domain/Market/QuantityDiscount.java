package com.example.seprojectweb.Domain.Market;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class QuantityDiscount {
    private Integer quantity;
    @Transient
    private Double discount;

    public QuantityDiscount(Integer quantity, Double discount) {
        this.quantity = quantity;
        this.discount = discount;
    }

    public QuantityDiscount() {
        discount = 0.0;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
