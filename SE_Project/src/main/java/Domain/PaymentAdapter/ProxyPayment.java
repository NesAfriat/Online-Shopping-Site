package Domain.PaymentAdapter;

public class ProxyPayment implements IPayment{
    IPayment paymentService;

    private static class PaymentAdapterHolder {
        private static final ProxyPayment instance = new ProxyPayment();
    }

    public static ProxyPayment getInstance(){
        return ProxyPayment.PaymentAdapterHolder.instance;
    }

    public int chargeCreditCard(String creditCardNumber, String date, String cvs, double cost){
        if (paymentService != null)
            return paymentService.chargeCreditCard(creditCardNumber, date, cvs, cost);

        return 1;
    }
}
