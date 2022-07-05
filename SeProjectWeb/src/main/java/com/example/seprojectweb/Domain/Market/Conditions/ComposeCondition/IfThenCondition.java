package com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition;

import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.*;

@Entity
public class IfThenCondition extends Condition {
    public Condition getTest() {
        return test;
    }

    public void setTest(Condition test) {
        this.test = test;
    }

    public Condition getThen() {
        return then;
    }

    public void setThen(Condition then) {
        this.then = then;
    }

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Condition test;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Condition then;


    public IfThenCondition(Condition test, Condition then) {
        super("if " + test.getDescription() + " then " + then.getDescription());
        this.test = test;
        this.then = then;
    }

    public IfThenCondition() {

    }

    @Override
    public boolean checkCondition(ShoppingBasket shoppingBasket) {
        return !test.checkCondition(shoppingBasket) || then.checkCondition(shoppingBasket);
    }


}
