package Domain.Market;

import Domain.DeliveryAdapter.IDelivey;
import Domain.DeliveryAdapter.ProxyDelivery;
import Domain.InnerLogicException;
import Domain.PaymentAdapter.IPayment;
import javafx.util.Pair;

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


    public PurchaseHistory purchaseBasket(String creditCardNumber, String date, String cvs, String country, String city, String street, ShopController shopController, IDelivey delivery, IPayment payment) {
        // IMPORTANT! Sorting is must in order to avoid deadlocks.
        List<Map.Entry<Product, Pair<Integer, Double>>> sortedProducts = basket.getProducts().stream()
                        .sorted(Comparator.comparingInt(productIntegerEntry -> productIntegerEntry.getKey().getProductID()))
                        .collect(Collectors.toList());
        acquireLocks(sortedProducts);
        for (Map.Entry<Product, Pair<Integer, Double>> productToAmount : sortedProducts){
            Product product = getProductFromEntry(productToAmount);
            int quantity = getQuantityFromEntry(productToAmount);
            if (!(product.getQuantity() >= quantity)){
                releaseLocks(sortedProducts);
                return new PurchaseHistory("Required quantity is bigger than can be supplied. product: " + product.getProductID(), visitorId, shop.getShopId());
            }
        }
        // 0.1 validate purchase policy
        boolean isValidPurchase = shop.validatePurchasePolicy(basket);
        if (!isValidPurchase){
            releaseLocks(sortedProducts);
            return new PurchaseHistory("purchase is not according to shop policy.", visitorId, shop.getShopId());
        }
        // 0.2 get discount
        shop.applyDiscounts(basket);
        double cost = basket.getTotalPrice();

        // 1. validate can deliver
        int deliveryId = delivery.bookDelivery(country, city, street);
        if (deliveryId < 0){
            releaseLocks(sortedProducts);
            return new PurchaseHistory("Failed to get confirmation for delivery for basket.", visitorId, shop.getShopId());
        }
        // 2. validate can pay
        int chargedReceipt = payment.chargeCreditCard(creditCardNumber, date, cvs, cost);
        if (chargedReceipt < 0){
            ProxyDelivery.getInstance().cancelDelivery(deliveryId);
            releaseLocks(sortedProducts);
            return new PurchaseHistory("Failed to get confirmation for payment for basket.", visitorId, shop.getShopId());
        }
        // 3. update new quantities
        for (Map.Entry<Product, Pair<Integer, Double>> productToAmount : sortedProducts){
            Product product = getProductFromEntry(productToAmount);
            int toBuyQuantity = getQuantityFromEntry(productToAmount);
            try {
                product.setQuantity(product.getQuantity()-toBuyQuantity);
            } catch (InnerLogicException e) {
                Logger.getInstance().logError("worst error ever if happen delete project start from beginning (in buy cart)\nerror message: " + e.getMessage());
            }
        }
        releaseLocks(sortedProducts);
        PurchaseHistory purchaseHistory = new PurchaseHistory(visitorId, shop.getShopId(), basket, cost, deliveryId, chargedReceipt);
        shop.notifyShopObservers(purchaseHistory.toString());
        return purchaseHistory;
    }

    private void acquireLocks(List<Map.Entry<Product, Pair<Integer, Double>>> sortedProducts) {
        for (Map.Entry<Product, Pair<Integer, Double>> entry : sortedProducts){
            entry.getKey().acquire();
        }
    }

    private void releaseLocks(List<Map.Entry<Product, Pair<Integer, Double>>> sortedProducts) {
        for (Map.Entry<Product, Pair<Integer, Double>> entry : sortedProducts){
            entry.getKey().release();
        }
    }

    private Product getProductFromEntry(Map.Entry<Product, Pair<Integer, Double>> entry){
        return entry.getKey();
    }

    private int getQuantityFromEntry(Map.Entry<Product, Pair<Integer, Double>> entry){
        return entry.getValue().getKey();
    }

    private double getDiscountFromEntry(Map.Entry<Product, Pair<Integer, Double>> entry){
        return entry.getValue().getValue();
    }

}
