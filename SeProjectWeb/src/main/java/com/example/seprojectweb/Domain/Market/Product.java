package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.PersistenceManager;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Objects;

@Entity
public class Product {


//    @Id
//    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String productName;
    private double price;
    private String description;
    private int quantity;
    private String category;

    @Transient
    private boolean lock;


    @Transient
    LinkedList<Double> rates;


    public Product(String productName, double price, String description, int quantity, String category) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.rates = new LinkedList<>();
        this.category = category;
        this.lock = false;
    }

    // @DEPRECATED CONSTRUCTOR. STILL HERE ONLY BECAUSE NEED TO REFACTOR TESTS USING THIS CONSTRUCTOR
    public Product(int ignor, String productName, double price, String description, int quantity, String category) {
        this.id = ignor;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.rates = new LinkedList<>();
        this.category = category;
        this.lock = false;
    }

    public Product() {
        this.lock = false;
        this.rates = new LinkedList<>();

    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) throws InnerLogicException {
        this.category = category;
        PersistenceManager.updateSession(this);
    }

    @Transient
    public LinkedList<Double> getRates() {
        return rates;
    }

    public void setRates(LinkedList<Double> rates) {
        this.rates = rates;
    }

    /***
     *
     * @param rate its not the new rate! its calculate the average of the rate!
     */
    public void addRate(double rate) {
        this.rates.add(rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, price, description, quantity);
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void addToQuantity(int quantity) throws InnerLogicException {
        int newQuantity = this.quantity + quantity;
        if (newQuantity < 0) {
            throw new InnerLogicException("the number of products is lower then the number to reduce");
        }
        this.quantity = newQuantity;
        PersistenceManager.updateSession(this);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws InnerLogicException {
        if (quantity < 0) {
            throw new InnerLogicException("the number of products is lower then the number to reduce");
        }
        this.quantity = quantity;
    }
    public void setQuantityWithOutCheck(int quantity){
        this.quantity = quantity;
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
