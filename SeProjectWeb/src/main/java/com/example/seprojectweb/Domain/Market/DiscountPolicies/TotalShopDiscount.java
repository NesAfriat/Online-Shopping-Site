package com.example.seprojectweb.Domain.Market.DiscountPolicies;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.QuantityDiscount;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;
import javafx.util.Pair;

import javax.persistence.Entity;
import java.util.Date;
import java.util.Map;

@Entity
public class TotalShopDiscount extends Discount {
    private double percentage;

    public TotalShopDiscount(double percentage, Date lastValidDate) throws InnerLogicException {
        super(lastValidDate);
        verifyLegalPercentage(percentage);
        this.percentage = percentage;
    }

    public TotalShopDiscount() {

    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        if (checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, QuantityDiscount> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                shoppingBasket.updateProductDiscount(product, percentage);
            }
        }
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        double output = 0;
        if (checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, QuantityDiscount> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                int quantity = entry.getValue().getQuantity();
                output += Discount.calculateDiscountOnProduct(
                        product.getPrice(), quantity, entry.getValue().getDiscount(), percentage);
            }
        }
        return output;
    }


    public double getPercentage() {
        return percentage;
    }
}
