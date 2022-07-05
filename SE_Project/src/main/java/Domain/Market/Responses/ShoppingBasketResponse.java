package Domain.Market.Responses;

import Domain.Market.Product;
import Domain.Market.ShoppingBasket;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShoppingBasketResponse {
    public Map<ProductResponse, Integer> products;
    public ShoppingBasketResponse(int shopID, ShoppingBasket sb) {
        products = new ConcurrentHashMap<>();
        for(Map.Entry<Product, Pair<Integer, Double>> prod: sb.getProducts()){
            products.put(new ProductResponse(shopID, prod.getKey()), prod.getValue().getKey());
        }
    }

    public Integer getProductId(int productId){
        return products.keySet().stream().filter(productResponse -> productResponse.getId() == productId).collect(Collectors.toList()).get(0).getId();
    }
}
