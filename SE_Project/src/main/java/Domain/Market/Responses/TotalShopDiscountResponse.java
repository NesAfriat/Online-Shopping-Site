package Domain.Market.Responses;


import Domain.Market.DiscountPolicies.TotalShopDiscount;

public class TotalShopDiscountResponse extends DiscountResponse{

    private final double percentage;

    public TotalShopDiscountResponse(int shopId, TotalShopDiscount discount) {
        super(shopId, discount);
        this.percentage = discount.getPercentage();
    }

    public double getPercentage() {
        return percentage;
    }
}
