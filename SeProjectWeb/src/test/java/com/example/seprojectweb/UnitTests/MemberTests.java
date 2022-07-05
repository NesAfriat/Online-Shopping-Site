package com.example.seprojectweb.UnitTests;


import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.PersistenceManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class MemberTests {

    private final String user2UserName = "User2";
    private final String user2Password = "Password2";

    private final String userLoggedUserName = "UserLogged";
    private final String userLoggedPassword = "Password1";
    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }

    @Test
    public void loginExistingMember() {
        VisitorController visitorController = VisitorController.getInstance();
        Visitor visitor = visitorController.visitSystem();
        MemberObserverForTests memberObserverForTests = new MemberObserverForTests();
        try {
            Visitor member = visitorController.login(visitor.getId(), userLoggedUserName, userLoggedPassword, memberObserverForTests);
            Assertions.assertTrue(member.isLoggedIn() && member.getLoggedIn().isLoggedIn());
            Assertions.assertEquals(member.getLoggedIn().getUsername(), userLoggedUserName);
        }
        catch (Exception e){
            fail();
        }

    }

    @Test
    public void loadCartAndPurchaseSuccess() {
        VisitorController visitorController = VisitorController.getInstance();
        Visitor visitor = visitorController.visitSystem();
        MemberObserverForTests memberObserverForTests = new MemberObserverForTests();
        PurchaseCashier purchaseCashier = PurchaseCashier.getInstance();
        String date= "05/04/2022";
        String cvs= "123";
        String country= "Israel";
        String city= "Ramat";
        String street= "Pozis";
        String zip = "123456789";
        String creditCard= "123456789101112131415";
        try {
            Visitor member = visitorController.login(visitor.getId(), user2UserName, user2Password, memberObserverForTests);
            ShoppingCart shoppingCart = member.getLoggedIn().getShoppingCart();
            List<PurchaseHistory> purchaseHistoryList = purchaseCashier.purchaseCart(visitor.getId(), creditCard,date,cvs,country,city,street,zip);
            Assertions.assertTrue(member.isLoggedIn() && member.getLoggedIn().isLoggedIn());
            Assertions.assertEquals(member.getLoggedIn().getUsername(), user2UserName);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }

    }
}
