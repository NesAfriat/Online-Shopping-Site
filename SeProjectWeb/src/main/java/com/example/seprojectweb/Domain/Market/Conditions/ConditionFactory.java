package com.example.seprojectweb.Domain.Market.Conditions;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition.AndCondition;
import com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition.IfThenCondition;
import com.example.seprojectweb.Domain.Market.Conditions.ComposeCondition.OrCondition;
import com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition.AtLeastProductQuantityCondition;
import com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition.AtMostProductQuantityCondition;
import com.example.seprojectweb.Domain.Market.Conditions.SimpleCondition.TotalBasketPriceCondition;
import com.example.seprojectweb.Domain.Market.Product;

import java.util.LinkedList;
import java.util.List;

public class ConditionFactory {

    public static Condition buildTotalBasketPriceCondition(double price) {
        return new TotalBasketPriceCondition(price);
    }

    public static Condition buildAtLeastProductQuantityCondition(Product product, int quantity) {
        return new AtLeastProductQuantityCondition(product, quantity);
    }

    public static Condition buildAtMostProductQuantityCondition(Product product, int quantity) {
        return new AtMostProductQuantityCondition(product, quantity);
    }


    public static Condition getComposedCondition(CONDITION_COMPOSE_TYPE type, Condition newCondition, Condition oldCondition) throws InnerLogicException {
        if (type == CONDITION_COMPOSE_TYPE.RESET) {
            return newCondition;
        } else if (type == CONDITION_COMPOSE_TYPE.IF_THEN) {
            return new IfThenCondition(newCondition, oldCondition);
        } else {
//            List<Condition> conditions = new LinkedList<>();
//            conditions.add(newCondition);
//            conditions.add(oldCondition);
            if (type == CONDITION_COMPOSE_TYPE.AND) {
                return new AndCondition(newCondition, oldCondition);
            } else if (type == CONDITION_COMPOSE_TYPE.OR) {
                return new OrCondition(newCondition, oldCondition);
            } else
                throw new InnerLogicException("unknown condition compose type");
        }
    }
}

;
