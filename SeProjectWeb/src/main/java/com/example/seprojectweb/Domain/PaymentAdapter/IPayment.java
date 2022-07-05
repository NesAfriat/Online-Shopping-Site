package com.example.seprojectweb.Domain.PaymentAdapter;

public interface IPayment {

    int chargeCreditCard(String creditCardNumber, String date, String holder, String cvs, double cost);

    void cancelPayment(int paymentId);
}
