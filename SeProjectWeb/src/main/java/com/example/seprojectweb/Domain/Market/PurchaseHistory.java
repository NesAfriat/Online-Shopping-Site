package com.example.seprojectweb.Domain.Market;

import javax.persistence.*;
import java.util.*;

@Entity
public class PurchaseHistory {
    @Transient
    private static final int DEFAULT_ERROR_NUMBER = -1;
    private int visitorId;



    private String userName;
    private int shopId;
    private double cost;
    private int deliveryId;
    private int chargedReceipt;
    //private final List<Pair<Integer, Integer>> productIdToQuantity;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer, Integer> productIdToQuantity;
    @Transient
    private String purchaseError;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public PurchaseHistory(int visitorId, String userName, int shopId, ShoppingBasket basket, double cost, int deliveryId, int chargedReceipt) {
        this.visitorId = visitorId;
        this.userName = userName;
        this.shopId = shopId;
        this.cost = cost;
        this.deliveryId = deliveryId;
        this.chargedReceipt = chargedReceipt;
        //this.productIdToQuantity = new ArrayList<>();
        this.productIdToQuantity = new HashMap<>();
        for (Map.Entry<Product, QuantityDiscount> productIntegerEntry : basket.getProducts()) {
            int productId = productIntegerEntry.getKey().getId();
            int quantity = productIntegerEntry.getValue().getQuantity();
           // productIdToQuantity.add(new Pair<>(productId, quantity));
            productIdToQuantity.put(productId, quantity);
        }
        this.purchaseError = null;
    }

    public PurchaseHistory(int visitorId, String userName, int shopId, int productId, int quantity, double cost, int deliveryId, int chargedReceipt) {
        this.visitorId = visitorId;
        this.userName = userName;
        this.shopId = shopId;
        this.cost = cost;
        this.deliveryId = deliveryId;
        this.chargedReceipt = chargedReceipt;
        this.productIdToQuantity = new HashMap<>();
        productIdToQuantity.put(productId, quantity);
        this.purchaseError = null;
    }

    public PurchaseHistory(String purchaseError, int visitorId, int shopId) {
        this.purchaseError = purchaseError;
        this.visitorId = visitorId;
        this.shopId = shopId;
        this.cost = DEFAULT_ERROR_NUMBER;
        this.deliveryId = DEFAULT_ERROR_NUMBER;
        this.chargedReceipt = DEFAULT_ERROR_NUMBER;
        this.productIdToQuantity = new HashMap<>();
    }

    public PurchaseHistory() {

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

    public String getPurchaseError() {
        return purchaseError;
    }

    public boolean isErrorOccurred() {
        return purchaseError != null;
    }

    @Override
    public String toString() {
        if (!isErrorOccurred()) {
            return "PurchaseHistory{" +
                    "visitorId=" + visitorId +
                    ", shopId=" + shopId +
                    ", cost=" + cost +
                    ", deliveryId=" + deliveryId +
                    ", chargedReceipt=" + chargedReceipt +
                    ", productIdToQuantity=" + productIdToQuantity +
                    '}';
        } else
            return "PurchaseHistory{" +
                    "visitorId=" + visitorId +
                    ", shopId=" + shopId +
                    ", purchaseError='" + purchaseError + '\'' +
                    '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setChargedReceipt(int chargedReceipt) {
        this.chargedReceipt = chargedReceipt;
    }

    public Map<Integer, Integer> getProductIdToQuantity() {
        return productIdToQuantity;
    }

    public void setProductIdToQuantity(Map<Integer, Integer> productIdToQuantity) {
        this.productIdToQuantity = productIdToQuantity;
    }

    public void setPurchaseError(String purchaseError) {
        this.purchaseError = purchaseError;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseHistory that = (PurchaseHistory) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
