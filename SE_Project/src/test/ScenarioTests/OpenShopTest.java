package ScenarioTests;

import AcceptanceTests.MemberObserverForTests;
import Domain.InnerLogicException;
import Domain.Market.Shop;
import Domain.Market.ShopController;
import Domain.Market.Visitor;
import Domain.Market.VisitorController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OpenShopTest {

    ShopController shopController;
    VisitorController visitorController;

    @BeforeEach
    void setUp() {
        shopController = ShopController.getInstance();
    }

    @AfterEach
    void tearDown() {
        shopController.clear();
    }
    @DisplayName("open shop tests  => Success")
    @ParameterizedTest(name = "{index} => id = {0}, FID = {1}, FPhone = {2}, FCC = {3}, sName = {4}, sDesc = {5}, sLocation = {6}")
    @MethodSource("pProvider1")
    void openShopSuccess(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) throws InnerLogicException {
        Shop s = shopController.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
        assertEquals(s.getName(), shopName);

    }
    private static Stream<Arguments> pProvider1() throws InnerLogicException {
        VisitorController visitorController = VisitorController.getInstance();
        Visitor v1 = visitorController.visitSystem();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        visitorController.register(v1.getId(), "idan", "123");
        visitorController.login(v1.getId(), "idan", "123", memberObserver);
        return Stream.of(
                Arguments.of(v1.getId(),"0545425955","5555","idan-shop","we sell comics","Beer Sheva")
        );
    }

    @DisplayName("open shop tests  => Failed (shop already exist")
    @ParameterizedTest(name = "{index} => id = {0}, FID = {1}, FPhone = {2}, FCC = {3}, sName = {4}, sDesc = {5}, sLocation = {6}")
    @MethodSource("pProvider2")
    void openShopFailedExist(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) throws InnerLogicException {
        //for commit
        try{
            Shop s = shopController.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
            shopController.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
        }catch(InnerLogicException e){
            assertTrue(true);
        }catch(Exception e){
            fail();
        }
    }
    private static Stream<Arguments> pProvider2() {
        return Stream.of(
                Arguments.of(2,"0545425955","5555","idan-shop","we sell comics","Beer Sheva")
        );
    }

    @DisplayName("open shop tests  => Failed (shop name empty or spaces")
    @ParameterizedTest(name = "{index} => id = {0}, FID = {1}, FPhone = {2}, FCC = {3}, sName = {4}, sDesc = {5}, sLocation = {6}")
    @MethodSource("pProvider3")
    void openShopFailedEmptyName(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) throws InnerLogicException {
        try{
            shopController.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
        }catch(InnerLogicException e){
            assertTrue(true);
        }catch(Exception e){
            fail();
        }
    }
    private static Stream<Arguments> pProvider3() {
        return Stream.of(
                Arguments.of(2,"0545425955","5555","","we sell comics","Beer Sheva"),
                Arguments.of(2,"0545425955","5555","         ","we sell comics","Beer Sheva")
        );
    }

    @DisplayName("open shop tests  => Failed (shop desc empty or spaces")
    @ParameterizedTest(name = "{index} => id = {0}, FID = {1}, FPhone = {2}, FCC = {3}, sName = {4}, sDesc = {5}, sLocation = {6}")
    @MethodSource("pProvider4")
    void openShopFailedEmptyDesc(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) throws InnerLogicException {
        try{
            shopController.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
        }catch(InnerLogicException e){
            assertTrue(true);
        }catch(Exception e){
            fail();
        }
    }
    private static Stream<Arguments> pProvider4() {
        return Stream.of(
                Arguments.of(2,"0545425955","5555","idan-shop","","Beer Sheva"),
                Arguments.of(2,"0545425955","5555","idan-shop-2","     ","Beer Sheva")
        );
    }
}
