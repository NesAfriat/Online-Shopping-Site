package Domain.Market.Notifications;

import java.util.Date;

public class ShopNotification extends Notification {

    private final int shopId;

    public ShopNotification(int shopId, String message) {
        super(message);
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }

    @Override
    public String toString() {
        return "ShopNotification{" +
                "shopId=" + shopId +
                "message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
