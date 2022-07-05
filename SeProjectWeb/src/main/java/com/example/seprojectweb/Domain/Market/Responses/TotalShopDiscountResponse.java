package com.example.seprojectweb.Domain.Market.Responses;


import com.example.seprojectweb.Domain.Market.DiscountPolicies.TotalShopDiscount;

public class TotalShopDiscountResponse extends DiscountResponse {

    private final double percentage;

    public TotalShopDiscountResponse(int shopId, TotalShopDiscount discount) {
        super(shopId, discount);
        this.percentage = discount.getPercentage();
        this.discountType = "whole shop discount";
    }

    public double getPercentage() {
        return percentage;
    }
}
