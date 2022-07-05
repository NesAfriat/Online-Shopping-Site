package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Responses.*;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.LineSeparatorDetector;

import java.util.List;

import static org.junit.Assert.*;

//TODO: get Responses out of domain


public class VisitorTests extends ProjectTests{
    String illegalPassword= "a!245GB#%";

    String GenerageNewEmail()
    {
        String emailName= "email".concat(signedEmailCount.toString());
        String newEmail= emailName.concat("@gmail.com");
        signedEmailCount++;
        return newEmail;
    }

    @BeforeEach
    void setUp() {
        visitResponse1=visitSystem();
        visitor1= visitResponse1.getValue();
        visitLoggedResponse= visitSystem();
        visitorLogged= visitLoggedResponse.getValue();
        userNameChanging= userNameChanging.concat(signedUserCount.toString());
        signedUserCount++;
        visitorLoggedMemberObserver = new MemberObserverForTests();
        loginUserResponse= login(visitorLogged.getId(),"UserLogged",members.get("UserLogged"), visitorLoggedMemberObserver);
        visitorLogged= loginUserResponse.getValue();
        openNewShopResponse= openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();
        newProductResponse1= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity,price,description,category1);
        productResponse1= newProductResponse1.getValue();
    }

    @AfterEach
    void tearDown() {
        leaveSystem(visitResponse1.getValue().getId());
        visitor1=null;
        closeShop(visitorLogged.getId(),shopResponse.getId());
        logout(visitorLogged.getId());
        leaveSystem(visitorLogged.getId());
        visitorLogged=null;
    }

    //visitSystem - UC3
    @Test
    void visitSystemSuccess() {
        assertFalse(visitResponse1.isErrorOccurred());
        assertNotNull(visitResponse1.getValue());
        assertNull(visitor1.getLoggedIn());
        assertNotNull(visitor1.getShoppingCart());
    }
    @Test
    @DisplayName("get all visitors test")
    void getAllVisitors(){
        Response<List<VisitorResponse>> res = getAllVisitors(1);
        assertFalse(res.isErrorOccurred());
        assertEquals(res.getValue().size(),5);
    }
    @Test
    void visitSystemUniqID() {
        Response<VisitorResponse> visitorResponse2= visitSystem();
        VisitorResponse visitor2=  visitorResponse2.getValue();
        assertFalse(visitor1.getId()==visitor2.getId());
    }

    //leave system - UC4 and UC15 issue 23
    @Test
    void leaveSystemSuccess() {
        int visitorID= visitor1.getId();
        Response<VisitorResponse> visitorResponseLeave= leaveSystem(visitorID);
        assertFalse(visitorResponseLeave.isErrorOccurred());
        VisitorResponse vs2=visitorResponseLeave.getValue();
        assertEquals(visitorID,vs2.getId());
    }
    @Test
    void leaveSystemFail() {
        Response<VisitorResponse> visitorResponseLeave= leaveSystem(notUserId);
        assertTrue(visitorResponseLeave.isErrorOccurred());
        assertFalse(visitorResponseLeave.getErrorMessage().contains("fatal"));
    }
    @Test
    void leaveSystemTwice() {
        Response<VisitorResponse> visitorResponseLeave1= leaveSystem(visitor1.getId());
        assertFalse(visitorResponseLeave1.isErrorOccurred());
        Response<VisitorResponse> visitorResponseLeave2= leaveSystem(visitor1.getId());
        assertTrue(visitorResponseLeave2.isErrorOccurred() && !visitorResponseLeave2.getErrorMessage().contains("fatal"));
    }
    //////for guests #UC4 - visitor
    @Test
    void leaveSystemGuestDeleteCart() {
        assertNotNull(visitor1.getShoppingCart());
        addProductToShoppingCart(visitor1.getId(),shopResponse.getId(),productResponse1.getId(),1);
        Response<VisitorResponse> visitorResponseLeave= leaveSystem(visitor1.getId());
        VisitorResponse leaveResponse=visitorResponseLeave.getValue();
        visitor1= visitSystem().getValue();
        assertNotNull(visitor1.getShoppingCart());
        ShoppingCartResponse shoppingCartResponse= visitor1.getShoppingCart();
        assertFalse(shoppingCartResponse.baskets.containsKey(productResponse1.getId()));
    }
    //////for guests #UC15 - Guest
    @Test
    void leaveSystemMemberDeleteCart() {
        assertNotNull(visitorLogged.getShoppingCart());
        addProductToShoppingCart(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),1);
        Response<VisitorResponse> visitorResponseLeave= leaveSystem(visitorLogged.getId());
        VisitorResponse leaveResponse=visitorResponseLeave.getValue();
        visitorLogged= visitSystem().getValue();
        assertNotNull(visitorLogged.getShoppingCart());
        ShoppingCartResponse shoppingCartResponse= visitorLogged.getShoppingCart();
        assertFalse(shoppingCartResponse.baskets.containsKey(productResponse1.getId()));
    }


    //register #UC5 issue #13
    @Test
    void registerSuccess()
    {
        Response<MemberResponse> response= register(visitor1.getId(),userNameChanging,password,GenerageNewEmail());
        assertFalse(response.isErrorOccurred());
        assertNotNull(response.getValue());
    }
    @Test
    void registerTwice()
    {
        Response<MemberResponse> response= register(visitor1.getId(),"User2",members.get("User2"),"User2@gmail.com");
        assertTrue(response.isErrorOccurred());
        assertNull(response.getValue());
        assertFalse(response.getErrorMessage().contains("fatal"));

    }
    @Test
    void registerWithNullUserName()
    {
        Response<MemberResponse> response= register(visitor1.getId(),null,password,GenerageNewEmail());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }
    @Test
    void registerWithEmptyUserName()
    {
        Response<MemberResponse> response= register(visitor1.getId(),"",password,GenerageNewEmail());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }
    @Test
    void registerWithExistingUserName()
    {
        Response<MemberResponse> response= register(visitor1.getId(),"User2",password,GenerageNewEmail());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }
    @Test
    void registerWithShortPassword()
    {
        Response<MemberResponse> response= register(visitor1.getId(),userNameChanging,"pass1",GenerageNewEmail());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }
    @Test
    void registerWithIlegalCharPassword()
    {
        Response<MemberResponse> response= register(visitor1.getId(),userNameChanging,illegalPassword,GenerageNewEmail());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }
    @Test
    void registerWithIlegalCharEmail()
    {
        Response<MemberResponse> response= register(visitor1.getId(),userNameChanging,password,"yossi.com");
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }

    @Test
    void registerNotValidUserID()
    {
        Response<MemberResponse> response= register(notUserId,userNameChanging,password,GenerageNewEmail());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        MemberResponse memberResponse = response.getValue();
        assertNull(memberResponse);
    }
    //login tests #UC 13 #issue 20
    @Test
    void loginSuccess()
    {
        assertFalse(loginUserResponse.isErrorOccurred());
        VisitorResponse loggedResponse= loginUserResponse.getValue();
        assertNotNull(loggedResponse);
        assertNotNull(loggedResponse.getLoggedIn());
    }
    @Test
    void loginTwiceSameVisitorID()
    {
        Response<VisitorResponse> response= login(visitorLogged.getId(),"User2",members.get("User2"));
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
    }
    @Test
    void loginWithoutEnteringSystem()
    {
        Response<VisitorResponse> response= login(notUserId,"User2",members.get("User2"));
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));

    }
    @Test
    void loginNotExistingUser()
    {
        Response<VisitorResponse> response= login(visitorLogged.getId(),userNameChanging,password);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse visitorResponse = response.getValue();
        assertNull(visitorResponse);
    }

    @Test
    void loginInvalidPassword()
    {
        Response<VisitorResponse> response= login(visitor1.getId(),"User2",members.get("User3"));
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse visitorResponse = response.getValue();
        assertNull(visitorResponse);

    }
    @Test
    void loginIlEmptyUserName()
    {
        Response<VisitorResponse> response= login(visitor1.getId(),"",password);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse visitorResponse = response.getValue();
        assertNull(visitorResponse);
    }
    void loginNullUserName()
    {
        Response<VisitorResponse> response= login(visitor1.getId(),null,password);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse visitorResponse = response.getValue();
        assertNull(visitorResponse);
    }
    @Test
    void  loginIllegalPassword()
    {
        Response<VisitorResponse> response= login(visitor1.getId(),"User2",illegalPassword);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse visitorResponse = response.getValue();
        assertNull(visitorResponse);
    }

    //logout tests #UC 14 #issue 11
    @Test
    void logoutSuccess()
    {
        Response<VisitorResponse> response= logout(visitorLogged.getId());
        assertFalse(response.isErrorOccurred());
        VisitorResponse logutResponse= response.getValue();
        assertNotNull(logutResponse);
        assertNull(logutResponse.getLoggedIn());
    }
    @Test
    void logoutWithoutEnteringSystem()
    {   Response<VisitorResponse> response= logout(notUserId);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse logutResponse= response.getValue();
        assertNull(logutResponse);

    }
    @Test
    void logoutWithoutLogin()
    {    Response<VisitorResponse> response= logout(visitor1.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        VisitorResponse logutResponse= response.getValue();
        assertNull(logutResponse);
    }
}
