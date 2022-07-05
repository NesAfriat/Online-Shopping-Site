package com.example.seprojectweb.ScenarioTests;

import com.example.seprojectweb.Domain.Market.MarketRepresentative;
import com.example.seprojectweb.Domain.Market.Responses.MemberResponse;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import com.example.seprojectweb.Domain.Market.Responses.ShopResponse;
import com.example.seprojectweb.Domain.Market.Responses.VisitorResponse;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminTests {

    private MarketRepresentative m = MarketRepresentative.getInstance();
    int visitorAdminID = 2;
    private static final String DEFAULT_ADMIN_USERNAME = "HannaLaslo";
    private static final String DEFAULT_ADMIN_PASSWORD = "TheQueen";
    String userName1 = "riki1";
    String pass1 = "riki11111";
    String userName2 = "riki2";
    String pass2 = "riki11111";
    VisitorResponse visitorAdmin = m.visitSystem().getValue();
    VisitorResponse visitAdmin  = m.login(visitorAdmin.getId(),DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD,null).getValue();
    VisitorResponse visitor1 = m.visitSystem().getValue();
    VisitorResponse visitor2 = m.visitSystem().getValue();
    MemberResponse member1 = m.register(visitor1.getId(),userName1,pass1).getValue();
    VisitorResponse visit1loggin  = m.login(visitor1.getId(),userName1, pass2,null).getValue();
    Response<ShopResponse> s = m.openShop(visitor1.getId(),"0524436773","111111111","aaaaa","323323","fffff");
    MemberResponse member2 = m.register(visitor2.getId(),userName2,pass2).getValue();

    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }

    @Test
    @DisplayName("cancel fail => not admin")
    void cancelMembership1() {
        Response<MemberResponse> res = m.cancelMembership(visitor1.getId(),userName2);
        assertTrue(res.isErrorOccurred());
    }
    @Test
    @DisplayName("cancel fail => have role in shop")
    void cancelMembership2() {
        Response<MemberResponse> res = m.cancelMembership(visitorAdmin.getId(),userName1);
        assertTrue(res.isErrorOccurred());
    }
    @Test
    @DisplayName("cancel membership => success")
    void cancelMembership3() {
        Response<MemberResponse> res = m.cancelMembership(visitAdmin.getId(),userName2);
        assertFalse(res.isErrorOccurred());
    }
    //scenraio - multiple owners for the same shop - then we remove one owner and expect the rest to be removed
    @Test
    @DisplayName("get all members => success")
    void getAllMembers1() {
        Response<List<MemberResponse>> res = m.getAllMembers(visitAdmin.getId());

        assertFalse(res.isErrorOccurred());
        assertEquals(3, res.getValue().size());


    }
    @Test
    @DisplayName("get members fail => not admin")
    void getAllMembers2() {
        Response<List<MemberResponse>> res = m.getAllMembers(visitor1.getId());
        assertTrue(res.isErrorOccurred());
    }


}