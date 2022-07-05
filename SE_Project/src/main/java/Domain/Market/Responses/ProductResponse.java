package Domain.Market.Responses;

import Domain.Market.Product;

import java.util.LinkedList;
import java.util.Objects;

public class ProductResponse {
    private int id;
    private String productName;
    private double price;
    private String description;
    private int quantity;
    private int shopId;
    private LinkedList<Double> rates;
    private String category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return id == that.id &&
                Double.compare(that.price, price) == 0 &&
                quantity == that.quantity &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, price, description, quantity);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }

//    public ProductResponse(int id, int shopId, String productName, double price, String description, int quantity,String category) {
//        this.shopId = shopId;
//        this.id = id;
//        this.productName = productName;
//        this.price = price;
//        this.description = description;
//        this.quantity = quantity;
//        this.category = category;
//    }

    public ProductResponse(int shopID, Product prod){
        shopId = shopID;
        id = prod.getId();
        productName = prod.getProductName();
        price = prod.getPrice();
        description = prod.getDescription();
        quantity = prod.getQuantity();
        category = prod.getCategory();
        rates = prod.getRates();
    }
}
