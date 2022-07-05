package Domain.Market;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchaseHistory {
    private final int visitorId;
    private final int shopId;
    private final double cost;
    private final int deliveryId;
    private final int chargedReceipt;
    private final List<Pair<Integer, Integer>> productIdToQuantity;
    private final String purchaseError;
    private static final int DEFAULT_ERROR_NUMBER = -1;

    public PurchaseHistory(int visitorId, int shopId, ShoppingBasket basket, double cost, int deliveryId, int chargedReceipt) {
        this.visitorId = visitorId;
        this.shopId = shopId;
        this.cost = cost;
        this.deliveryId = deliveryId;
        this.chargedReceipt = chargedReceipt;
        this.productIdToQuantity = new ArrayList<>();
        for (Map.Entry<Product, Pair<Integer, Double>> productIntegerEntry : basket.getProducts()){
            int productId = productIntegerEntry.getKey().getId();
            int quantity = productIntegerEntry.getValue().getKey();
            productIdToQuantity.add(new Pair<>(productId, quantity));
        }
        this.purchaseError = null;
    }

    public PurchaseHistory(String purchaseError, int visitorId, int shopId){
        this.purchaseError = purchaseError;
        this.visitorId = visitorId;
        this.shopId = shopId;
        this.cost = DEFAULT_ERROR_NUMBER;
        this.deliveryId = DEFAULT_ERROR_NUMBER;
        this.chargedReceipt = DEFAULT_ERROR_NUMBER;
        this.productIdToQuantity = new ArrayList<>();
    }

    public int getVisitorId() {
        return visitorId;
    }

    public int getShopId() {
        return shopId;
    }

    public double getCost() {
        return cost;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public int getChargedReceipt() {
        return chargedReceipt;
    }

    public List<Pair<Integer, Integer>> getProductIdToQuantity() {
        return productIdToQuantity;
    }

    public String getPurchaseError() {
        return purchaseError;
    }

    public boolean isErrorOccurred(){
        return purchaseError != null;
    }

    @Override
    public String toString() {
        if (!isErrorOccurred()){
            return "PurchaseHistory{" +
                    "visitorId=" + visitorId +
                    ", shopId=" + shopId +
                    ", cost=" + cost +
                    ", deliveryId=" + deliveryId +
                    ", chargedReceipt=" + chargedReceipt +
                    ", productIdToQuantity=" + productIdToQuantity +
                    '}';
        }else
            return "PurchaseHistory{" +
                    "visitorId=" + visitorId +
                    ", shopId=" + shopId +
                    ", purchaseError='" + purchaseError + '\'' +
                    '}';
    }
}
