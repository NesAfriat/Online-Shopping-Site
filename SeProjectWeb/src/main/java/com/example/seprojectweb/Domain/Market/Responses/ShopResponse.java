package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.Shop;

import java.util.HashMap;
import java.util.Map;

public class ShopResponse {
    private final int id;
    private final String Founder;
    private final int FounderID;
    private final String FounderPhone;
    private final String FounderCreditCard;
    private final boolean isOpen;
    private final String name;
    private final String description;
    private final String location;
    private final Map<Integer, ProductResponse> products;


    public ShopResponse(Shop shop) {
        id = shop.getShopId();
        Founder = shop.getFounder();
        FounderID = shop.getFounderID();
        FounderPhone = shop.getPhone();
        FounderCreditCard = shop.getCreditCard();
        isOpen = shop.isOpen();
        name = shop.getName();
        description = shop.getDescription();
        location = shop.getLocation();
        products = convertShopProducts(shop);
    }

    private Map<Integer, ProductResponse> convertShopProducts(Shop shop) {
        Map<Integer, Product> products = shop.getProducts();
        Map<Integer, ProductResponse> returnP = new HashMap<>();
        for (Product prod : products.values()) {
            returnP.put(prod.getId(), new ProductResponse(shop.getShopId(), prod));
        }

        return returnP;
    }

    public int getId() {
        return id;
    }

    public String getFounder() {
        return Founder;
    }

    public int getFounderID() {
        return FounderID;
    }

    public String getFounderPhone() {
        return FounderPhone;
    }

    public String getFounderCreditCard() {
        return FounderCreditCard;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Map<Integer, ProductResponse> getProducts() {
        return products;
    }
}
