package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import com.example.seprojectweb.Domain.Market.Responses.ShopOwnerResponse;
import com.example.seprojectweb.Domain.Market.ShopOwner;
import com.example.seprojectweb.MockDataBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RollTests extends MarketTests{

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
        openNewShopResponse =  openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();
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
        leaveSystem(visitorLogged.getId());
    }

    @Test
    void removeShopOwnerTest(){
        assertFalse(assignShopOwner(visitorLogged.getId(),memberUsername2, shopResponse.getId()).isErrorOccurred());
        assertFalse(assignShopOwner(visitor2.getId(),memberUsername3, shopResponse.getId()).isErrorOccurred());
        assertFalse(assignShopOwner(visitor3.getId(),memberUsername4, shopResponse.getId()).isErrorOccurred());
        assertFalse(removeShopOwner(visitor2.getId(),memberUsername3, shopResponse.getId()).isErrorOccurred());
    }
    @Test
    void getAllOwners(){
        List<ShopOwnerResponse> expected = List.of((ShopOwnerResponse) assignShopOwner(visitorLogged.getId(),memberUsername2, shopResponse.getId()).getValue(),
                (ShopOwnerResponse)assignShopOwner(visitor2.getId(),memberUsername3, shopResponse.getId()).getValue(),
                (ShopOwnerResponse) assignShopOwner(visitor3.getId(),memberUsername4, shopResponse.getId()).getValue()
                );
        Response<List<ShopOwnerResponse>> res = getAllOwners(visitorLogged.getId());
        assertFalse(res.isErrorOccurred());
        assertEquals(res.getValue().size(), expected.size() + 1);
        assertAll( ()-> res.getValue().containsAll(expected));
    }
}
