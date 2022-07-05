package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Entity
public class ShoppingCart {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Map<Integer, ShoppingBasket> shoppingBaskets;           // key: shopID, value: shopping basket

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public ShoppingCart() {
        this.shoppingBaskets = new ConcurrentHashMap<>();
    }

    public void addProductToShoppingCart(int shopID, Product prod, int quantity) {
        if (!shoppingBaskets.containsKey(shopID)) {
            shoppingBaskets.put(shopID, new ShoppingBasket());
        }
        shoppingBaskets.get(shopID).addProduct(prod, quantity);
    }

    public Set<Map.Entry<Integer, ShoppingBasket>> getBaskets() {
        return shoppingBaskets.entrySet();
    }

    public Map.Entry<Product,QuantityDiscount> removeProductFromShoppingCart(int shopID, int productID) throws InnerLogicException {
        if (!shoppingBaskets.containsKey(shopID)) {
            throw new InnerLogicException("Error...");
        }
        return shoppingBaskets.get(shopID).removeProduct(productID);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Map<Integer, ShoppingBasket> getShoppingBaskets() {
        return shoppingBaskets;
    }

    public void setShoppingBaskets(Map<Integer, ShoppingBasket> shoppingBaskets) {
        this.shoppingBaskets = shoppingBaskets;
    }
}
