package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.DiscountPolicies.XorDiscount;

public class XorDiscountResponse extends DiscountResponse {

    private final DiscountResponse discount1;
    private final DiscountResponse discount2;

    public XorDiscountResponse(int shopId, XorDiscount discount) {
        super(shopId, discount);
        this.discount1 = DiscountResponse.buildDiscountResponse(shopId, discount.getDiscount1());
        this.discount2 = DiscountResponse.buildDiscountResponse(shopId, discount.getDiscount2());
        this.discountType = discount1.getDiscountType() + " or " + discount2.getDiscountType();
    }
}
