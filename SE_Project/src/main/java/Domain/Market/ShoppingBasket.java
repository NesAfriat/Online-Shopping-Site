package Domain.Market;

import Domain.InnerLogicException;
import javafx.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShoppingBasket {
    Map<Product, Pair<Integer,Double>> products;             //key: Product, value: quantity of the product

    public ShoppingBasket(){
        products = new ConcurrentHashMap<>();
    }

    public void addProduct(Product prod, int quantity) {
        if(!products.containsKey(prod)){
            products.put(prod,new Pair<Integer,Double>(quantity, 0.0));

        }else {
            products.put(prod,new Pair<Integer,Double>(products.get(prod).getKey()+quantity, 0.0));
        }
    }

    public Set<Map.Entry<Product, Pair<Integer, Double>>> getProducts(){
        return products.entrySet();
    }

    public void removeProduct(int productID) throws InnerLogicException {
        List<Product> lst = products.keySet().stream().filter(product -> product.getProductID() == productID).collect(Collectors.toList());
        if(lst.isEmpty()){
            throw new InnerLogicException("tried to removing non exist product: "+productID+" in shopping basket");
        }
        if(lst.size()>1){
            throw new InnerLogicException("fatal double product in shopping basket");
        }
        products.remove(lst.get(0));
    }

    public void updateProductDiscount(Product product, double percentage){
        Pair<Integer, Double> quantityDiscount = products.get(product);
        int quantity = quantityDiscount.getKey();
        double oldPercentageToPay = 1 - quantityDiscount.getValue();
        double newPercentageDiscount = 1 - (oldPercentageToPay * (1 - percentage));
        quantityDiscount = new Pair<>(quantity, newPercentageDiscount);
        products.put(product,quantityDiscount);
    }

    public double getTotalPrice(){
        double output = 0;
        for (Map.Entry<Product, Pair<Integer, Double>> entry: products.entrySet()) {
            double price = entry.getKey().getPrice();
            int quantity = entry.getValue().getKey();
            double payPercentage = 1 - entry.getValue().getValue();
            output += quantity * price * payPercentage;
        }
        return output;
    }

    public int getProductQuantity(Product product) {
        if (!products.containsKey(product)){
            return 0;
        }
        else{
            return products.get(product).getKey();
        }
    }
}
