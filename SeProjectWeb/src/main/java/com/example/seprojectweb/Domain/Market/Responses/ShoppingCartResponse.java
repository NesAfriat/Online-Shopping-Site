package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.ShoppingBasket;
import com.example.seprojectweb.Domain.Market.ShoppingCart;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCartResponse {
    //TODO:add id or list of product respose
    public Map<Integer, ShoppingBasketResponse> baskets;

    public ShoppingCartResponse(ShoppingCart shoppingCart) {
        baskets = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, ShoppingBasket> sb : shoppingCart.getBaskets()) {
            baskets.put(sb.getKey(), new ShoppingBasketResponse(sb.getKey(), sb.getValue()));
        }
    }

}
