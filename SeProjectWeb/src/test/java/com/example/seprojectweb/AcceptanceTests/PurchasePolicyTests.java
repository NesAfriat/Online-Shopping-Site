package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.Market.Responses.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PurchasePolicyTests extends MarketTests{
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
        removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
        productResponse2=null;
        closeShop(visitorLogged.getId(),shopResponse.getId());
        leaveSystem(visitorLogged.getId());
    }

    @Test
    void Policy_Add_At_Least_From_Product_Policy_Purchase_Shopping_Cart_Success()
    {

        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        addAtLeastFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Policy_Add_At_Least_From_Product_Policy_Purchase_Shopping_Cart_Fail()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        addAtLeastFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==0);
        for (PurchaseHistoryResponse res:purchaseHistoryResponse) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }


    @Test
    void Policy_Add_At_Most_From_Product_Policy_Purchase_Shopping_Cart_Success()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        addAtMostFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); 
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Policy_Add_At_Most_From_Product_Policy_Purchase_Shopping_Cart_Fail()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        addAtMostFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==0);
        for (PurchaseHistoryResponse res:purchaseHistoryResponse) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Policy_And_Compose_Policy_Purchase_Shopping_Cart_Success()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);


        Response<PurchasePolicyResponse> policyResponseResponse1 =
                addAtLeastFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1
                );


        Response<PurchasePolicyResponse> policyResponseResponse2 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1
                );


        Response<PurchasePolicyResponse> policyResponseResponseComposed =
                composePurchasePolicies(
                        visitorLogged.getId(), shopResponse.getId(), CONDITION_COMPOSE_TYPE.AND, policyResponseResponse1.getValue().getPurchasePolicyId(), policyResponseResponse2.getValue().getPurchasePolicyId()
                );

        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Policy_And_Compose_Policy_Purchase_Shopping_Cart_Fail()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);


        Response<PurchasePolicyResponse> policyResponseResponse1 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1
                );


        Response<PurchasePolicyResponse> policyResponseResponse2 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1
                );


        Response<PurchasePolicyResponse> policyResponseResponseComposed =
                composePurchasePolicies(
                        visitorLogged.getId(), shopResponse.getId(), CONDITION_COMPOSE_TYPE.AND, policyResponseResponse1.getValue().getPurchasePolicyId(), policyResponseResponse2.getValue().getPurchasePolicyId()
                );

        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getPurchaseError() != null);
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }


    @Test
    void Policy_Or_Compose_Policy_Purchase_Shopping_Cart_Success_Both_True()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);


        Response<PurchasePolicyResponse> policyResponseResponse1 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1
                );


        Response<PurchasePolicyResponse> policyResponseResponse2 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1
                );


        Response<PurchasePolicyResponse> policyResponseResponseComposed =
                composePurchasePolicies(
                        visitorLogged.getId(), shopResponse.getId(), CONDITION_COMPOSE_TYPE.OR, policyResponseResponse1.getValue().getPurchasePolicyId(), policyResponseResponse2.getValue().getPurchasePolicyId()
                );

        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Policy_Or_Compose_Policy_Purchase_Shopping_Cart_Success_First_False()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);


        Response<PurchasePolicyResponse> policyResponseResponse1 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1
                );


        Response<PurchasePolicyResponse> policyResponseResponse2 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1
                );


        Response<PurchasePolicyResponse> policyResponseResponseComposed =
                composePurchasePolicies(
                        visitorLogged.getId(), shopResponse.getId(), CONDITION_COMPOSE_TYPE.OR, policyResponseResponse1.getValue().getPurchasePolicyId(), policyResponseResponse2.getValue().getPurchasePolicyId()
                );

        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }


    @Test
    void Policy_Or_Compose_Policy_Purchase_Shopping_Cart_Success_Second_False()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);


        Response<PurchasePolicyResponse> policyResponseResponse1 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity + 1
                );


        Response<PurchasePolicyResponse> policyResponseResponse2 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1
                );


        Response<PurchasePolicyResponse> policyResponseResponseComposed =
                composePurchasePolicies(
                        visitorLogged.getId(), shopResponse.getId(), CONDITION_COMPOSE_TYPE.OR, policyResponseResponse1.getValue().getPurchasePolicyId(), policyResponseResponse2.getValue().getPurchasePolicyId()
                );

        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Policy_Or_Compose_Policy_Purchase_Shopping_Cart_Fail_Both_False()
    {

        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);


        Response<PurchasePolicyResponse> policyResponseResponse1 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1
                );


        Response<PurchasePolicyResponse> policyResponseResponse2 =
                addAtMostFromProductPolicy(
                        visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1
                );


        Response<PurchasePolicyResponse> policyResponseResponseComposed =
                composePurchasePolicies(
                        visitorLogged.getId(), shopResponse.getId(), CONDITION_COMPOSE_TYPE.OR, policyResponseResponse1.getValue().getPurchasePolicyId(), policyResponseResponse2.getValue().getPurchasePolicyId()
                );

        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getPurchaseError() != null);
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }


    @Test
    void Policy_Add_At_Least_From_Product_Policy_Purchase_Shopping_Cart_Success2()
    {

        Response<ShoppingCartResponse> shoppingCart = addProductToShoppingCart(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        shoppingCart = addProductToShoppingCart(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        addAtLeastFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitorLogged.getId(),creditCard,date,cvs,country,city,street,zip);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
        ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }

    @Test
    void Purchase_Shopping_Cart_Success()
    {

        Response<ShoppingCartResponse> res1 = addProductToShoppingCart(visitor3.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<ShoppingCartResponse> res2 = addProductToShoppingCart(visitor3.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        Response<ShoppingCartResponse> res3 = addProductToShoppingCart(visitor3.getId(),shopResponse2.getId(),productResponse33.getId(),quantity);
        //addAtLeastFromProductPolicy(visitorLogged.getId(),shopResponse.getId(),productResponse1.getId(),quantity - 1);
        //Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor3.getId(),creditCard,date,cvs,country,city,street);
//        assertFalse(response2.isErrorOccurred());
//        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
//        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
//        for (PurchaseHistoryResponse res:purchaseHistoryResponse
//        ) {
//            assertTrue(res.getShopId()==shopResponse.getId());
//        }
    }
}
