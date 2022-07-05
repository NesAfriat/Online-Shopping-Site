package com.example.seprojectweb.ScenarioTests;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.Market.Shop;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchasePolicyTest {

    Shop shop;
    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }

    @BeforeEach
    void setUp() throws InnerLogicException {
        shop = new Shop(1);
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();

        shop.addProduct("item1",25.5,"des item1",4,"cat1",dbConnection);
        shop.addProduct("item2",22,"des item2",44,"cat2",dbConnection);
        shop.addProduct("item3",22,"des item3",41,"cat3",dbConnection);


        dbConnection.commitTransaction();
        dbConnection.closeConnections();


    }





    @DisplayName("shop change product quantity tests  => Success")
    @ParameterizedTest(name = "{index} => id = {0}, pName = {1}, price = {2}, des = {3}, quantity = {4}")
    @MethodSource("pProvider1")
    void addDiscount(int id,String name,double price,String des, int quantity) throws InnerLogicException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();

        Product p = shop.addProduct(name, price, des, quantity,"cat",dbConnection);

        dbConnection.commitTransaction();
        dbConnection.closeConnections();

        assertEquals(p,shop.getProduct(p.getId()));
    }
    private static Stream<Arguments> pProvider1() {
        return Stream.of(
                Arguments.of(4, "item15", 24, "item15 des",22)
        );
    }
}
