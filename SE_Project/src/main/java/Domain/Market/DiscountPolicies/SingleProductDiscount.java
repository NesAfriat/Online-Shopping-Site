package Domain.Market.DiscountPolicies;

import Domain.InnerLogicException;
import Domain.Market.Product;
import Domain.Market.Responses.ProductResponse;
import Domain.Market.ShoppingBasket;
import javafx.util.Pair;

import java.util.Date;
import java.util.Map;

public class SingleProductDiscount extends Discount{

    private final Product product;
    private final double percentage;


    public SingleProductDiscount(int discountId, double percentage, Date lastValidDate, Product product) throws InnerLogicException {
        super(discountId, lastValidDate);
        this.product= product;
        verifyLegalPercentage(percentage);
        this.percentage=percentage;
    }


    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        if(checkCondition(shoppingBasket)){
            shoppingBasket.updateProductDiscount(product, percentage);
        }
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        if(checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, Pair<Integer, Double>> entry : shoppingBasket.getProducts()) {
                if (entry.getKey() == product) {
                    int quantity = entry.getValue().getKey();
                    return Discount.calculateDiscountOnProduct(
                            product.getPrice(), quantity, entry.getValue().getValue(), percentage);
                }
            }
        }
        return 0;
    }

    public Product getProduct() {
        return product;
    }

    public double getPercentage() {
        return percentage;
    }
}
