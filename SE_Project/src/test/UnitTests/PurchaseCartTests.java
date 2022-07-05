package UnitTests;

import Domain.DeliveryAdapter.ProxyDelivery;
import Domain.Market.*;
import Domain.PaymentAdapter.ProxyPayment;
import javafx.util.Pair;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PurchaseCartTests {

    private PurchaseCashier purchaseCashier = PurchaseCashier.getInstance();

    private Visitor visitorMock;
    private Member memberMock;
    private ShoppingCart shoppingCartMock;
    private ShoppingBasket shoppingBasketMock;
    private Shop shopMock;
    private Product productMock;
    private VisitorController visitorControllerMock;
    private ShopController shopControllerMock;
    private RoleController roleControllerMock;
    private ProxyDelivery proxyDeliveryMock;
    private ProxyPayment proxyPaymentMock;


    @BeforeEach
    void setUp(){
        visitorMock = mock(Visitor.class);
        memberMock = mock(Member.class);
        shoppingCartMock = mock(ShoppingCart.class);
        shoppingBasketMock = mock(ShoppingBasket.class);
        shopMock = mock(Shop.class);
        productMock = mock(Product.class);
        visitorControllerMock = mock(VisitorController.class);
        shopControllerMock = mock(ShopController.class);
        roleControllerMock = mock(RoleController.class);
        proxyDeliveryMock = mock(ProxyDelivery.class);
        proxyPaymentMock = mock(ProxyPayment.class);

        when(visitorMock.getId()).thenReturn(1);
        when(visitorMock.getShoppingCart()).thenReturn(shoppingCartMock);
        ConcurrentHashMap<Integer, ShoppingBasket> basketsMap = new ConcurrentHashMap<>();
        basketsMap.put(1, shoppingBasketMock);
        when(visitorMock.getBaskets()).thenReturn(basketsMap.entrySet());
        when(productMock.getProductID()).thenReturn(1);
        when(productMock.getPrice()).thenReturn(20.0);
        when(productMock.getQuantity()).thenReturn(5);
        ConcurrentHashMap<Product, Pair<Integer, Double>> productsMap = new ConcurrentHashMap<>();
        productsMap.put(productMock, new Pair<>(1, 0.));
        when(shoppingBasketMock.getProducts()).thenReturn(productsMap.entrySet());
        when(proxyDeliveryMock.bookDelivery("Israel","Ashdod","Malahim")).thenReturn(1);
        when(proxyPaymentMock.chargeCreditCard("123456789","11/26","123",productMock.getPrice()*productsMap.get(productMock).getValue())).thenReturn(1);
        when(shopControllerMock.validatePurchasePolicy(1, visitorMock.getId(), shoppingBasketMock)).thenReturn(true);
        when(shopControllerMock.calculateShopDiscount(1, visitorMock.getId(),shoppingBasketMock)).thenReturn(0.0);
        try{
            when(shopControllerMock.verifyOpenShop(1)).thenReturn(true);
            when(visitorControllerMock.getVisitor(1)).thenReturn(visitorMock);
            when(visitorControllerMock.getBaskets(1)).thenReturn(basketsMap.entrySet());
        }catch(Exception e){
            // do nothing
        }
    }

    @Test
    @DisplayName("PurchaseCart Success")
    void purchaseCartSuccess1(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertFalse(res.get(0).isErrorOccurred());
            Assertions.assertEquals(res.get(0).getCost(), productMock.getPrice());
            Assertions.assertFalse(purchaseCashier.getPurchasesShop(1).isEmpty());
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("PurchaseCart Success - Full discount")
    void purchaseCartSuccess2(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            when(shopControllerMock.calculateShopDiscount(1, visitorMock.getId(),shoppingBasketMock)).thenReturn(1.0);

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertFalse(res.get(0).isErrorOccurred());
            Assertions.assertEquals(res.get(0).getCost(), 0.0);
            Assertions.assertFalse(purchaseCashier.getPurchasesShop(1).isEmpty());
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("PurchaseCart Success - Half discount")
    void purchaseCartSuccess3(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            when(shopControllerMock.calculateShopDiscount(1, visitorMock.getId(),shoppingBasketMock)).thenReturn(0.5);

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertFalse(res.get(0).isErrorOccurred());
            Assertions.assertEquals(res.get(0).getCost(), productMock.getPrice()/2);
            Assertions.assertFalse(purchaseCashier.getPurchasesShop(1).isEmpty());
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("PurchaseCart Fail - bad creditCard")
    void purchaseCartFail1(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789A","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.fail();
        }catch(Exception e){
            Assertions.assertTrue(e.getMessage().contains("invalid"));
        }
    }

    @Test
    @DisplayName("PurchaseCart Fail - Empty basket")
    void purchaseCartFail2(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange

            ConcurrentHashMap<Integer, ShoppingBasket> basketsMap = new ConcurrentHashMap<>();

            when(visitorMock.getBaskets()).thenReturn(basketsMap.entrySet());
            when(visitorControllerMock.getBaskets(1)).thenReturn(basketsMap.entrySet());

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.fail();

        }catch(Exception e){
            Assertions.assertTrue(e.getMessage().contains("empty"));
        }
    }

    @Test
    @DisplayName("PurchaseCart Fail - quantity in basket is higher then in shop")
    void purchaseCartFail3(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            when(productMock.getQuantity()).thenReturn(1);
            ConcurrentHashMap<Product, Pair<Integer, Double>> productsMap = new ConcurrentHashMap<>();
            productsMap.put(productMock, new Pair<>(5, 0.));
            when(shoppingBasketMock.getProducts()).thenReturn(productsMap.entrySet());

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertTrue(res.get(0).isErrorOccurred());
            Assertions.assertTrue(res.get(0).getPurchaseError().contains("bigger"));


        }catch(Exception e){
            Assertions.fail();
        }
    }
    @Test
    @DisplayName("PurchaseCart Fail - bad delivery")
    void purchaseCartFail4(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            when(proxyDeliveryMock.bookDelivery("Israel","Ashdod","Malahim")).thenReturn(-1);

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertTrue(res.get(0).isErrorOccurred());
            Assertions.assertTrue(res.get(0).getPurchaseError().contains("delivery"));


        }catch(Exception e){
            Assertions.fail();
        }
    }
    @Test
    @DisplayName("PurchaseCart Fail - bad payment")
    void purchaseCartFail5(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            ConcurrentHashMap<Product, Pair<Integer, Double>> productsMap = new ConcurrentHashMap<>();
            productsMap.put(productMock, new Pair<Integer, Double>(1, 0.));
            when(shoppingBasketMock.getProducts()).thenReturn(productsMap.entrySet());
            when(proxyPaymentMock.chargeCreditCard("123456789","11/26","123",productMock.getPrice()*productsMap.get(productMock).getKey())).thenReturn(-1);

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertTrue(res.get(0).isErrorOccurred());
            Assertions.assertTrue(res.get(0).getPurchaseError().contains("payment"));

        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("PurchaseCart Fail - shop is not open")
    void purchaseCartFail6(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            when(shopControllerMock.verifyOpenShop(1)).thenReturn(false);

            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertTrue(res.isEmpty());
            //TODO what happens if all the shops was close? do we throw an error?

        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("PurchaseCart Fail - bad PurchasePolicy")
    void purchaseCartFail7(){
        purchaseCashier.setPurchaseCashier(visitorControllerMock, shopControllerMock, proxyPaymentMock, proxyDeliveryMock);
        try{
            // Arrange
            when(shopControllerMock.validatePurchasePolicy(1, visitorMock.getId(), shoppingBasketMock)).thenReturn(false);
            // Act
            List<PurchaseHistory> res = purchaseCashier.purchaseCart(visitorMock.getId(),"123456789","11/26","123","Israel","Ashdod","Malahim");

            // Assert
            Assertions.assertFalse(res.isEmpty());
            Assertions.assertTrue(res.get(0).isErrorOccurred());
            Assertions.assertTrue(res.get(0).getPurchaseError().contains("policy"));

        }catch(Exception e){
            Assertions.fail();
        }
    }

}
