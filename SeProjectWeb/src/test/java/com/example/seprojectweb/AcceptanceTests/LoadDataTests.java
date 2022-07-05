package com.example.seprojectweb.AcceptanceTests;


import com.example.seprojectweb.Domain.Market.Responses.ProductResponse;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import com.example.seprojectweb.Domain.Market.Responses.ShopResponse;
import com.example.seprojectweb.Domain.Market.Responses.ShoppingCartResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoadDataTests extends MarketTests{

    private Response<ShopResponse> openNewShopResponse2;
    private ShopResponse shopResponse2;
    private Response<ProductResponse> newProductResponse33;
    private ProductResponse productResponse33;


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
        openNewShopResponse=  openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();
        newProductResponse1= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity,price,description,category1);
        productResponse1= newProductResponse1.getValue();
        newProductResponse2= addProduct(visitorLogged.getId(),shopResponse.getId(),productName2,quantity,price,description,category1);
        productResponse2= newProductResponse2.getValue();

        openNewShopResponse2 = openShop(visitorLogged.getId(), memberPhone, creditCard, GenerateNewShopName(), shopDescriptionValid, shopLocation);
        shopResponse2 = openNewShopResponse2.getValue();
        newProductResponse33= addProduct(visitorLogged.getId(),shopResponse2.getId(),productName1,quantity,price,description,category1);
        productResponse33= newProductResponse33.getValue();

    }

    @AfterEach
    void tearDown() {
        leaveSystem(visitor2.getId());
        visitor2 = null;
        leaveSystem(visitor3.getId());
        visitor3 = null;
        leaveSystem(visitor4.getId());
        visitor4 = null;
        //removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
        productResponse2=null;
        //closeShop(visitorLogged.getId(),shopResponse.getId());
        leaveSystem(visitorLogged.getId());
    }

    @Test
    void addProductsToShoppingCartAndAddProductPolicy()
    {

        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor2.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        shoppingCart = addProductToShoppingCart(visitor2.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        addAtLeastFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1);
    }
}
