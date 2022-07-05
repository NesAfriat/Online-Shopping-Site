package com.example.seprojectweb.UnitTests;

import com.example.seprojectweb.Domain.PaymentAdapter.HTTPPayment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HTTPPaymentTest {

    private HTTPPayment httpPayment;

    String goodCreditCard;
    String badCreditCard;
    String goodDate;
    String badDate;
    String goodHolder;
    String badHolder;
    String goodCvs;
    String badCvs;
    double goodCost;
    double badCost;

    @BeforeEach
    void setup() {
        httpPayment = null;

        goodCreditCard = "1234567890";
        goodDate = "2/26";
        goodHolder = "Idan Rozenberg";
        goodCvs = "123";
        goodCost = 10.5;

        badCreditCard = "";
        badDate = "2/20";
        badHolder = "";
        badCvs = "";
        badCost = -10.5;


    }

    @Test
    @DisplayName("HTTP handshake success")
    void handshake_test() {
        try {
            httpPayment = new HTTPPayment();
        } catch (Exception e) {
            System.out.println("Error message: "+e.getMessage());
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("HTTP payment success")
    void payment_test() {
        try {
            httpPayment = new HTTPPayment();
            Integer res = httpPayment.chargeCreditCard(goodCreditCard, goodDate, goodHolder, goodCvs, goodCost);
            Assertions.assertTrue(res > 0);
        } catch (Exception e) {
            System.out.println("Error message: "+e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("HTTP payment cancel")
    void cancel_test() {
        try {
            httpPayment = new HTTPPayment();
            Integer res = httpPayment.chargeCreditCard(goodCreditCard, goodDate, goodHolder, goodCvs, goodCost);
            Assertions.assertTrue(res > 0);
            httpPayment.cancelPayment(res);
        } catch (Exception e) {
            System.out.println("Error message: "+e.getMessage());
            Assertions.fail();
        }
    }
}
