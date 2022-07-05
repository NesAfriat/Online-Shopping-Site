package com.example.seprojectweb.Domain.PaymentAdapter;

import com.example.seprojectweb.Domain.DeliveryAdapter.IDelivey;

public class ProxyPayment implements IPayment {
    IPayment paymentService;

    public static ProxyPayment getInstance() {
        return PaymentAdapterHolder.instance;
    }

    public void setPaymentService(IPayment paymentService) {
        this.paymentService = paymentService;
    }

    public int chargeCreditCard(String creditCardNumber, String date, String holder, String cvs, double cost) {
        if (paymentService != null)
            return paymentService.chargeCreditCard(creditCardNumber, date, holder, cvs, cost);

        return 1;
    }

    @Override
    public void cancelPayment(int paymentId) {
        if (paymentService != null) {
            paymentService.cancelPayment(paymentId);
        }
    }

    private static class PaymentAdapterHolder {
        private static final ProxyPayment instance = new ProxyPayment();
    }
}
