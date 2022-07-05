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
public class CategoryDiscount extends Discount {

    private String category;
    private double percentage;

    public CategoryDiscount(double percentage, Date lastValidDate, String category) throws InnerLogicException {
        super(lastValidDate);
        this.category = category;
        verifyLegalPercentage(percentage);
        this.percentage = percentage;
    }

    public CategoryDiscount() {

    }


    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        if (checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, QuantityDiscount> entry : shoppingBasket.getProducts()) {
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
        if (checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, QuantityDiscount> entry : shoppingBasket.getProducts()) {
                Product product = entry.getKey();
                if (product.getCategory().equals(category)) {
                    int quantity = entry.getValue().getQuantity();
                    output += Discount.calculateDiscountOnProduct(
                            product.getPrice(), quantity, entry.getValue().getDiscount(), percentage);
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
