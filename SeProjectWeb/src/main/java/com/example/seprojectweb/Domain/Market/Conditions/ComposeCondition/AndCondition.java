package com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition;

import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.*;
import java.util.List;

@Entity
public class AndCondition extends Condition {

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Condition condition1;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Condition condition2;

    public AndCondition(Condition condition1, Condition condition2) {
        super(condition1.getDescription() + " and " + condition2.getDescription());
        this.condition1 = condition1;
        this.condition2 = condition2;
    }
    public AndCondition() {

    }


    @Override
    public boolean checkCondition(ShoppingBasket shoppingBasket) {
        return condition1.checkCondition(shoppingBasket) && condition2.checkCondition(shoppingBasket);
    }

//    @Override
//    public String getDescription() {
//        return condition1.getDescription() + " and " + condition2.getDescription();
//    }

    //@OneToOne(cascade = CascadeType.ALL, targetEntity= Condition.class)
    public Condition getCondition1() {
        return condition1;
    }

    public void setCondition1(Condition condition1) {
        this.condition1 = condition1;
    }

    public Condition getCondition2() {
        return condition2;
    }

    public void setCondition2(Condition condition2) {
        this.condition2 = condition2;
    }
}
