package com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition;

import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.Embeddable;
import javax.persistence.Entity;


@Entity
public class TotalBasketPriceCondition extends Condition {
    double price;

    public TotalBasketPriceCondition(double price) {
        super("condition true if the total basket price is equal or greater then " + price);
        this.price = price;
    }

    public TotalBasketPriceCondition() {

    }

    @Override
    public boolean checkCondition(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getTotalPrice() >= price;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
