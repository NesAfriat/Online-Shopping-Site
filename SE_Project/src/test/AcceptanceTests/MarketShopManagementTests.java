package AcceptanceTests;

import Domain.Market.Responses.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.util.List;

public class MarketShopManagementTests extends MarketTests {
    @BeforeEach
    void setUp() {
        visitLoggedResponse = visitSystem();
        visitorLogged = visitLoggedResponse.getValue();
        visitResponse2 = visitSystem();
        visitor2 = visitResponse2.getValue();
        visitResponse3 = visitSystem();
        visitor3 = visitResponse3.getValue();
        visitResponse4 = visitSystem();
        visitor4 = visitResponse4.getValue();
        visitorLoggedMemberObserver = new MemberObserverForTests();
        visitor2MemberObserver = new MemberObserverForTests();
        visitor3MemberObserver = new MemberObserverForTests();
        visitor4MemberObserver = new MemberObserverForTests();
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
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street);
    }

    @AfterEach
    void tearDown() {
        leaveSystem(visitor2.getId());
        visitor2 = null;
        leaveSystem(visitor3.getId());
        visitor3 = null;
        leaveSystem(visitor4.getId());
        visitor4 = null;
        closeShop(visitorLogged.getId(),shopResponse.getId());
        logout(visitorLogged.getId());
    }

    //open shop test UC 16 issue #50
    @Test
    void OpenNewShopSuccess() {
        assertFalse(openNewShopResponse.isErrorOccurred());
        assertNotNull(shopResponse);
        assertEquals(visitorLogged.getId(), shopResponse.getFounderID());
    }

    @Test
    void OpenNewShopEmptyDescription() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, creditCard, GenerateNewShopName(), "", shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopNullDescription() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, creditCard, GenerateNewShopName(), null, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopExistName() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, creditCard, shopNameExist, shopDescriptionValid, shopLocation);
        assertFalse(response.isErrorOccurred());
        response = openShop(visitorLogged.getId(), memberPhone, creditCard, shopNameExist, shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopEmptyName() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, creditCard, "", shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);

    }

    @Test
    void OpenNewShopNullName() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, creditCard, null, shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopNotExistVisitor() {
        Response<ShopResponse> response = openShop(notUserId, memberPhone, creditCard, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);

    }
    @Test
    void OpenNewShopNotLoggedVisitor() {
        visitResponse3=logout(visitor3.getId());
        visitor3= visitResponse3.getValue();
        Response<ShopResponse> response = openShop(visitor3.getId(), memberPhone, creditCard, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopInvalidPhoneNumber() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), invalidPhoneNumber, creditCard, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopShortPhoneNumber() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), shortPhoneNumber, creditCard, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopNullPhoneNumber() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), null, creditCard, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopInvalidCreditNumber() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, invalidCreditNumber, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    @Test
    void OpenNewShopNullCreditNumber() {
        Response<ShopResponse> response = openShop(visitorLogged.getId(), memberPhone, null, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse newShop = response.getValue();
        assertNull(newShop);
    }

    //Assign manager UC 18 no current issue
    @Test
    void AssignManagerSuccess() {
        Response<RoleResponse> response = assignShopManager(visitorLogged.getId(), memberUsername2, shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        RoleResponse roleResponse = response.getValue();
        assertNotNull(roleResponse);
        assertEquals(roleResponse.getAssignorUsername(), memberUsername1);
    }

    @Test
    void AssignManagerNotVisitor() {
        Response<RoleResponse> response = assignShopManager(notUserId, memberUsername3, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignManagerNotLogged() {
        visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        Response<RoleResponse> response = assignShopManager(visitorLogged.getId(), memberUsername3, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
        login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
    }

    @Test
    void AssignManagerNotOwner() {
        Response<RoleResponse> response = assignShopManager(visitor3.getId(), memberUsername4, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignManagerNotExistMember() {
        Response<RoleResponse> response = assignShopManager(visitorLogged.getId(), notAMemberUserName, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignManagerAlreadyGotRole() {
        Response<RoleResponse> response = assignShopManager(visitorLogged.getId(), memberUsername1, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignManagerInValidShop() {
        Response<RoleResponse> response = assignShopManager(visitorLogged.getId(), memberUsername3, notShopID);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    //Assign shop owner UC 18 issue #54
    @Test
    void AssignOwnerSuccess() {
        Response<RoleResponse> response = assignShopOwner(visitorLogged.getId(), memberUsername3, shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        RoleResponse roleResponse = response.getValue();
        assertNotNull(roleResponse);
        assertEquals(roleResponse.getAssignorUsername(), memberUsername1);
    }

    @Test
    void AssignOwnerNotVisitor() {
        Response<RoleResponse> response = assignShopOwner(notUserId, memberUsername4, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignOwnerNotLogged() {
        visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        Response<RoleResponse> response = assignShopOwner(visitorLogged.getId(), memberUsername4, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
        login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
    }

    @Test
    void AssignOwnerNotOwner() {
        Response<RoleResponse> response = assignShopOwner(visitor2.getId(), memberUsername4, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignOwnerNotExistMember() {
        Response<RoleResponse> response = assignShopOwner(visitorLogged.getId(), notAMemberUserName, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignOwnerAlreadyGotRole() {
        Response<RoleResponse> response = assignShopOwner(visitorLogged.getId(), memberUsername1, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    @Test
    void AssignOwnerInValidShop() {
        Response<RoleResponse> response = assignShopOwner(visitorLogged.getId(), memberUsername4, notShopID);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        RoleResponse roleResponse = response.getValue();
        assertNull(roleResponse);
    }

    //UC 21 issue #issue 71
    @Test
    void getShopRoleInfoSuccess() {
        Response<List<RoleResponse>> response = getShopRoleInfo(visitorLogged.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        assertFalse(response.getValue().isEmpty());
        for (RoleResponse res : response.getValue()) {
            assertNotNull(res);
        }
    }

    @Test
    void getShopRoleInfoContainsOtherRoles() {
        assignShopManager(visitorLogged.getId(), memberUsername2, shopResponse.getId());
        assignShopOwner(visitorLogged.getId(), memberUsername3, shopResponse.getId());
        Response<List<RoleResponse>> response = getShopRoleInfo(visitorLogged.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        assertFalse(response.getValue().size() != 3);
    }

    @Test
    void getShopRoleInfoWithoutRoleInShop() {
        login(visitor4.getId(), memberUsername4, members.get(memberUsername4));
        Response<List<RoleResponse>> response =getShopRoleInfo(visitor4.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        logout(visitor4.getId());
    }
    @Test
    void getShopRoleInfoWithoutLogging() {
        visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        Response<List<RoleResponse>> response =getShopRoleInfo(visitorLogged.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
    }
    @Test
    void getShopRoleInfoInvalidUser() {
        Response<List<RoleResponse>> response = getShopRoleInfo(notUserId, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
    }
    @Test
    void getShopRoleInfoInvalidShop() {
        Response<List<RoleResponse>> response = getShopRoleInfo(visitorLogged.getId(),notShopID);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
    }



    //closeShopTwice
    //use case 20 issue 67
    @Test
    void closeShopSuccess() {
        Response<ShopResponse> response = closeShop(visitorLogged.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        Response<ShopResponse> visitorSearch= getShopInfo(visitor2.getId(),shopResponse.getId());
        assertTrue(visitorSearch.isErrorOccurred());
        assertFalse(visitorSearch.getErrorMessage().contains("fatal"));
        Response<ShopResponse> founderSearch= getShopInfo(visitorLogged.getId(),shopResponse.getId());
        assertFalse(founderSearch.isErrorOccurred());
        assertNotNull(founderSearch.getValue());
    }
    @Test
    void closeShopNotExistShop() {
        Response<ShopResponse> response = closeShop(visitorLogged.getId(), notShopID);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void closeShopNotFounder() {
        Response<ShopResponse> response = closeShop(visitor2.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void closeShopNotVisitor() {
        Response<ShopResponse> response = closeShop(notUserId, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void closeShopNotLogged() {
        visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        Response<ShopResponse> response = closeShop(visitorLogged.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
        login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
    }
    @Test
    void closeShopTwice() {
        Response<ShopResponse> response = closeShop(visitorLogged.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        response = closeShop(visitorLogged.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    //use case 2 no current issue:
    @Test
    void getShopPurchaseHistorySuccess() {
        Response<List<PurchaseHistoryResponse>> response =  getShopPurchaseHistory(visitorLogged.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        List<PurchaseHistoryResponse> list= response.getValue();
        assertNotNull(list);
        assertTrue(list.size()==1);
    }

    @Test
    void getShopPurchaseHistoryWithoutVisiting() {
        Response<List<PurchaseHistoryResponse>> response =  getShopPurchaseHistory(notUserId, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        List<PurchaseHistoryResponse> infoList = response.getValue();
        assertNull(infoList);
    }
    @Test
    void getShopInfoWithoutLogging() {
        visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        Response<List<PurchaseHistoryResponse>> response =  getShopPurchaseHistory(visitorLogged.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        List<PurchaseHistoryResponse> infoList = response.getValue();
        assertNull(infoList);
        login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
    }
    //UC 22 role permission
    @Test
    void getShopInfoWithoutRoleInShop() {
        Response<List<PurchaseHistoryResponse>> response =  getShopPurchaseHistory(visitor4.getId(), shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        List<PurchaseHistoryResponse> infoList = response.getValue();
        assertNull(infoList);
    }
    @Test
    void getShopInfoInvalidShop() {
        Response<List<PurchaseHistoryResponse>> response =  getShopPurchaseHistory(visitorLogged.getId(), notShopID);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        List<PurchaseHistoryResponse> infoList = response.getValue();
        assertNull(infoList);
    }

    //UC 2 Admin permission
    @Test
    void getShopInfoWithRoleInSystemSuccess() {
        visitResponseAdmin =login(visitor4.getId(),adminUser,adminPass);
        visitor4= visitResponseAdmin.getValue();
        Response<List<PurchaseHistoryResponse>> response = getShopPurchaseHistory(visitor4.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        List<PurchaseHistoryResponse> list = response.getValue();
        assertNotNull(list);
        assertTrue(list.size() == 1);
        logout(visitor4.getId());
    }}






