package com.example.seprojectweb.ScenarioTests;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Product;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }
    @DisplayName("product change quantity tests => Success")
    @ParameterizedTest(name = "{index} => id = {0}, pName = {1}, price = {2}, des = {3}, quantity = {4},change ={5},expected = {6} ")
    @MethodSource("pProvider1")
    void setQuantitySuccess(int id,String name,double price,String des, int quantity, int change,int expectedQuantity) throws InnerLogicException {
        Product p = new Product(id,name,price,des,quantity,"cat");
        p.addToQuantity(change);
        assertEquals(p.getQuantity(),expectedQuantity);
    }
    private static Stream<Arguments> pProvider1() {
        return Stream.of(
                Arguments.of(1,"item1",24, "item1 des", 25, 25,50),
                Arguments.of(2,"item1",24, "item1 des", 25, -5,20),
                Arguments.of(3,"item1",24, "item1 des", 25, -25,0),
                Arguments.of(4,"item1",24, "item1 des", 25, 0,25)
        );
    }
    @DisplayName("product change quantity tests => Fail")
    @ParameterizedTest(name = "{index} => id = {0}, pName = {1}, price = {2}, des = {3}, quantity = {4},change ={5}")
    @MethodSource("pProvider2")
    void setQuantityFail(int id,String name,double price,String des, int quantity, int change) {
        Product p = new Product(id,name,price,des,quantity,"cat");
        Throwable exception = assertThrows(InnerLogicException.class, () -> p.addToQuantity(change));
    }
    private static Stream<Arguments> pProvider2() {
        return Stream.of(
                Arguments.of(1,"item1",24, "item1 des", 25, -100),
                Arguments.of(2,"item1",24, "item1 des", 25, -26),
                Arguments.of(3,"item1",24, "item1 des", 0, -1)
        );
    }

}