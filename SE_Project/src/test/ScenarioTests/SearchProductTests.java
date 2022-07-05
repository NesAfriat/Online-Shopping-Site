package ScenarioTests;

import Domain.Market.Product;
import Domain.Market.Shop;
import Domain.Market.ShopController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SearchProductTests {

    ShopController sc = ShopController.getInstance();
    HashMap<Integer, Shop> shops = new HashMap<>();
    Shop shop1 = new Shop(1);
    Shop shop2 = new Shop(2);
    Shop shop3 = new Shop(3);
    Product p1 = new Product(1,"item1",25.5,"des item1",4,"cat1");
    Product p2 = new Product(2,"item2",22,"des item2",44,"cat2");
    Product p3 = new Product(3,"item3",22,"des item3",41,"cat6");
    Product p4 = new Product(4,"item1",25.5,"des item4",4,"cat4");
    Product p5 = new Product(5,"item4",22,"des item5",44,"cat5");
    Product p6 = new Product(6,"item5",22,"des item6",41,"cat6");
    Product p7 = new Product(7,"item6",25.5,"des item7",4,"cat1");
    Product p8 = new Product(8,"item1",22,"des item8",44,"cat6");
    Product p9 = new Product(9,"item7",22,"des item9",41,"cat9");
    @BeforeEach
    void setUp() {

        HashMap<Integer,Product> products1 = new HashMap<>();
        HashMap<Integer,Product> products2 = new HashMap<>();
        HashMap<Integer,Product> products3 = new HashMap<>();
        products1.put(1,p1);
        products1.put(2,p2);
        products1.put(3,p3);
        products2.put(4,p4);
        products2.put(5,p5);
        products2.put(6,p6);
        products3.put(7,p7);
        products3.put(8,p8);
        products3.put(9,p9);
        shop1.set(products1);
        shop2.set(products2);
        shop3.set(products3);
        shops.put(1,shop1);
        shops.put(2,shop2);
        shops.put(3,shop3);
        sc.set(shops);
    }

    @AfterEach
    void tearDown() {
        shops.clear();
    }

    @Test
    @DisplayName("search product by name")
    void searchProductByName() {
        HashMap<Shop,List<Product>> productsAndShops =  sc.searchProductByName(1,"item2");
        int loop = 0;
        for (Map.Entry<Shop,List<Product>> entry: productsAndShops.entrySet()){
            assertEquals(entry.getKey().getShopId(),(shop1.getShopId()));
            for (Product p: entry.getValue()){
                assertEquals(p,p2);
                loop ++;
            }
        }
        assertEquals(loop, 1);
        HashMap<Shop,List<Product>> productsAndShops2 =  sc.searchProductByName(1,"item");
        assertTrue(productsAndShops2.isEmpty());
        HashMap<Shop,List<Product>> productsAndShops3 =  sc.searchProductByName(1,"    ");
        assertTrue(productsAndShops3.isEmpty());
    }

    @Test
    void searchProductByKeyWord() {
    }

}