package Domain.Market.DiscountPolicies;

import Domain.Market.ShoppingBasket;

public class XorDiscount extends Discount{


    Discount discount1;
    Discount discount2;

    public XorDiscount(int discountId, Discount discount1, Discount discount2) {
        super(discountId, null);
        this.discount1 = discount1;
        this.discount2 = discount2;
    }

    @Override
    public void applyDiscount(ShoppingBasket shoppingBasket) {
        Discount chosenOne = chooseDiscount(shoppingBasket);
        chosenOne.applyDiscount(shoppingBasket);
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket) {
        Discount chosenOne = chooseDiscount(shoppingBasket);
        return chosenOne.calculateDiscount(shoppingBasket);
    }

    private Discount chooseDiscount(ShoppingBasket shoppingBasket){
        if(discount1.calculateDiscount(shoppingBasket) <= discount2.calculateDiscount(shoppingBasket)){
            return discount1;
        }
        return discount2;
    }

    public Discount getDiscount1() {
        return discount1;
    }

    public Discount getDiscount2() {
        return discount2;
    }
}
