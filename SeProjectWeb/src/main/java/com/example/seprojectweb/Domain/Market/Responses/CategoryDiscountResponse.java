package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.DiscountPolicies.CategoryDiscount;

public class CategoryDiscountResponse extends DiscountResponse {

    private final String category;
    private final double percentage;

    public CategoryDiscountResponse(int shopId, CategoryDiscount discount) {
        super(shopId, discount);
        this.category = discount.getCategory();
        this.percentage = discount.getPercentage();
        this.discountType = "category discount - "+category;
    }

    public String getCategory() {
        return category;
    }

    public double getPercentage() {
        return percentage;
    }
}
