package Domain.Market.Notifications;

public interface IShopObserver {

    void sendNotification(ShopNotification shopNotification);

}
