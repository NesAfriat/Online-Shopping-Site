package Domain.Market.Conditions;

import Domain.Market.ShoppingBasket;

public class IfThenCondition implements ICondition {

    ICondition test;
    ICondition then;


    public IfThenCondition (ICondition test, ICondition then){
        this.test = test;
        this.then = then;
    }

    @Override
    public boolean checkCondition(ShoppingBasket shoppingBasket) {
        return  !test.checkCondition(shoppingBasket) || then.checkCondition(shoppingBasket);
    }

    @Override
    public String getDescription() {
        return "if " + test.getDescription() + " then " + then.getDescription();
    }
}
