package com.example.seprojectweb.ScenarioTests;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.Shop;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {
    Shop shop = new Shop(1);
    HashMap<Integer, Product> products;

    @BeforeEach
    void setUp() {
        Product p1 = new Product(1,"item1",25.5,"des item1",4,"cat1");
        Product p2 = new Product(2,"item2",22,"des item2",44,"cat2");
        Product p3 = new Product(3,"item3",22,"des item3",41,"cat3");
        HashMap<Integer,Product> products = new HashMap<>();
        products.put(1,p1);
        products.put(2,p2);
        products.put(3,p3);
        shop.insertPruducts(products);
    }
    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }
    @AfterEach
    void tearDown() {
        shop.clear();
    }

    @DisplayName("shop change product quantity tests  => Success")
    @ParameterizedTest(name = "{index} => id = {0}, pName = {1}, price = {2}, des = {3}, quantity = {4}")
    @MethodSource("pProvider1")
    void addItemSuccess(int id,String name,double price,String des, int quantity) throws InnerLogicException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();
        Product p = shop.addProduct(name, price, des, quantity,"cat",dbConnection);
        assertEquals(p,shop.getProduct(id));
        dbConnection.commitTransaction();
        dbConnection.closeConnections();
    }
    private static Stream<Arguments> pProvider1() {
        return Stream.of(
                Arguments.of(4, "item12", 24, "item13 des",22)
        );
    }

    @DisplayName("Shop change product name=> Success")
    @ParameterizedTest(name = "{index} => id = {0}, new name = {1}")
    @MethodSource("pProvider3")
    void changeProductName(int id, String newName) throws InnerLogicException {
        shop.changeProductName(id,newName);
        assertEquals(newName,shop.getProduct(id).getProductName());
    }
    private static Stream<Arguments> pProvider3() {
        return Stream.of(
                Arguments.of(1,"item11"),
                Arguments.of(2,"item22"),
                Arguments.of(3,"item33")
        );
    }



    @DisplayName("Shop change product description=> Success")
    @ParameterizedTest(name = "{index} => id = {0}, new name = {1}")
    @MethodSource("pProvider6")
    void changeProductDes(int id, String newDes) throws InnerLogicException {
        shop.getProduct(id).setDescription(newDes);
        assertEquals(newDes,shop.getProduct(id).getDescription());
    }
    private static Stream<Arguments> pProvider6() {
        return Stream.of(
                Arguments.of(1,"item11"),
                Arguments.of(2,"item22"),
                Arguments.of(3,"item33")
        );
    }
    @DisplayName("Shop change product Price=> Success")
    @ParameterizedTest(name = "{index} => id = {0}, new name = {1}")
    @MethodSource("pProvider7")
    void changeProductPrice(int id, int newPrice) throws InnerLogicException {
        shop.changeProductPrice(id,newPrice);
        assertEquals(newPrice,shop.getProduct(id).getPrice());
    }
    private static Stream<Arguments> pProvider7() {
        return Stream.of(
                Arguments.of(1,7),
                Arguments.of(2,8),
                Arguments.of(3,9)
        );
    }

    @DisplayName("Shop remove product=> Success")
    @ParameterizedTest(name = "{index} => id = {0}")
    @MethodSource("pProvider4")
    void removeProductSuccess(int id) throws InnerLogicException {
        shop.removeProduct(id);
        assertFalse(shop.containsProduct(id));
    }
    private static Stream<Arguments> pProvider4() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3)
        );
    }
    @DisplayName("Shop remove product=> Fail")
    @ParameterizedTest(name = "{index} => id = {0}")
    @MethodSource("pProvider5")
    void removeProductFail(int id){
        Throwable exception = assertThrows(InnerLogicException.class, () -> shop.removeProduct(id));

    }
    private static Stream<Arguments> pProvider5() {
        return Stream.of(
                Arguments.of(4),
                Arguments.of(5),
                Arguments.of(-1)
        );
    }


}