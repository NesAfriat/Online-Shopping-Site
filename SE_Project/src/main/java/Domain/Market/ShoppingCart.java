package Domain.Market;

import Domain.InnerLogicException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    Map<Integer, ShoppingBasket> shoppingBaskets;           // key: shopID, value: shopping basket

    public ShoppingCart(){
        this.shoppingBaskets = new ConcurrentHashMap<>();
    }

    public void addProductToShoppingCart(int shopID, Product prod, int quantity) {
        if(!shoppingBaskets.containsKey(shopID)){
            shoppingBaskets.put(shopID, new ShoppingBasket());
        }
        shoppingBaskets.get(shopID).addProduct(prod, quantity);
    }

    public Set<Map.Entry<Integer, ShoppingBasket>> getBaskets(){
        return shoppingBaskets.entrySet();
    }

    public void removeProductFromShoppingCart(int shopID, int productID) throws InnerLogicException {
        if(!shoppingBaskets.containsKey(shopID)){
            throw new InnerLogicException("Error...");
        }
        shoppingBaskets.get(shopID).removeProduct(productID);
    }

}
