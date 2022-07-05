package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.PurchaseHistory;
import com.example.seprojectweb.Domain.Market.QuantityDiscount;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PurchaseHistoryResponse {
    private final int visitorId;
    private final int shopId;
    private final double cost;
    private final int deliveryId;
    private final int chargedReceipt;
    private final List<Pair<Integer, Integer>> productIdToQuantity;
    private final String purchaseError;

    public PurchaseHistoryResponse(PurchaseHistory purchaseHistory) {
        this.purchaseError = purchaseHistory.getPurchaseError();
        this.visitorId = purchaseHistory.getVisitorId();
        this.shopId = purchaseHistory.getShopId();
        this.cost = purchaseHistory.getCost();
        this.deliveryId = purchaseHistory.getDeliveryId();
        this.productIdToQuantity = new ArrayList<>();
        this.chargedReceipt = purchaseHistory.getChargedReceipt();
        for (Map.Entry<Integer, Integer> productIntegerEntry : purchaseHistory.getProductIdToQuantity().entrySet()) {
            Pair<Integer, Integer> myProductToQuantity = new Pair<>(productIntegerEntry.getKey(), productIntegerEntry.getValue());
            productIdToQuantity.add(myProductToQuantity);
        }
    }

    public boolean isErrorAcquired() {
        return purchaseError != null;
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
}
