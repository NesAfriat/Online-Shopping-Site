package Domain.Market.Conditions;

import Domain.InnerLogicException;
import Domain.Market.Product;
import Domain.Market.ShoppingBasket;

import java.util.LinkedList;
import java.util.List;

public class ConditionFactory {

    public static ICondition buildTotalBasketPriceCondition(double price){
        return new ICondition() {
            @Override
            public boolean checkCondition(ShoppingBasket shoppingBasket) {
                return shoppingBasket.getTotalPrice() >= price;
            }

            @Override
            public String getDescription() {
                return "condition true if the total basket price is equal or greater then " + price;
            }
        };
    }

    public static ICondition buildAtLeastProductQuantityCondition(Product product, int quantity){
        return new ICondition() {
            @Override
            public boolean checkCondition(ShoppingBasket shoppingBasket) {
                return shoppingBasket.getProductQuantity(product) >= quantity;
            }

            @Override
            public String getDescription() {
                return "condition true if the quantity of " + product.getProductName() +
                        " is equal or greater than " + quantity;
            }
        };
    }

    public static ICondition buildAtMostProductQuantityCondition(Product product, int quantity){
        return new ICondition() {
            @Override
            public boolean checkCondition(ShoppingBasket shoppingBasket) {
                return shoppingBasket.getProductQuantity(product) <= quantity;
            }

            @Override
            public String getDescription() {
                return "condition true if the quantity of " + product.getProductName() +
                        " is equal or less than " + quantity;
            }
        };
    }


    public static ICondition getComposedCondition(CONDITION_COMPOSE_TYPE type, ICondition newCondition, ICondition oldCondition) throws InnerLogicException {
        if(type == CONDITION_COMPOSE_TYPE.RESET){
            return newCondition;
        }
        else if(type == CONDITION_COMPOSE_TYPE.IF_THEN){
            return new IfThenCondition(newCondition, oldCondition);
        }
        else {
            List<ICondition> conditions = new LinkedList<>();
            conditions.add(newCondition); conditions.add(oldCondition);
            if (type == CONDITION_COMPOSE_TYPE.AND){
                return new AndCondition(conditions);
            }
            else if(type == CONDITION_COMPOSE_TYPE.OR){
                return new OrCondition(conditions);
            }
            else
                throw new InnerLogicException("unknown condition compose type");
        }
    }
}
