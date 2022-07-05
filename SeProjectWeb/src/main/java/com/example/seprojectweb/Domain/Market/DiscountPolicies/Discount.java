package com.example.seprojectweb.Domain.Market.DiscountPolicies;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.Conditions.ConditionFactory;
import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Discount {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private Date lastValidDate; // if null then no expiration date

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Condition condition;


    public Discount(Date lastValidDate) {
        //this.id = id;
        this.lastValidDate = lastValidDate;
        this.condition = null;
    }

    public Discount() {

    }

    protected static double calculateDiscountOnProduct(double price, int quantity, double existingDiscount, double newDiscount) {
        double oldPercentageToPay = 1 - existingDiscount;
        double newPercentageDiscount = 1 - (oldPercentageToPay * (1 - newDiscount));
        return price * quantity * newPercentageDiscount;
    }

    protected void verifyLegalPercentage(double percentage) throws InnerLogicException {
        if (percentage < 0 || percentage > 1) {
            throw new InnerLogicException("tried to add discount with illegal percentage");
        }
    }

    public abstract void applyDiscount(ShoppingBasket shoppingBasket);

    /***
     * do not change the basket
     * @return the differences price of the basket
     */
    public abstract double calculateDiscount(ShoppingBasket shoppingBasket);

    protected boolean checkCondition(ShoppingBasket shoppingBasket) {
        boolean conditionPass = (condition == null) || condition.checkCondition(shoppingBasket);
        boolean isValidDate = lastValidDate == null || lastValidDate.after(new Date());
        return isValidDate && conditionPass;
    }

    public void addCompositeCondition(CONDITION_COMPOSE_TYPE type, Condition newCondition) throws InnerLogicException {
        if (condition == null) {
            condition = newCondition;
        } else {
            condition = ConditionFactory.getComposedCondition(type, newCondition, condition);
        }
    }


    public Condition getCondition() {
        return condition;
    }


    public Date getLastValidDate() {
        return lastValidDate;
    }


    public void setLastValidDate(Date lastValidDate) {
        this.lastValidDate = lastValidDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return id == discount.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
