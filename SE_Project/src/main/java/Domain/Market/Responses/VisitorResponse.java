package Domain.Market.Responses;

import Domain.Market.Member;
import Domain.Market.Visitor;

public class VisitorResponse {
    int id;
    MemberResponse loggedIn;
    ShoppingCartResponse shoppingCart;
    public VisitorResponse(Visitor visitor){
        this.id = visitor.getId();
        Member member= visitor.getLoggedIn();
        if(member!=null)
        this.loggedIn = new MemberResponse(member);
        this.shoppingCart = new ShoppingCartResponse(visitor.getShoppingCart());

    }

    public int getId() {
        return id;
    }

    public MemberResponse getLoggedIn() {
        return loggedIn;
    }

    public ShoppingCartResponse getShoppingCart() {
        return shoppingCart;
    }
}
