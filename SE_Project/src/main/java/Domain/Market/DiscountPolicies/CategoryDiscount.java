package Domain.Market.DiscountPolicies;

import Domain.InnerLogicException;
import Domain.Market.Product;
import Domain.Market.ShoppingBasket;
import javafx.util.Pair;

import java.util.Date;
import java.util.Map;

public class CategoryDiscount extends Discount{

    private String category;
    private double percentage;

    public CategoryDiscount(int discountId, double percentage, Date lastValidDate, String category) throws InnerLogicException {
        super(discountId, lastValidDate);
        this.category = category;
        verifyLegalPercentage(percentage);
        this.percentage=percentage;
    }


    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        if(checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, Pair<Integer, Double>> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                if (product.getCategory().equals(category)) {
                    shoppingBasket.updateProductDiscount(product, percentage);
                }
            }
        }
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        double output = 0;
        if(checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, Pair<Integer, Double>> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                if (product.getCategory().equals(category)) {
                    int quantity = entry.getValue().getKey();
                    output += Discount.calculateDiscountOnProduct(
                            product.getPrice(), quantity, entry.getValue().getValue(), percentage);
                }
            }
        }
        return output;
    }

    public String getCategory() {
        return category;
    }

    public double getPercentage() {
        return percentage;
    }
}
