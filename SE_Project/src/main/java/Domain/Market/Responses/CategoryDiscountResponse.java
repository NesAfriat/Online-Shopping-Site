package Domain.Market.Responses;

import Domain.Market.DiscountPolicies.CategoryDiscount;

public class CategoryDiscountResponse extends DiscountResponse{

    private String category;
    private double percentage;

    public CategoryDiscountResponse(int shopId, CategoryDiscount discount) {
        super(shopId, discount);
        this.category = discount.getCategory();
        this.percentage = discount.getPercentage();
    }

    public String getCategory() {
        return category;
    }

    public double getPercentage() {
        return percentage;
    }
}
