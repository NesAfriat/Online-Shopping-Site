package ScenarioTests;

import Domain.InnerLogicException;
import Domain.Market.Product;
import Domain.Market.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchasePolicyTest {

    Shop shop;


    @BeforeEach
    void setUp() throws InnerLogicException {
        shop = new Shop(1);
        shop.addProduct("item1",25.5,"des item1",4,"cat1");
        shop.addProduct("item2",22,"des item2",44,"cat2");
        shop.addProduct("item3",22,"des item3",41,"cat3");


    }





    @DisplayName("shop change product quantity tests  => Success")
    @ParameterizedTest(name = "{index} => id = {0}, pName = {1}, price = {2}, des = {3}, quantity = {4}")
    @MethodSource("pProvider1")
    void addDiscount(int id,String name,double price,String des, int quantity) throws InnerLogicException {
        Product p = shop.addProduct(name, price, des, quantity,"cat");
        assertEquals(p,shop.getProduct(id));
    }
    private static Stream<Arguments> pProvider1() {
        return Stream.of(
                Arguments.of(4, "item1", 24, "item1 des",22),
                Arguments.of(4, "item2", 24, "item2 des",23),
                Arguments.of(4, "item3", 24, "item3 des",25)
        );
    }
}
