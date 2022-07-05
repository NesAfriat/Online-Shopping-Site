package Domain.Market.Responses;

import Domain.Market.ShoppingBasket;
import Domain.Market.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCartResponse {
    //TODO:add id or list of product respose
    public Map<Integer, ShoppingBasketResponse> baskets;

    public ShoppingCartResponse(ShoppingCart shoppingCart) {
        baskets = new ConcurrentHashMap<>();
        for(Map.Entry<Integer, ShoppingBasket> sb: shoppingCart.getBaskets()){
            baskets.put(sb.getKey(), new ShoppingBasketResponse(sb.getKey(), sb.getValue()));
        }
    }

}
