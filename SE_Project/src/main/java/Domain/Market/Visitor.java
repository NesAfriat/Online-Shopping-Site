package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Notifications.IMemberObserver;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Visitor {
    private int id;
    private Member loggedIn;
    private ShoppingCart shoppingCart;
    private long lastActivityTime;

    public Visitor(int id){
        this.id = id;
        this.loggedIn = null;
        this.shoppingCart = new ShoppingCart();
        this.lastActivityTime = System.currentTimeMillis();
    }

    public int getId(){
        return id;
    }

    public Member getLoggedIn(){
        return loggedIn;
    }

    public ShoppingCart getShoppingCart(){
        return shoppingCart;
    }

    public long getLastActivityTime(){
        return lastActivityTime;
    }

    public void updateLastActivityTime() {
        this.lastActivityTime = System.currentTimeMillis();
    }

    public void login(Member member, IMemberObserver memberObserver) throws InnerLogicException {
        if(loggedIn != null){
            throw new InnerLogicException(loggedIn.getUsername() + " is all ready logged in for this session");
        }else{
            member.login(memberObserver);
            loggedIn = member;
        }
    }

    public void logout() throws InnerLogicException {
        if (loggedIn == null) {
            throw new InnerLogicException("member isn't logged in");
        }
        loggedIn.logout();
        loggedIn = null;
    }

    public boolean isLoggedIn() {
        return loggedIn != null;
    }

    public void setShoppingCart(ShoppingCart cart) {
        shoppingCart = cart;
    }

    public ShoppingCart addProductToShoppingCart(int shopID, Product prod, int quantity) {
        shoppingCart.addProductToShoppingCart(shopID,prod,quantity);
        return shoppingCart;
    }

    public ShoppingCart removeProductFromShoppingCart(int shopID, int productID) throws InnerLogicException {
        shoppingCart.removeProductFromShoppingCart(shopID, productID);
        return shoppingCart;
    }


    public Set<Map.Entry<Integer, ShoppingBasket>> getBaskets() {
        return shoppingCart.getBaskets();
    }
}
