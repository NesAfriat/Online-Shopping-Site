package Domain.Market.Conditions;

import Domain.Market.ShoppingBasket;

import java.util.List;

public class OrCondition implements ICondition {

    List<ICondition> conditions;

    public OrCondition(List<ICondition> conditions){
        this.conditions = conditions;
    }

    @Override
    public boolean checkCondition(ShoppingBasket shoppingBasket) {
        boolean output = false;
        for (ICondition condition : conditions) {
            output = output | condition.checkCondition(shoppingBasket);
        }
        return output;
    }

    @Override
    public String getDescription() {
        StringBuilder output = new StringBuilder();
        for (ICondition condition: conditions) {
            output.append(condition.getDescription()).append(" or ");
        }
        return output.toString();
    }

}
