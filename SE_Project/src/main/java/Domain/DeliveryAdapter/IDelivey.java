package Domain.DeliveryAdapter;

public interface IDelivey {

    int bookDelivery(String country, String city, String address);

    void cancelDelivery(int deliveryId);
}
