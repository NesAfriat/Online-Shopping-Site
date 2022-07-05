package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MarketCostumerAsyncTests extends MarketTests{
    @BeforeEach
    void setUp() {
        visitResponse2 = visitSystem();
        visitor2 = visitResponse2.getValue();
        visitResponse3 = visitSystem();
        visitor3 = visitResponse3.getValue();
        visitResponse4 = visitSystem();
        visitor4 = visitResponse4.getValue();
        loginUserResponse = login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
        visitorLogged= loginUserResponse.getValue();
        loginUserResponse = login(visitor2.getId(), memberUsername2, members.get(memberUsername2));
        visitor2= loginUserResponse.getValue();
        loginUserResponse = login(visitor3.getId(), memberUsername3, members.get(memberUsername3));
        visitor3= loginUserResponse.getValue();
        openNewShopResponse =  openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();
        newProductResponse1= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity1,price,description,category1);

        productResponse1= newProductResponse1.getValue();
        newProductResponse2= addProduct(visitorLogged.getId(),shopResponse.getId(),productName2,quantity,price,description,category1);
        productResponse2= newProductResponse2.getValue();

    }

    //use case 10 #issue 48
    @Test
    @DisplayName("async -> try to buy last product")
    void purchaseShoppingCartSuccess() throws InterruptedException {
        Response<ShopResponse> sr = getShopInfo(1,0);
        shopResponse = sr.getValue();
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity1);
        addProductToShoppingCart(visitor2.getId(),shopResponse.getId(),productResponse1.getId(),quantity1);

        final Response<List<PurchaseHistoryResponse>>[] responseVis4 = new Response[]{null};
        int errorAcquired = 0;
        Thread visitor4Thread = new Thread(){
            public void run(){
               responseVis4[0] = purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
            }
        };

        final Response<List<PurchaseHistoryResponse>>[] responseVis1 = new Response[]{null};
        Thread visitor1Thread = new Thread(){
            public void run(){
                responseVis1[0] = purchaseShoppingCart(visitor2.getId(),creditCard,date,cvs,country,city,street,zip);
            }
        };
        visitor1Thread.start();
        visitor4Thread.start();
        visitor1Thread.join();
        visitor4Thread.join();



        List<PurchaseHistoryResponse> purchaseHistoryResponse= responseVis1[0].getValue();
        assertTrue(purchaseHistoryResponse.size()==1);

        if(purchaseHistoryResponse.get(0).isErrorAcquired()){
            errorAcquired++;
        }else {
            assertTrue(purchaseHistoryResponse.get(0).getShopId() == shopResponse.getId());
        }
        List<PurchaseHistoryResponse> purchaseHistoryResponse2= responseVis4[0].getValue();
        assertTrue(purchaseHistoryResponse2.size()==1);

        if(purchaseHistoryResponse2.get(0).isErrorAcquired()){
            errorAcquired++;
        }else {
            assertTrue(purchaseHistoryResponse2.get(0).getShopId() == shopResponse.getId());
        }

        assertEquals(errorAcquired,1);
    }
    @Test
    @DisplayName("async -> remove item and then buy")
    void removeWithBuyProduct() throws InterruptedException {
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity1);
        final boolean[] doneRemove = {false};
        Thread removeProductThread = new Thread(() -> {
            Response<ProductResponse> response= removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
            productResponse2 = response.getValue();
            assertNotNull(productResponse2);
            assertEquals(productResponse2.getProductName(),productName2);
            assertEquals(productResponse2.getShopId(),shopResponse.getId());
            doneRemove[0] = true;
        });
        final Response<List<PurchaseHistoryResponse>>[] responseVis4 = new Response[]{null};
        Thread buyProductThread = new Thread(){
            public void run(){
                while (!doneRemove[0]){}
                responseVis4[0] = purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
            }
        };
        removeProductThread.start();
        buyProductThread.start();
        removeProductThread.join();
        buyProductThread.join();

        List<PurchaseHistoryResponse> purchaseHistoryResponse2 = responseVis4[0].getValue();
        assertTrue(purchaseHistoryResponse2.size()==1); //Ive changed here
        assertTrue(purchaseHistoryResponse2.get(0).isErrorAcquired());
    }
}


