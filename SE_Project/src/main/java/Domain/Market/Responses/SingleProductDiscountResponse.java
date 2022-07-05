package Domain.Market.Responses;

import Domain.Market.DiscountPolicies.SingleProductDiscount;

public class SingleProductDiscountResponse extends DiscountResponse{
    private final ProductResponse product;
    private final double percentage;

    public SingleProductDiscountResponse(int shopId, SingleProductDiscount discount){
        super(shopId, discount);
        this.product = new ProductResponse(shopId, discount.getProduct());
        this.percentage = discount.getPercentage();
    }

    public ProductResponse getProduct() {
        return product;
    }

    public double getPercentage() {
        return percentage;
    }
}
