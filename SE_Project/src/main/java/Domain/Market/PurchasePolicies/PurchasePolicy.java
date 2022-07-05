package Domain.Market.PurchasePolicies;

import Domain.InnerLogicException;
import Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import Domain.Market.Conditions.ConditionFactory;
import Domain.Market.Conditions.ICondition;
import Domain.Market.ShoppingBasket;

public class PurchasePolicy {
    private ICondition condition;
    private final int purchasePolicyId;

    public PurchasePolicy(int purchasePolicyId, ICondition condition) {
        this.condition = condition;
        this.purchasePolicyId = purchasePolicyId;
    }

    public boolean checkPolicy(ShoppingBasket shoppingBasket){
        return condition.checkCondition(shoppingBasket);
    }

    public void addCompositeCondition(CONDITION_COMPOSE_TYPE type, ICondition newCondition) throws InnerLogicException {
        condition = ConditionFactory.getComposedCondition(type, newCondition, condition);
    }

    public int getPurchasePolicyId() {
        return purchasePolicyId;
    }

    public ICondition getCondition() {
        return condition;
    }

    public int getId() {
        return purchasePolicyId;
    }
}
