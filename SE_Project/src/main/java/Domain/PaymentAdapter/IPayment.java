package Domain.PaymentAdapter;

public interface IPayment {

    int chargeCreditCard(String creditCardNumber, String date, String cvs, double cost);
}
