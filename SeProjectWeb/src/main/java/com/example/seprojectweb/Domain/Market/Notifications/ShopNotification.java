package com.example.seprojectweb.Domain.Market.Notifications;

import javax.persistence.Entity;

@Entity
public class ShopNotification extends Notification {

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    private int shopId;

    public ShopNotification(int shopId, String message) {
        super(message);
        this.shopId = shopId;
    }

    public ShopNotification() {

    }

    public int getShopId() {
        return shopId;
    }

    @Override
    public String toString() {
        return "ShopNotification{" +
                "shopId=" + shopId +
                "message='" + super.getMessage() + '\'' +
                ", createdAt=" + super.getCreatedAt() +
                '}';
    }
}
