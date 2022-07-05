package com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition;

import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.*;

@Entity
public class AtLeastProductQuantityCondition extends Condition {



    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private int quantity;

    public AtLeastProductQuantityCondition(Product product, int quantity) {
        super("condition true if the quantity of " + product.getProductName() +
                " is equal or greater than " + quantity);
        this.product = product;
        this.quantity = quantity;

    }

    public AtLeastProductQuantityCondition() {

    }

    @Override
    public boolean checkCondition(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getProductQuantity(product) >= quantity;
    }


    @ManyToOne(cascade = CascadeType.ALL, targetEntity=Product.class)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
