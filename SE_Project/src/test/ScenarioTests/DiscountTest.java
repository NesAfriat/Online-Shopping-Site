package ScenarioTests;

import Domain.InnerLogicException;
import Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import Domain.Market.Product;
import Domain.Market.Shop;
import Domain.Market.ShoppingBasket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountTest {
    Shop shop;
    private static int productId1 = 1;
    private static int productId2 = 2;
    private static int productId3 = 3;


    @BeforeEach
    void setUp() throws InnerLogicException {
        shop = new Shop(1);
        productId1 = shop.addProduct("item1",25.5,"des item1",4,"cat1").getProductID();
        productId2 = shop.addProduct("item2",22,"des item2",44,"cat2").getProductID();
        productId3 = shop.addProduct("item3",22,"des item3",41,"cat3").getProductID();


    }




    @DisplayName("add new discount => Success")
    @ParameterizedTest(name = "{index} => percentage = {0}, lastValidDate = {1}, productId = {2}")
    @MethodSource("pProvider1")
    void addProductDiscount(double percentage, Date lastValidDate, int productId) throws InnerLogicException {
        int quantity = 2;
        Product product = shop.getProduct(productId);
        shop.addProductDiscount(percentage, lastValidDate, productId);
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.addProduct(product, quantity);
        shop.applyDiscounts(shoppingBasket);
        double basketPrice = shoppingBasket.getTotalPrice();
        assertEquals(product.getPrice() * quantity * (1-percentage), basketPrice);
    }
    private static Stream<Arguments> pProvider1() {
        Date today = new Date();
        Date future = new Date(today.getTime() + 100000000);
        return Stream.of(
                Arguments.of(0.2, future,productId1),
                Arguments.of(0.3, future,productId2),
                Arguments.of(0.4, future,productId3)
        );
    }






    @DisplayName("add new discount => fail - illegal product id")
    @ParameterizedTest(name = "{index} => percentage = {0}, lastValidDate = {1}, productId = {2}")
    @MethodSource("pProvider2")
    void addProductDiscount_fail_illegal_product_id(double percentage, Date lastValidDate, int productId){
        int quantity = 2;
        try {
            Product product = shop.getProduct(productId);
            shop.addProductDiscount(percentage, lastValidDate, productId + 100);
            fail();
        }catch (InnerLogicException e){
            assertTrue(true);
        }
    }
    private static Stream<Arguments> pProvider2() {
        Date today = new Date();
        Date future = new Date(today.getTime() + 100000000);
        return Stream.of(
                Arguments.of(0.2, future,productId1)
        );
    }







    @DisplayName("add new discount => Success")
    @ParameterizedTest(name = "{index} => percentage = {0}, lastValidDate = {1}, productId = {2}")
    @MethodSource("pProvider3")
    void addProductDiscount(double percentage, Date lastValidDate, int productId, String addCategoryDiscount){
        int quantity = 2;
        try{
            Product product = shop.getProduct(productId);
            shop.addCategoryDiscount(percentage, lastValidDate, addCategoryDiscount);
            ShoppingBasket shoppingBasket = new ShoppingBasket();
            shoppingBasket.addProduct(product, quantity);
            shop.applyDiscounts(shoppingBasket);
            double basketPrice = shoppingBasket.getTotalPrice();
            assertEquals(product.getPrice() * quantity * (1-percentage), basketPrice);
        }catch (InnerLogicException e){
            fail();
        }

    }
    private static Stream<Arguments> pProvider3() {
        Date today = new Date();
        Date future = new Date(today.getTime() + 100000000);
        return Stream.of(
                Arguments.of(0.2, future,productId1, "cat1"),
                Arguments.of(0.3, future,productId2, "cat2"),
                Arguments.of(0.4, future,productId3, "cat3")
        );
    }


    @Test
    void andDiscount(){
        int quantity1 = 2;
        int quantity2 = 4;
        int quantity3 = 6;
        Date today = new Date();
        Date future = new Date(today.getTime() + 100000000);
        double percentage = 0.2;
        try{
            Product product1 = shop.getProduct(productId1);
            Product product2 = shop.getProduct(productId2);
            Product product3 = shop.getProduct(productId3);
            int discountId = shop.addCategoryDiscount(percentage, future, "cat1").getDiscountId();
            shop.addPQCondition(CONDITION_COMPOSE_TYPE.RESET, discountId, productId2, 2);
            shop.addPQCondition(CONDITION_COMPOSE_TYPE.AND, discountId, productId3, 3);
            ShoppingBasket shoppingBasket = new ShoppingBasket();
            shoppingBasket.addProduct(product1, quantity1);
            shoppingBasket.addProduct(product2, quantity2);
            shoppingBasket.addProduct(product3, quantity3);
            shop.applyDiscounts(shoppingBasket);
            double basketPrice = shoppingBasket.getTotalPrice();
            double expectedPrice = product1.getPrice() * quantity1 * (1-percentage);
            expectedPrice += product2.getPrice() * quantity2;
            expectedPrice += product3.getPrice() * quantity3;
            assertEquals(expectedPrice, basketPrice);
        }catch (InnerLogicException e){
            fail();
        }
    }





    @Test
    void andDiscount_not_satisfy_the_condition(){
        int quantity1 = 2;
        int quantity2 = 4;
        int quantity3 = 1;
        Date today = new Date();
        Date future = new Date(today.getTime() + 100000000);
        double percentage = 0.2;
        try{
            Product product1 = shop.getProduct(productId1);
            Product product2 = shop.getProduct(productId2);
            Product product3 = shop.getProduct(productId3);
            int discountId = shop.addCategoryDiscount(percentage, future, "cat1").getDiscountId();
            shop.addPQCondition(CONDITION_COMPOSE_TYPE.RESET, discountId, productId2, 2);
            shop.addPQCondition(CONDITION_COMPOSE_TYPE.AND, discountId, productId3, 3);
            ShoppingBasket shoppingBasket = new ShoppingBasket();
            shoppingBasket.addProduct(product1, quantity1);
            shoppingBasket.addProduct(product2, quantity2);
            shoppingBasket.addProduct(product3, quantity3);
            shop.applyDiscounts(shoppingBasket);
            double basketPrice = shoppingBasket.getTotalPrice();
            double expectedPrice = product1.getPrice() * quantity1;
            expectedPrice += product2.getPrice() * quantity2;
            expectedPrice += product3.getPrice() * quantity3;
            double expectedDPrice = product1.getPrice() * quantity1 * (1-percentage);
            expectedDPrice += product2.getPrice() * quantity2;
            expectedDPrice += product3.getPrice() * quantity3;
            assertNotEquals(expectedDPrice, basketPrice);
            assertEquals(expectedPrice, basketPrice);
        }catch (InnerLogicException e){
            fail();
        }
    }



}
