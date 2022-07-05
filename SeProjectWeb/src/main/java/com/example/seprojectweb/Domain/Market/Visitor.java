package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.PersistenceManager;

import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Visitor {
    private final int id;
    private Member loggedIn;
    private ShoppingCart shoppingCart;
    private long lastActivityTime;

    public Visitor(int id) {
        this.id = id;
        this.loggedIn = null;
        this.shoppingCart = new ShoppingCart();
        this.lastActivityTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public Member getLoggedIn() {
        return loggedIn;
    }


    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart cart) {
        shoppingCart = cart;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void updateLastActivityTime() {
        this.lastActivityTime = System.currentTimeMillis();
    }

    public void login(Member member, IMemberObserver memberObserver) throws InnerLogicException {
        if (isLoggedIn()) {
            throw new InnerLogicException(loggedIn.getUsername() + " is all ready logged in for this session");
        } else {
            member.login(memberObserver);
            loggedIn = member;
            shoppingCart = member.getShoppingCart();
        }
    }

    public void logout() throws InnerLogicException {
        if (!isLoggedIn()) {
            throw new InnerLogicException("member isn't logged in");
        }
        //PersistenceManager.updateSession(loggedIn);
        loggedIn.logout();
        loggedIn = null;
        shoppingCart = new ShoppingCart();
    }

    public boolean isLoggedIn() {
        return loggedIn != null;
    }

    public ShoppingCart addProductToShoppingCart(int shopID, Product prod, int quantity) throws InnerLogicException {


        if (isLoggedIn()) {
            DBConnection dbConnection = PersistenceManager.beginTransaction();
            shoppingCart.addProductToShoppingCart(shopID, prod, quantity);
            //at fail
            dbConnection.addFailure(()-> {
                try {
                    shoppingCart.removeProductFromShoppingCart(shopID,prod.getId());
                } catch (InnerLogicException e) {
                    throw new IllegalAccessError(e.getMessage());
                }
            });
            dbConnection.update(loggedIn);
            PersistenceManager.commitTransaction(dbConnection);
        }
        else {
            shoppingCart.addProductToShoppingCart(shopID, prod, quantity);
        }
        return shoppingCart;
    }

    public ShoppingCart removeProductFromShoppingCart(int shopID, int productID) throws InnerLogicException {

        if (isLoggedIn()) {
            DBConnection dbConnection = PersistenceManager.beginTransaction();
            Map.Entry<Product,QuantityDiscount> entry = shoppingCart.removeProductFromShoppingCart(shopID, productID);
            dbConnection.addFailure(()->shoppingCart.getShoppingBaskets().get(shopID).getProducts().add(entry));
            //at fail

            dbConnection.update(loggedIn);
            PersistenceManager.commitTransaction(dbConnection);

        }else {
            shoppingCart.removeProductFromShoppingCart(shopID, productID);
        }
        return shoppingCart;
    }


    public Set<Map.Entry<Integer, ShoppingBasket>> getBaskets() {
        return shoppingCart.getBaskets();
    }

    public void clearPurchasedBaskets(List<Map.Entry<Integer, ShoppingBasket>> basketsToDelete,DBConnection dbConnection) throws InnerLogicException {
        if (isLoggedIn()){
            loggedIn.clearCart(basketsToDelete,dbConnection);
            return;
        }

        Set<Map.Entry<Integer, ShoppingBasket>> shopIdToBasket = getBaskets();
        for (Map.Entry<Integer, ShoppingBasket> shopIdBasketEntry : basketsToDelete) {
            shopIdToBasket.remove(shopIdBasketEntry);
        }

    }
}
