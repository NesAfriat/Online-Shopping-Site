package com.example.seprojectweb.Domain.Market.Notifications;

import javax.persistence.Entity;

@Entity

public class BidNotification extends Notification{

    private int bidId;

    public BidNotification(int bidId, String message) {
        super(message);
        this.bidId = bidId;
    }

    public BidNotification() {

    }

    @Override
    public String toString() {
        return "BidNotification{" +
                "bidId=" + bidId +
                "message='" + super.getMessage() + '\'' +
                ", createdAt=" + super.getCreatedAt() +
                '}';
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }
}
