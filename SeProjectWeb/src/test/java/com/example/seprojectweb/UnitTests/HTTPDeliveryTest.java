package com.example.seprojectweb.UnitTests;

import com.example.seprojectweb.Domain.DeliveryAdapter.HTTPDelivery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HTTPDeliveryTest {

    private HTTPDelivery httpDelivery;
    String goodCountry;
    String badCountry;
    String goodCity;
    String badCity;
    String goodName;
    String badName;
    String goodAddress;
    String badAddress;
    String goodZip;
    String badZip;

    @BeforeEach
    void setup() {

        goodZip = "8458527";
        goodAddress = "Maccabim";
        goodCountry = "Israel";
        goodCity = "Shoham";
        goodName = "idan rozenberg";

        badZip = "";
        badAddress = "";
        badCountry = "";
        badCity = "";
        badName = "";
    }

    @Test
    @DisplayName("HTTP handshake success")
    void handshake_test() {
        try {
            httpDelivery = new HTTPDelivery();
        } catch (Exception e) {
            System.out.println("Error message: "+e.getMessage());
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("HTTP delivery success")
    void delivery_test() {
        try {
            httpDelivery = new HTTPDelivery();
            Integer res = httpDelivery.bookDelivery(goodCountry, goodCity, goodName, goodAddress, goodZip);
            Assertions.assertTrue(res > 0);
        } catch (Exception e) {
            System.out.println("Error message: "+e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("HTTP delivery cancel")
    void cancel_test() {
        try {
            httpDelivery = new HTTPDelivery();
            Integer res = httpDelivery.bookDelivery(goodCountry, goodCity, goodName, goodAddress, goodZip);
            Assertions.assertTrue(res > 0);
            httpDelivery.cancelDelivery(res);
        } catch (Exception e) {
            System.out.println("Error message: "+e.getMessage());
            Assertions.fail();
        }
    }
}
