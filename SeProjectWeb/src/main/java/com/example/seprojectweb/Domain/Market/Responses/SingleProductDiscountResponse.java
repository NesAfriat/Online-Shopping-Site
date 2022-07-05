package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.DiscountPolicies.SingleProductDiscount;

public class SingleProductDiscountResponse extends DiscountResponse {
    private final ProductResponse product;
    private final double percentage;

    public SingleProductDiscountResponse(int shopId, SingleProductDiscount discount) {
        super(shopId, discount);
        this.product = new ProductResponse(shopId, discount.getProduct());
        this.percentage = discount.getPercentage();
        this.discountType = "single product - " + product.getProductName();
    }

    public ProductResponse getProduct() {
        return product;
    }

    public double getPercentage() {
        return percentage;
    }
}
