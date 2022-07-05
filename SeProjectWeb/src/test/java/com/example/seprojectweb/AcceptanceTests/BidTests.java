package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Responses.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BidTests extends ProjectTests{

    VisitorResponse visitorShopOwner1;
    VisitorResponse visitorShopOwner2;
    VisitorResponse visitorShopManager1;
    VisitorResponse visitorShopManager2;
    VisitorResponse visitorBidder;
    MemberResponse shopOwner1;
    MemberResponse shopOwner2;
    MemberResponse shopManager1;
    MemberResponse shopManager2;
    MemberResponse bidder;
    MemberObserverForTests bidderMailBox;
    ShopResponse shop;
    ProductResponse product;

    int QUANTITY = 10;

    @BeforeEach
    void setUp() {
        visitorShopOwner1 = visitSystem().getValue();
        visitorShopOwner2 = visitSystem().getValue();
        visitorShopManager1 = visitSystem().getValue();
        visitorShopManager2 = visitSystem().getValue();
        visitorBidder = visitSystem().getValue();

        register(visitorShopOwner1.getId(), "ShopOwner1", "pass", "email@email.email");
        register(visitorShopOwner1.getId(), "ShopOwner2", "pass", "email@email.email");
        register(visitorShopOwner1.getId(), "ShopManager1", "pass", "email@email.email");
        register(visitorShopOwner1.getId(), "ShopManager2", "pass", "email@email.email");
        register(visitorShopOwner1.getId(), "bidder", "pass", "email@email.email");


        shopOwner1 = login(visitorShopOwner1.getId(), "ShopOwner1", "pass").getValue().getLoggedIn();

        bidderMailBox = new MemberObserverForTests();
        bidder = login(visitorBidder.getId(), "bidder", "pass", bidderMailBox).getValue().getLoggedIn();



        shop = openShop(visitorShopOwner1.getId(), "0521234567", "1111222233334444", "shop", "shopshop", "over there").getValue();

        product = addProduct(visitorShopOwner1.getId(), shop.getId(), "product", QUANTITY, 100, "good product", "cat").getValue();

        assignShopOwner(visitorShopOwner1.getId(), "ShopOwner2", shop.getId());
    }

    @AfterEach
    void tearDown() {

        removeProduct(visitorShopOwner1.getId(), product.getShopId(), product.getId());

        closeShop(visitorShopOwner1.getId(), shop.getId());

        logout(visitorShopOwner1.getId());
        leaveSystem(visitorShopOwner1.getId());

        logout(visitorShopOwner2.getId());
        leaveSystem(visitorShopOwner2.getId());


        logout(visitorShopManager1.getId());
        leaveSystem(visitorShopManager1.getId());

        logout(visitorShopManager2.getId());
        leaveSystem(visitorShopManager2.getId());

        logout(visitorBidder.getId());
        leaveSystem(visitorBidder.getId());


    }

    @Test
    void bid_success_test()
    {
        Response bidResponse = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(bidResponse.isErrorOccurred());
    }

    @Test
    void bid_success_test_with_Manager()
    {
        assignShopManager(visitorShopOwner1.getId(), "ShopManager1", shop.getId());

        Response bidResponse = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(bidResponse.isErrorOccurred());

    }

    @Test
    void bid_fail_price_to_high_test()
    {
        Response bidResponse = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()+1);
        assertTrue(bidResponse.isErrorOccurred());
        assertFalse(bidResponse.getErrorMessage().contains("fatal"));
    }




    @Test
    void approve_bid_success_test()
    {
        Response<BidResponse> bidResponse = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(bidResponse.isErrorOccurred());
        bidResponse =approveBid(visitorShopOwner1.getId(), shop.getId(), bidResponse.getValue().getId());
        assertTrue(bidResponse.getValue().getApprovers().contains(shopOwner1.getUsername()));
    }


    @Test
    void purchase_approve_bid_success_test()
    {
        Response<BidResponse> response = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(response.isErrorOccurred());
        BidResponse bidResponse = response.getValue();


        response = approveBid(visitorShopOwner1.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner1.getUsername()));


        visitorShopOwner2 = visitSystem().getValue();
        shopOwner2 = login(visitorShopOwner2.getId(), "ShopOwner2", "pass").getValue().getLoggedIn();
        response = approveBid(visitorShopOwner2.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner2.getUsername()));


        Response<PurchaseHistoryResponse> response2 = purchaseBid(visitorBidder.getId(),bidResponse.getId(), "1234123412341234", "11-2023", "123", "israel", "beer sheva" ,"rubic","123");
        assertFalse(response2.isErrorOccurred());
    }

    @Test
    void purchase_unapprove_bid_fail_test()
    {
        Response<BidResponse> response = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(response.isErrorOccurred());
        BidResponse bidResponse = response.getValue();
        response = approveBid(visitorShopOwner1.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner1.getUsername()));
        Response<PurchaseHistoryResponse> response2 = purchaseBid(visitorBidder.getId(),bidResponse.getId(), "1234123412341234", "11/11/2023", "123", "israel", "beer sheva" ,"rubic","123");
        assertTrue(response2.isErrorOccurred());
        assertFalse(response2.getErrorMessage().contains("fatal"));
    }

    @Test
    void approve_bid_get_notification_test()
    {
        Response<BidResponse> response = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(response.isErrorOccurred());
        BidResponse bidResponse = response.getValue();
        response = approveBid(visitorShopOwner1.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner1.getUsername()));
        visitorShopOwner2 = login(visitorShopOwner2.getId(), "ShopOwner2", "pass").getValue();
        shopOwner2 = visitorShopOwner2.getLoggedIn();
        response = approveBid(visitorShopOwner2.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner2.getUsername()));
        assertTrue(bidderMailBox.getNotifications().size() == 1);
    }

    @Test
    void unapprove_bid_new_owner_test()
    {
        Response<BidResponse> response = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(response.isErrorOccurred());
        BidResponse bidResponse = response.getValue();
        response = approveBid(visitorShopOwner1.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner1.getUsername()));

        assignShopOwner(visitorShopOwner1.getId(), "ShopManager1", shop.getId());


        visitorShopOwner2 = login(visitorShopOwner2.getId(), "ShopOwner2", "pass").getValue();
        shopOwner2 = visitorShopOwner2.getLoggedIn();
        response = approveBid(visitorShopOwner2.getId(), shop.getId(), bidResponse.getId());
        assertFalse(response.isErrorOccurred());
        bidResponse = response.getValue();
        assertTrue(bidResponse.getApprovers().contains(shopOwner2.getUsername()));
        assertTrue(bidderMailBox.getNotifications().size() == 0);
        Response<PurchaseHistoryResponse> response2 = purchaseBid(visitorBidder.getId(),bidResponse.getId(), "1234123412341234", "11/11/2023", "123", "israel", "beer sheva" ,"rubic","123");
        assertTrue(response2.isErrorOccurred());
        assertFalse(response2.getErrorMessage().contains("fatal"));


        removeShopOwner(visitorShopOwner1.getId(), "ShopManager1", shop.getId());


        Response<PurchaseHistoryResponse> response3 = purchaseBid(visitorBidder.getId(),bidResponse.getId(), "1234123412341234", "11/11/2023", "123", "israel", "beer sheva" ,"rubic","123");
        assertFalse(response3.isErrorOccurred());
    }




    @Test
    void get_bid_success_test()
    {
        Response bidResponse = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 1, product.getPrice()-1);
        assertFalse(bidResponse.isErrorOccurred());
        bidResponse = bidProduct(visitorBidder.getId(), shop.getId(), product.getId(), 2, product.getPrice() - 2);
        assertFalse(bidResponse.isErrorOccurred());
        Response<List<BidResponse>> listBidResponse = getAllMemberBid(visitorBidder.getId());
        assertTrue(listBidResponse.getValue().size()==2);
        listBidResponse = getAllShopBid(visitorShopOwner1.getId(), shop.getId());
        assertTrue(listBidResponse.getValue().size()==2);

    }





}
