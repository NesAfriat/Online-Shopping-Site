package Domain.Market;

import Domain.InnerLogicException;

import java.util.LinkedList;
import java.util.Objects;

public class Product {
    private int id;
    private String productName;
    private double price;
    private String description;
    private int quantity;
    private String category;
    LinkedList<Double> rates;
    private boolean lock;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Double.compare(product.price, price) == 0 &&
                quantity == product.quantity &&
                productName.equals(product.productName) &&
                description.equals(product.description);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public LinkedList<Double> getRates() {
        return rates;
    }

    /***
     *
     * @param rate its not the new rate! its calculate the average of the rate!
     */
    public void setRates(double rate) {
        this.rates.add(rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, price, description, quantity);
    }



    public Product(int id, String productName, double price, String description,int quantity,String category){
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.rates = new LinkedList<>();
        this.category = category;
        this.lock = false;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getId() {
        return id;
    }
    public void setQuantity(int quantity) throws InnerLogicException {
        if (quantity < 0){
            throw new InnerLogicException("the number of products is lower then the number to reduce");
        }
        this.quantity = quantity;
    }

    public void addToQuantity(int quantity) throws InnerLogicException {
        int newQuantity = this.quantity + quantity;
        if (newQuantity < 0){
            throw new InnerLogicException("the number of products is lower then the number to reduce");
        }
        this.quantity = newQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getProductID() {
        return id;
    }

    /**
     * lock must acquire before changing the quantity
     */
    public synchronized void acquire() {
        while (lock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        lock = true;
    }

    public synchronized void release() {
        lock = false;
        notifyAll();
    }
}
