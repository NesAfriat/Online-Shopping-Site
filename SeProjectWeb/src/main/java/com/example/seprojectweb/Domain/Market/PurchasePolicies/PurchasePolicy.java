package com.example.seprojectweb.Domain.Market.PurchasePolicies;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.Conditions.ConditionFactory;
import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class PurchasePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Condition condition;

    public PurchasePolicy(Condition condition) {
        this.condition = condition;
        //this.purchasePolicyId = purchasePolicyId;
    }

    public PurchasePolicy() {

    }

    public boolean checkPolicy(ShoppingBasket shoppingBasket) {
        return condition.checkCondition(shoppingBasket);
    }

    public void addCompositeCondition(CONDITION_COMPOSE_TYPE type, Condition newCondition) throws InnerLogicException {
        condition = ConditionFactory.getComposedCondition(type, newCondition, condition);
    }




    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
