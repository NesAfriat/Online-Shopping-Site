package Domain.Market.DiscountPolicies;

import Domain.InnerLogicException;
import Domain.Market.Conditions.*;
import Domain.Market.ShoppingBasket;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Discount {
    private ICondition condition;
    private final AtomicBoolean isFirstCondition;
    private final int discountId;
    private final Date lastValidDate; // if null then no expiration date


    public Discount(int discountId, Date lastValidDate){
        this.lastValidDate = lastValidDate;
        this.condition = new ICondition() {
            @Override
            public boolean checkCondition(ShoppingBasket shoppingBasket) {
                return true;
            }

            @Override
            public String getDescription() {
                return "";
            }
        };
        this.discountId = discountId;
        isFirstCondition = new AtomicBoolean(true);
    }


    protected void verifyLegalPercentage(double percentage) throws InnerLogicException {
        if(percentage < 0 || percentage > 1){
            throw new InnerLogicException("tried to add discount with illegal percentage");
        }
    }
    public abstract void applyDiscount(ShoppingBasket shoppingBasket);


    /***
     * do not change the basket
     * @return the differences price of the basket
     */
    public abstract double calculateDiscount(ShoppingBasket shoppingBasket);

    protected boolean checkCondition(ShoppingBasket shoppingBasket){
        return (lastValidDate == null || lastValidDate.after(new Date())) && condition.checkCondition(shoppingBasket);
    }


    public void addCompositeCondition(CONDITION_COMPOSE_TYPE type, ICondition newCondition) throws InnerLogicException {
        if (isFirstCondition.getAndSet(false)) {
            condition = newCondition;
        }
        else{
            condition = ConditionFactory.getComposedCondition(type, newCondition, condition);
        }
    }

    protected static double calculateDiscountOnProduct(double price, int quantity, double existingDiscount, double newDiscount){
        double oldPercentageToPay = 1 - existingDiscount;
        double newPercentageDiscount = 1 - (oldPercentageToPay * (1 - newDiscount));
        return price * quantity * newPercentageDiscount;
    }

    public int getId(){
        return discountId;
    }

    public ICondition getCondition() {
        return condition;
    }

    public int getDiscountId() {
        return discountId;
    }

    public Date getLastValidDate() {
        return lastValidDate;
    }

}
