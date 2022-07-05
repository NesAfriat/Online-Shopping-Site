package com.example.seprojectweb.Domain.Market.DiscountPolicies;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.QuantityDiscount;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Map;

@Entity
public class SingleProductDiscount extends Discount {

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private double percentage;


    public SingleProductDiscount(double percentage, Date lastValidDate, Product product) throws InnerLogicException {
        super(lastValidDate);
        this.product = product;
        verifyLegalPercentage(percentage);
        this.percentage = percentage;
    }

    public SingleProductDiscount() {

    }


    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        if (checkCondition(shoppingBasket)) {
            shoppingBasket.updateProductDiscount(product, percentage);
        }
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        if (checkCondition(shoppingBasket)) {
            for (Map.Entry<Product, QuantityDiscount> entry : shoppingBasket.getProducts()) {
                if (entry.getKey() == product) {
                    int quantity = entry.getValue().getQuantity();
                    return Discount.calculateDiscountOnProduct(
                            product.getPrice(), quantity, entry.getValue().getDiscount(), percentage);
                }
            }
        }
        return 0;
    }

    @ManyToOne(cascade = CascadeType.ALL, targetEntity=Product.class)
    public Product getProduct() {
        return product;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
