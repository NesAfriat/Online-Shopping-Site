package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Responses.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class SystemTests extends ProjectTests{
    Response<VisitorResponse> visitResponse2;
    Response<VisitorResponse> visitResponse3;
    VisitorResponse visitor2;
    VisitorResponse visitor3;
    String memberUsername1= "UserLogged";
    String memberUsername2= "User2";
    String date= "05/04/2022";
    String cvs= "123";
    String country= "Israel";
    String city= "Ramat";
    String street= "Pozis";
    String zip = "123";
    @BeforeEach
    void setUp() {
        visitLoggedResponse = visitSystem();
        visitorLogged = visitLoggedResponse.getValue();
        visitResponse2 = visitSystem();
        visitor2 = visitResponse2.getValue();
        visitResponse3 = visitSystem();
        visitor3 = visitResponse3.getValue();
        visitResponseAdmin = visitSystem();
        visitorAdmin = visitResponseAdmin.getValue();
        loginUserResponse = login(visitorLogged.getId(), memberUsername1, members.get(memberUsername1));
        visitorLogged= loginUserResponse.getValue();
        loginUserResponse = login(visitor2.getId(), memberUsername2, members.get(memberUsername2));
        visitor2= loginUserResponse.getValue();
        loginUserResponse = login(visitorAdmin.getId(), adminUser, adminPass);
        visitorAdmin= loginUserResponse.getValue();
        openNewShopResponse=  openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();
        newProductResponse1= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity,price,description,category1);
        productResponse1= newProductResponse1.getValue();
        addProductToShoppingCart(visitor3.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        purchaseShoppingCart(visitor3.getId(),creditCard,date,cvs,country,city,street,zip);
    }

    @AfterEach
    void tearDown() {
        leaveSystem(visitor2.getId());
        visitor2 = null;
        leaveSystem(visitor3.getId());
        visitor3 = null;
        leaveSystem(visitorAdmin.getId());
        visitorAdmin = null;
        closeShop(visitorLogged.getId(),shopResponse.getId());
        logout(visitorLogged.getId());
    }

}
//TODO: init tests, next version editing interface