package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.Market.Responses.PurchaseHistoryResponse;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DiscountTests extends MarketTests{
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



}
