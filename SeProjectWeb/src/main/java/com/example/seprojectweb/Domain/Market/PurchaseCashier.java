package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.DeliveryAdapter.HTTPDelivery;
import com.example.seprojectweb.Domain.DeliveryAdapter.ProxyDelivery;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.Market.Notifications.ShopNotification;
import com.example.seprojectweb.Domain.PaymentAdapter.HTTPPayment;
import com.example.seprojectweb.Domain.PaymentAdapter.ProxyPayment;
import com.example.seprojectweb.Domain.PersistenceManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseCashier {

    private VisitorController visitorController;
    private ShopController shopController;
    private ProxyPayment paymentAdapter;
    private ProxyDelivery deliveryAdapter;

    private Map<Integer, List<PurchaseHistory>> shopIdToPurchases;

    private Map<String, List<PurchaseHistory>> memberUsernameToPurchases;

    public PurchaseCashier() {
        visitorController = VisitorController.getInstance();
        shopController = ShopController.getInstance();
        shopIdToPurchases = new ConcurrentHashMap<>();
        memberUsernameToPurchases = new ConcurrentHashMap<>();
        paymentAdapter = ProxyPayment.getInstance();
        paymentAdapter.setPaymentService(new HTTPPayment());
        deliveryAdapter = ProxyDelivery.getInstance();
        deliveryAdapter.setDeliveryService(new HTTPDelivery());
    }

    public static PurchaseCashier getInstance() {
        return PurchaseControllerHolder.instance;
    }

    public void setPurchaseCashier(VisitorController vc, ShopController sc, ProxyPayment pa, ProxyDelivery da) {
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

    private boolean checkPaymentInput(String creditCardNumber) {
        return (creditCardNumber == null || creditCardNumber.trim().isEmpty() || !creditCardNumber.matches("[0-9]*"));
    }

    public List<PurchaseHistory> purchaseCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) throws InnerLogicException {
//        if(checkPaymentInput(creditCardNumber)){
//            throw new InnerLogicException("error - invalid credit card");
//        }
        Visitor visitor = visitorController.getVisitor(visitorID);
        String holder = "visitor";
        if(visitor.isLoggedIn()){
            holder = visitor.getLoggedIn().getUsername();
        }
        Map<Integer,ShoppingBasket> cloneShopIdToBaskets = new HashMap<>();

        Set<Map.Entry<Integer, ShoppingBasket>> shopIdToBasket = visitor.getBaskets();
        for (Map.Entry<Integer,ShoppingBasket> entry: shopIdToBasket){
            cloneShopIdToBaskets.put(entry.getKey(),entry.getValue());
        }
        if (shopIdToBasket.isEmpty()) {
            throw new InnerLogicException("cant buy empty shopping cart");
        }
        DBConnection dbConnection = PersistenceManager.beginTransaction();

        List<PurchaseHistory> purchaseHistories = new ArrayList<>();
        List<Map.Entry<Integer, ShoppingBasket>> basketsToDelete = new ArrayList<>();
        for (Map.Entry<Integer, ShoppingBasket> shopToBasket : shopIdToBasket) {
            int shopId = shopToBasket.getKey();
            Shop shop = null;
            ShoppingBasket shoppingBasket = shopToBasket.getValue();
            try {
                shop = shopController.getShopWithConnection(shopId,dbConnection);
            } catch (Exception e) {
                basketsToDelete.add(shopToBasket);
            }

            if (shop != null && shop.isOpen()) {
                RegularPurchase regularPurchase = new RegularPurchase(visitorID, shop, shoppingBasket);
                PurchaseHistory purchaseHistory = regularPurchase.purchaseBasket(holder, creditCardNumber, date, cvs, country, city, street, zip, deliveryAdapter, paymentAdapter, dbConnection);


                saveHistory(purchaseHistories, purchaseHistory, visitor, shopId,dbConnection);
                if (!purchaseHistory.isErrorOccurred()){
                    basketsToDelete.add(shopToBasket);
                    StringBuilder sb = new StringBuilder();
                    for(Map.Entry<Product, QuantityDiscount> entry: shoppingBasket.getProducts()){
                        sb.append(entry.getKey().getProductName());
                    }
                    Notification notification = new ShopNotification(shopId, "The following products has been purchased:\n"+sb);
                    RoleController.getInstance().sendMessagesToShopOwnersWithConnection(shopId, notification,dbConnection);
                }
                else{
                    shoppingBasket.clearDiscounts();
                }
            }
        }
        // delete baskets that purchased successfully
//        for (Map.Entry<Integer, ShoppingBasket> shopId : basketsToDelete) {
//            shopIdToBasket.remove(shopId);
//        }
        visitor.clearPurchasedBaskets(basketsToDelete,dbConnection);
        PersistenceManager.commitTransaction(dbConnection);

        //TODO "if purchase failed and all basket failed throw inner exception"
        return purchaseHistories;
    }


    public void saveHistory(PurchaseHistory purchaseHistory, String username, int shopId) throws InnerLogicException {
        if (!purchaseHistory.isErrorOccurred()) {
            PersistenceManager.persist(purchaseHistory);
            // add to relevant maps here
            List<PurchaseHistory> shopPurchases = shopIdToPurchases.getOrDefault(shopId, new ArrayList<>());
            shopPurchases.add(purchaseHistory);

            List<PurchaseHistory> memberPurchases = memberUsernameToPurchases.getOrDefault(username, new ArrayList<>());
            memberPurchases.add(purchaseHistory);
        }
    }



    private void saveHistory(List<PurchaseHistory> purchaseHistories, PurchaseHistory purchaseHistory, Visitor visitor, int shopId, DBConnection dbConnection) throws InnerLogicException {
        purchaseHistories.add(purchaseHistory);
        if (!purchaseHistory.isErrorOccurred()) {
            dbConnection.persist(purchaseHistory);
            // add to relevant maps here
            List<PurchaseHistory> shopPurchases = shopIdToPurchases.getOrDefault(shopId, new ArrayList<>());
            shopPurchases.add(purchaseHistory);
            shopIdToPurchases.putIfAbsent(shopId, shopPurchases);
            // if member add history to members map
            if (visitor.isLoggedIn()) {
                String memberUsername = visitor.getLoggedIn().getUsername();
                List<PurchaseHistory> memberPurchases = memberUsernameToPurchases.getOrDefault(memberUsername, new ArrayList<>());
                memberPurchases.add(purchaseHistory);
                memberUsernameToPurchases.putIfAbsent(memberUsername, memberPurchases);
            }
        }
    }

    //for tests
    public List<PurchaseHistory> getPurchasesMember(String memberUserName) {
        return memberUsernameToPurchases.get(memberUserName);
    }

    public List<PurchaseHistory> getPurchasesShop(int shopId) {
        return shopIdToPurchases.get(shopId);
    }

    private static class PurchaseControllerHolder {
        private static final PurchaseCashier instance = new PurchaseCashier();
    }
    private DBConnection beginTransaction() throws InnerLogicException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();
        return dbConnection;
    }
    private void commitTransaction(DBConnection dbConnection) throws InnerLogicException {
        dbConnection.commitTransaction();
        dbConnection.closeConnections();
    }
}
