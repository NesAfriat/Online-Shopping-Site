package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.QuantityDiscount;
import com.example.seprojectweb.Domain.Market.ShoppingBasket;
import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShoppingBasketResponse {
//    public Map<ProductResponse, Integer> products;
    public Map<Integer, Pair<Integer, ProductResponse>> products; // productId, (amount, product)

    public ShoppingBasketResponse(int shopID, ShoppingBasket sb) {
        products = new ConcurrentHashMap<>();
        for (Map.Entry<Product, QuantityDiscount> prod : sb.getProducts()) {
            products.put(prod.getKey().getId(), new Pair<>(prod.getValue().getQuantity(), new ProductResponse(shopID, prod.getKey())));
//            products.put(new ProductResponse(shopID, prod.getKey()), prod.getValue().getQuantity());
        }
    }

    public Integer getProductId(int productId) {
//        return products.keySet().stream().filter(productResponse -> productResponse.getId() == productId).collect(Collectors.toList()).get(0).getId();
        if(products.containsKey(productId)){
            return productId;
        }else{
            return -1;
        }
    }
}
