package com.example.seprojectweb.Domain.Market.Responses;


import com.example.seprojectweb.Domain.Market.DiscountPolicies.*;

import java.util.Date;

public abstract class DiscountResponse {

    private final int discountId;
    private final String conditionDescription;
    private final Date lastValidDate;
    private final int shopId;

    public String discountType;

    protected DiscountResponse(int shopId, Discount discount) {
        this.shopId = shopId;
        this.discountId = discount.getId();
        this.lastValidDate = discount.getLastValidDate();
        if(discount.getCondition()!=null){
            this.conditionDescription = discount.getCondition().getDescription();
        }else{
            this.conditionDescription = "no condition";
        }
        this.discountType = null;
    }

    public static DiscountResponse buildDiscountResponse(int shopId, Discount discount) {
        if (discount instanceof SingleProductDiscount) {
            return new SingleProductDiscountResponse(shopId, (SingleProductDiscount) discount);
        } else if (discount instanceof CategoryDiscount) {
            return new CategoryDiscountResponse(shopId, (CategoryDiscount) discount);
        } else if (discount instanceof TotalShopDiscount) {
            return new TotalShopDiscountResponse(shopId, (TotalShopDiscount) discount);
        } else if (discount instanceof XorDiscount) {
            return new XorDiscountResponse(shopId, (XorDiscount) discount);
        } else return null;
    }

    public int getShopId() {
        return shopId;
    }

    public int getDiscountId() {
        return discountId;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public Date getLastValidDate() {
        return lastValidDate;
    }

    protected String getDiscountType() {
        return discountType;
    }
}
