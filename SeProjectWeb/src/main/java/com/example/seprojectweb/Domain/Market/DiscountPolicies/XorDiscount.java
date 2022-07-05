package com.example.seprojectweb.Domain.Market.DiscountPolicies;

import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class XorDiscount extends Discount {

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    Discount discount1;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    Discount discount2;

    public XorDiscount(Discount discount1, Discount discount2) {
        super(null);
        this.discount1 = discount1;
        this.discount2 = discount2;
    }

    public XorDiscount() {

    }

    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        Discount chosenOne = chooseDiscount(shoppingBasket);
        chosenOne.applyDiscount(shoppingBasket);
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        Discount chosenOne = chooseDiscount(shoppingBasket);
        return chosenOne.calculateDiscount(shoppingBasket);
    }

    private Discount chooseDiscount(ShoppingBasket shoppingBasket) {
        if (discount1.calculateDiscount(shoppingBasket) >= discount2.calculateDiscount(shoppingBasket)) {
            return discount1;
        }
        return discount2;
    }

    public Discount getDiscount1() {
        return discount1;
    }

    public Discount getDiscount2() {
        return discount2;
    }

    public void setDiscount1(Discount discount1) {
        this.discount1 = discount1;
    }

    public void setDiscount2(Discount discount2) {
        this.discount2 = discount2;
    }
}
