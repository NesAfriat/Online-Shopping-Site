package com.example.seprojectweb.Domain.DeliveryAdapter;

public interface IDelivey {

    int bookDelivery(String name, String country, String city, String address, String zip);

    void cancelDelivery(int deliveryId);
}
