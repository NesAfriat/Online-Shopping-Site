package Domain.Market;

import Domain.DeliveryAdapter.IDelivey;
import Domain.DeliveryAdapter.ProxyDelivery;
import Domain.InnerLogicException;
import Domain.PaymentAdapter.IPayment;
import Domain.PaymentAdapter.ProxyPayment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseCashier {

    private VisitorController visitorController;
    private ShopController shopController;
    private IPayment paymentAdapter;
    private IDelivey deliveryAdapter;
    private Map<Integer, List<PurchaseHistory>> shopIdToPurchases;
    private Map<String, List<PurchaseHistory>> memberUsernameToPurchases;

    public PurchaseCashier() {
        visitorController = VisitorController.getInstance();
        shopController = ShopController.getInstance();
        shopIdToPurchases = new ConcurrentHashMap<>();
        memberUsernameToPurchases = new ConcurrentHashMap<>();
        paymentAdapter = ProxyPayment.getInstance();
        deliveryAdapter = ProxyDelivery.getInstance();
    }

    public void setPurchaseCashier(VisitorController vc, ShopController sc, ProxyPayment pa, ProxyDelivery da){
        visitorController = vc;
        shopController = sc;
        paymentAdapter = pa;
        deliveryAdapter = da;
        shopIdToPurchases = new ConcurrentHashMap<>();
        memberUsernameToPurchases = new ConcurrentHashMap<>();
    }

    public List<PurchaseHistory> getShopHistories(int shopId) {
        return shopIdToPurchases.getOrDefault(shopId, new ArrayList<>());
    }

    private static class PurchaseControllerHolder {
        private static final PurchaseCashier instance = new PurchaseCashier();
    }

    public static PurchaseCashier getInstance(){
        return PurchaseCashier.PurchaseControllerHolder.instance;
    }

    private boolean checkPaymentInput(String creditCardNumber)
    {
        return (creditCardNumber==null || creditCardNumber.trim().isEmpty() || !creditCardNumber.matches("[0-9]*"));
    }
    public List<PurchaseHistory> purchaseCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street) throws InnerLogicException {
//        if(checkPaymentInput(creditCardNumber)){
//            throw new InnerLogicException("error - invalid credit card");
//        }
        Visitor visitor = visitorController.getVisitor(visitorID);

        Set<Map.Entry<Integer, ShoppingBasket>> shopIdToBasket = visitorController.getBaskets(visitorID);
        if(shopIdToBasket.isEmpty()){
            throw new InnerLogicException("cant buy empty shopping cart");
        }
        List<PurchaseHistory> purchaseHistories = new ArrayList<>();
        List<Map.Entry<Integer, ShoppingBasket>> basketsToDelete = new ArrayList<>();
        for (Map.Entry<Integer, ShoppingBasket> shopToBasket : shopIdToBasket){
            int shopId = shopToBasket.getKey();
            Shop shop = null;
            ShoppingBasket shoppingBasket = shopToBasket.getValue();
            try {
                shop = shopController.getShop(visitorID, shopId);
            }
            catch (Exception e){
                basketsToDelete.add(shopToBasket);
            }

            if(shop != null && shop.isOpen()){
                RegularPurchase regularPurchase = new RegularPurchase(visitorID, shop, shoppingBasket);
                PurchaseHistory purchaseHistory = regularPurchase.purchaseBasket(creditCardNumber, date, cvs, country, city, street, shopController, deliveryAdapter, paymentAdapter);


                saveHistory(purchaseHistories, purchaseHistory, visitor, shopId);
                if (!purchaseHistory.isErrorOccurred())
                    basketsToDelete.add(shopToBasket);
            }
        }
        // delete baskets that purchased successfully
        for (Map.Entry<Integer, ShoppingBasket> shopId : basketsToDelete){
            shopIdToBasket.remove(shopId);
        }
        //TODO "if purchase failed and all basket failed throw inner exception"
        return purchaseHistories;
    }

    private void saveHistory(List<PurchaseHistory> purchaseHistories, PurchaseHistory purchaseHistory, Visitor visitor, int shopId) {
        purchaseHistories.add(purchaseHistory);
        if (!purchaseHistory.isErrorOccurred()){
            // add to relevant maps here
            List<PurchaseHistory> shopPurchases = shopIdToPurchases.getOrDefault(shopId, new ArrayList<>());
            shopPurchases.add(purchaseHistory);
            shopIdToPurchases.putIfAbsent(shopId, shopPurchases);
            // if member add history to members map
            if (visitor.isLoggedIn()){
                String memberUsername = visitor.getLoggedIn().getUsername();
                List<PurchaseHistory> memberPurchases = memberUsernameToPurchases.getOrDefault(memberUsername,new ArrayList<>());
                memberPurchases.add(purchaseHistory);
                memberUsernameToPurchases.putIfAbsent(memberUsername, memberPurchases);
            }
        }
    }

    //for tests
    public List<PurchaseHistory> getPurchasesMember(String memberUserName){
        return memberUsernameToPurchases.get(memberUserName);
    }

    public List<PurchaseHistory> getPurchasesShop(int shopId){
        return shopIdToPurchases.get(shopId);
    }
}
