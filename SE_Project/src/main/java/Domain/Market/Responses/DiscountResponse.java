package Domain.Market.Responses;


import Domain.Market.DiscountPolicies.*;

import java.util.Date;

// TODO: implement all the discount responses, including abstract discount
public abstract class DiscountResponse {

    private final int discountId;

    private final String conditionDescription;
    private final Date lastValidDate;
    private final int shopId;
    protected DiscountResponse(int shopId, Discount discount) {
        this.shopId = shopId;
        this.discountId = discount.getId();
        this.lastValidDate = discount.getLastValidDate();
        this.conditionDescription = discount.getCondition().getDescription();
    }

    public static DiscountResponse buildDiscountResponse(int shopId, Discount discount){
        if(discount instanceof SingleProductDiscount){
            return new SingleProductDiscountResponse(shopId, (SingleProductDiscount)discount);
        } else if (discount instanceof CategoryDiscount) {
            return new CategoryDiscountResponse(shopId, (CategoryDiscount) discount);
        }else if (discount instanceof TotalShopDiscount) {
            return new TotalShopDiscountResponse(shopId, (TotalShopDiscount) discount);
        }else if (discount instanceof XorDiscount) {
            return new XorDiscountResponse(shopId, (XorDiscount)discount);
        }else return null;
    }

    public int getShopId() {
        return shopId;
    }
}
