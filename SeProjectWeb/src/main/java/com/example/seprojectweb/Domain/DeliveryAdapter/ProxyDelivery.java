package com.example.seprojectweb.Domain.DeliveryAdapter;

import org.springframework.web.client.RestTemplate;

public class ProxyDelivery implements IDelivey {

    IDelivey deliveryService;

    public static ProxyDelivery getInstance() {
        return DeliveryAdapterHolder.instance;
    }

    public void setDeliveryService(IDelivey deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * @return delivery id > 0 if success, -1 if fail
     * FOR NOW DUMMY IMPL
     */
    public int bookDelivery(String name, String country, String city, String address, String zip) {
        if (deliveryService != null) {
            return deliveryService.bookDelivery(name, country, city, address, zip);
        }
        return 1;
    }

    public void cancelDelivery(int deliveryId) {
        if (deliveryService != null) {
            deliveryService.cancelDelivery(deliveryId);
        }
    }

    private static class DeliveryAdapterHolder {
        private static final ProxyDelivery instance = new ProxyDelivery();
    }
}
