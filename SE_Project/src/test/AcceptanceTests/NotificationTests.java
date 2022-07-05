package AcceptanceTests;

import AcceptanceTests.MarketTests;
import Domain.Market.Notifications.Notification;
import Domain.Market.Responses.*;
import Domain.Market.ShopController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.util.List;

public class NotificationTests extends MarketTests {
    @BeforeEach
    void setUp() {
        visitLoggedResponse = visitSystem();
        visitorLogged = visitLoggedResponse.getValue();
        visitResponse2 = visitSystem();
        visitor2 = visitResponse2.getValue();
        visitResponse3 = visitSystem();
        visitor3 = visitResponse3.getValue();
        visitorLoggedMemberObserver = new MemberObserverForTests();
        visitor2MemberObserver = new MemberObserverForTests();
        visitor3MemberObserver = new MemberObserverForTests();
        loginUserResponse = login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1), visitorLoggedMemberObserver);
        visitorLogged= loginUserResponse.getValue();
        loginUserResponse = login(visitor2.getId(), memberUsername2, members.get(memberUsername2), visitor2MemberObserver);
        visitor2= loginUserResponse.getValue();
        loginUserResponse = login(visitor3.getId(), memberUsername3, members.get(memberUsername3), visitor3MemberObserver);
        visitor3= loginUserResponse.getValue();
        openNewShopResponse=  openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();

        newProductResponse1= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity,price,description,category1);
        productResponse1= newProductResponse1.getValue();
        newProductResponse2= addProduct(visitorLogged.getId(),shopResponse.getId(),productName2,quantity,price,description,category1);
        productResponse2= newProductResponse2.getValue();

        assignShopOwner(visitorLogged.getId(), visitor2.getLoggedIn().getUsername(),shopResponse.getId());


    }

    @AfterEach
    void tearDown() {
        leaveSystem(visitor2.getId());
        visitor2 = null;
        leaveSystem(visitor3.getId());
        visitor3 = null;
        closeShop(visitorLogged.getId(),shopResponse.getId());
        logout(visitorLogged.getId());
    }

    @Test
    void closeShopRealTimeNotification(){
        int sizeBefore = visitor2MemberObserver.getNotifications().size();
        closeShop(visitorLogged.getId(), shopResponse.getId());
        assertTrue(visitor2MemberObserver.getNotifications().size() == sizeBefore + 1);
        boolean isCloseMessageGot = false;
        for (Notification notification :visitor2MemberObserver.getNotifications()) {
            isCloseMessageGot =  isCloseMessageGot ||notification.getMessage().contains("is now close");
        }
        assertTrue(isCloseMessageGot);
    }


    @Test
    void closeShopRealTimeNotification_not_owner_no_get(){
        int sizeBefore = visitor3MemberObserver.getNotifications().size();
        closeShop(visitorLogged.getId(), shopResponse.getId());
        assertTrue(visitor3MemberObserver.getNotifications().size() == sizeBefore);
        boolean isCloseMessageGot = false;
        for (Notification notification :visitor3MemberObserver.getNotifications()) {
            isCloseMessageGot =  isCloseMessageGot ||notification.getMessage().contains("is now close");
        }
        assertFalse(isCloseMessageGot);
    }

    @Test
    void closeShopNotRealTimeNotification(){
        int sizeBefore = visitor2MemberObserver.getNotifications().size();
        logout(visitor2.getId());
        closeShop(visitorLogged.getId(), shopResponse.getId());
        assertTrue(visitor2MemberObserver.getNotifications().size() == sizeBefore);
        loginUserResponse = login(visitor2.getId(), memberUsername2, members.get(memberUsername2), visitor2MemberObserver);
        assertTrue(visitor2MemberObserver.getNotifications().size() == sizeBefore + 1);
        boolean isCloseMessageGot = false;
        for (Notification notification :visitor2MemberObserver.getNotifications()) {
            isCloseMessageGot =  isCloseMessageGot ||notification.getMessage().contains("is now close");
        }
        assertTrue(isCloseMessageGot);
    }


    @Test
    void closeShopNotRealTimeNotification_not_owner_no_get(){
        int sizeBefore = visitor2MemberObserver.getNotifications().size();
        logout(visitor3.getId());
        closeShop(visitorLogged.getId(), shopResponse.getId());
        assertTrue(visitor3MemberObserver.getNotifications().size() == sizeBefore);
        loginUserResponse = login(visitor3.getId(), memberUsername3, members.get(memberUsername3), visitor3MemberObserver);
        assertTrue(visitor3MemberObserver.getNotifications().size() == sizeBefore);
        boolean isCloseMessageGot = false;
        for (Notification notification :visitor3MemberObserver.getNotifications()) {
            isCloseMessageGot =  isCloseMessageGot ||notification.getMessage().contains("is now close");
        }
        assertFalse(isCloseMessageGot);
    }

    @Test
    void purchaseShoppingCartSuccessWithNotifications(){
        ShopController shopController = ShopController.getInstance();
        int sizeBefore = visitor2MemberObserver.getNotifications().size();
        addProductToShoppingCart(visitor3.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        addProductToShoppingCart(visitor3.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor3.getId(),creditCard,date,cvs,country,city,street);
        assertTrue(visitor2MemberObserver.getNotifications().size() == sizeBefore + 1);
    }




}







