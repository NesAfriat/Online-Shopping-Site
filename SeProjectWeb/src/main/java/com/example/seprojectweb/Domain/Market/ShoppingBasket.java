package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Entity
public class ShoppingBasket {

    @ElementCollection(fetch = FetchType.EAGER)
    //@OneToMany
    Map<Product, QuantityDiscount> products;             //key: Product, value: quantity of the product

    //Map<Product, Pair<Integer, Double>> products;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public ShoppingBasket() {
        products = new ConcurrentHashMap<>();
    }

    public void addProduct(Product prod, int quantity) {
        if (!products.containsKey(prod)) {
            products.put(prod, new QuantityDiscount(quantity, 0.0));
            //products.put(prod, new Pair<Integer, Double>(quantity, 0.0));

        } else {
            products.put(prod, new QuantityDiscount(products.get(prod).getQuantity() + quantity, 0.0));
            //products.put(prod, new Pair<Integer, Double>(products.get(prod).getKey() + quantity, 0.0));
        }
    }

//    public Set<Map.Entry<Product, Pair<Integer, Double>>> getProducts() {
//        return products.entrySet();
//    }

    public Set<Map.Entry<Product, QuantityDiscount>> getProducts() {
        return products.entrySet();
    }

    public Map.Entry<Product,QuantityDiscount> removeProduct(int productID) throws InnerLogicException {
        List<Product> lst = products.keySet().stream().filter(product -> product.getId() == productID).collect(Collectors.toList());
        if (lst.isEmpty()) {
            throw new InnerLogicException("tried to removing non exist product: " + productID + " in shopping basket");
        }
        if (lst.size() > 1) {
            throw new InnerLogicException("fatal double product in shopping basket");
        }
        return Map.entry(lst.get(0),products.remove(lst.get(0)));
    }

    public void updateProductDiscount(Product product, double percentage) {
        //Pair<Integer, Double> quantityDiscount = products.get(product);
        QuantityDiscount quantityDiscount = products.get(product);
        int quantity = quantityDiscount.getQuantity();
        double oldPercentageToPay = 1 - quantityDiscount.getDiscount();
        double newPercentageDiscount = 1 - (oldPercentageToPay * (1 - percentage));
        //quantityDiscount = new Pair<>(quantity, newPercentageDiscount);
        quantityDiscount = new QuantityDiscount(quantity,newPercentageDiscount );
        products.put(product, quantityDiscount);
    }

    public double getTotalPrice() {
        double output = 0;
        for (Map.Entry<Product, QuantityDiscount> entry : products.entrySet()) {
            double price = entry.getKey().getPrice();
            int quantity = entry.getValue().getQuantity();
            double payPercentage = 1 - entry.getValue().getDiscount();
            output += quantity * price * payPercentage;
        }
        return output;
    }

    public int getProductQuantity(Product product) {
        if (!products.containsKey(product)) {
            return 0;
        } else {
            return products.get(product).getQuantity();
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void clearDiscounts() {
        for (Map.Entry<Product, QuantityDiscount> entry : products.entrySet()) {
           QuantityDiscount quantityDiscount = entry.getValue();
           quantityDiscount.setDiscount(0.0); // reset the discount
        }
    }
}
