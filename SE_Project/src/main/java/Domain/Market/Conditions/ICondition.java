package Domain.Market.Conditions;

import Domain.Market.ShoppingBasket;

public interface ICondition {

    boolean checkCondition(ShoppingBasket shoppingBasket);

    String getDescription();
}
