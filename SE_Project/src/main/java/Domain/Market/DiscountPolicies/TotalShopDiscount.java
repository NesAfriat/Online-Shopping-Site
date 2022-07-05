package Domain.Market.DiscountPolicies;

import Domain.InnerLogicException;
import Domain.Market.Product;
import Domain.Market.ShoppingBasket;
import javafx.util.Pair;

import java.util.Date;
import java.util.Map;

public class TotalShopDiscount extends Discount{
    private final double percentage;

    public TotalShopDiscount(int discountId, double percentage, Date lastValidDate) throws InnerLogicException {
        super(discountId, lastValidDate);
        verifyLegalPercentage(percentage);
        this.percentage=percentage;
    }



    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        if(checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, Pair<Integer, Double>> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                shoppingBasket.updateProductDiscount(product, percentage);
            }
        }
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        double output = 0;
        if(checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, Pair<Integer, Double>> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                int quantity = entry.getValue().getKey();
                output += Discount.calculateDiscountOnProduct(
                        product.getPrice(), quantity, entry.getValue().getValue(), percentage);
            }
        }
        return output;
    }


    public double getPercentage() {
        return percentage;
    }
}
