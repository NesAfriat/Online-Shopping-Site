package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.DeliveryAdapter.IDelivey;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.PaymentAdapter.IPayment;
import com.example.seprojectweb.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegularPurchase {
    private final int visitorId;
    private final Shop shop;
    private final ShoppingBasket basket;

    public RegularPurchase(int visitorId, Shop shop, ShoppingBasket basket) {
        this.visitorId = visitorId;
        this.shop = shop;
        this.basket = basket;
    }


    public PurchaseHistory purchaseBasket(String holder, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip, IDelivey delivery, IPayment payment, DBConnection dbConnection) throws InnerLogicException {
        // IMPORTANT! Sorting is must in order to avoid deadlocks.
//        List<Map.Entry<Product, Pair<Integer, Double>>> sortedProducts = basket.getProducts().stream()
//                .sorted(Comparator.comparingInt(productIntegerEntry -> productIntegerEntry.getKey().getId()))
//                .collect(Collectors.toList());
        List<Map.Entry<Product, QuantityDiscount>> sortedProducts = basket.getProducts().stream()
                .sorted(Comparator.comparingInt(productIntegerEntry -> productIntegerEntry.getKey().getId()))
                .collect(Collectors.toList());
        acquireLocks(sortedProducts);
        for (Map.Entry<Product, QuantityDiscount> productToAmount : sortedProducts) {
            Product product = getProductFromEntry(productToAmount);
            int quantity = getQuantityFromEntry(productToAmount);
            if (!(product.getQuantity() >= quantity)) {
                releaseLocks(sortedProducts);
                return new PurchaseHistory("Required quantity is bigger than can be supplied. product: " + product.getId(), visitorId, shop.getShopId());
            }
        }
        // 0.1 validate purchase policy
        String isValidPurchase = shop.validatePurchasePolicy(basket);
        if (isValidPurchase != null) {
            releaseLocks(sortedProducts);
            return new PurchaseHistory("purchase is not according to shop policy.\n" + isValidPurchase, visitorId, shop.getShopId());
        }
        // 0.2 get discount
        shop.applyDiscounts(basket);
        double cost = basket.getTotalPrice();

        // 1. validate can deliver
        int deliveryId = delivery.bookDelivery(holder, country, city, street, zip);
        if (deliveryId < 0) {
            releaseLocks(sortedProducts);
            return new PurchaseHistory("Failed to get confirmation for delivery for basket.", visitorId, shop.getShopId());
        }
        dbConnection.addFailure(()->delivery.cancelDelivery(deliveryId));
        // 2. validate can pay
        int chargedReceipt = payment.chargeCreditCard(creditCardNumber, date, holder, cvs, cost);
        if (chargedReceipt < 0) {
            delivery.cancelDelivery(deliveryId);
            releaseLocks(sortedProducts);
            return new PurchaseHistory("Failed to get confirmation for payment for basket.", visitorId, shop.getShopId());
        }
        dbConnection.addFailure(()->payment.cancelPayment(chargedReceipt));

        // 3. update new quantities
        for (Map.Entry<Product, QuantityDiscount> productToAmount : sortedProducts) {
            Product product = getProductFromEntry(productToAmount);
            int toBuyQuantity = getQuantityFromEntry(productToAmount);
            try {
                int prevProductQuantity = product.getQuantity();
                //on failure
                product.setQuantity(product.getQuantity() - toBuyQuantity);
                dbConnection.addFailure(()->product.setQuantityWithOutCheck(prevProductQuantity));
                dbConnection.updateSession(product);


            } catch (InnerLogicException e) {
                delivery.cancelDelivery(deliveryId);
                payment.cancelPayment(chargedReceipt);
                Logger.getInstance().logError("worst error ever if happen delete project start from beginning (in buy cart)\nerror message: " + e.getMessage());
            }
        }
        releaseLocks(sortedProducts);


        String userName;
        try{
            //this function now using connection
            Member loggedin = VisitorController.getInstance().getVisitorLoggedIn(visitorId);
            userName = loggedin.getUsername();
        }catch (InnerLogicException e){
            userName = null;
        }

        PurchaseHistory purchaseHistory = new PurchaseHistory(visitorId, userName, shop.getShopId(), basket, cost, deliveryId, chargedReceipt);
        shop.notifyShopObserversWithConnection(purchaseHistory.toString(), dbConnection);
        return purchaseHistory;
    }

//    private void acquireLocks(List<Map.Entry<Product, Pair<Integer, Double>>> sortedProducts) {
//        for (Map.Entry<Product, Pair<Integer, Double>> entry : sortedProducts) {
//            entry.getKey().acquire();
//        }
//    }

    private void acquireLocks(List<Map.Entry<Product, QuantityDiscount>> sortedProducts) {
        for (Map.Entry<Product, QuantityDiscount> entry : sortedProducts) {
            entry.getKey().acquire();
        }
    }

    private void releaseLocks(List<Map.Entry<Product, QuantityDiscount>> sortedProducts) {
        for (Map.Entry<Product, QuantityDiscount> entry : sortedProducts) {
            entry.getKey().release();
        }
    }

//    private void releaseLocks(List<Map.Entry<Product, Pair<Integer, Double>>> sortedProducts) {
//        for (Map.Entry<Product, Pair<Integer, Double>> entry : sortedProducts) {
//            entry.getKey().release();
//        }
//    }

    private Product getProductFromEntry(Map.Entry<Product, QuantityDiscount> entry) {
        return entry.getKey();
    }

    private int getQuantityFromEntry(Map.Entry<Product, QuantityDiscount> entry) {
        return entry.getValue().getQuantity();
    }

    private double getDiscountFromEntry(Map.Entry<Product, QuantityDiscount> entry) {
        return entry.getValue().getDiscount();
    }

}
