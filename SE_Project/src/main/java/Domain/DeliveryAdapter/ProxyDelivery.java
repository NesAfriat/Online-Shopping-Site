package Domain.DeliveryAdapter;

public class ProxyDelivery implements IDelivey {
    IDelivey deliveryService;


    private static class DeliveryAdapterHolder {
        private static final ProxyDelivery instance = new ProxyDelivery();
    }

    public static ProxyDelivery getInstance(){
        return ProxyDelivery.DeliveryAdapterHolder.instance;
    }

    public void setDeliveryService(IDelivey deliveryService){
        this.deliveryService = deliveryService;
    }

    /**
     * @return delivery id > 0 if success, -1 if fail
     * FOR NOW DUMMY IMPL
     */
    public int bookDelivery(String country, String city, String address){
        if (deliveryService != null){
            return deliveryService.bookDelivery(country, city, address);
        }
        return 1;
    }

    public void cancelDelivery(int deliveryId){
        if (deliveryService != null){
            deliveryService.cancelDelivery(deliveryId);
        }
    }
}
