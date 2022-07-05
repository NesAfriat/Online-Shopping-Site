package AcceptanceTests;

import Domain.Market.Responses.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MarketCostumerTests extends MarketTests{
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
        logout(visitorLogged.getId());
    }
    //use case 6 #issue 29
    @Test
    void getShopInfoSuccess() {
        Response<ShopResponse> response = getShopInfo(visitorLogged.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        ShopResponse shopInfoResponse= response.getValue();
        assertNotNull(shopInfoResponse);
        assertEquals(shopInfoResponse.getName(), shopResponse.getName());
        Map<Integer, ProductResponse> produtsResponse= shopInfoResponse.getProducts();
        assertFalse((produtsResponse.isEmpty()));
        assertEquals(produtsResponse.get(productResponse1.getId()).getProductName(),productName1);
    }

    @Test
    void getShopInfoWithoutVisiting() {
        Response<ShopResponse> response = getShopInfo(notUserId, shopResponse.getId());
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse shopInfo = response.getValue();
        assertNull(shopInfo);
    }
    @Test
    void getShopInfoWithoutLogging() {
        visitResponse2=logout(visitor2.getId());
        visitor2= visitResponse2.getValue();
        Response<ShopResponse> response = getShopInfo(visitor2.getId(), shopResponse.getId());
        assertFalse(response.isErrorOccurred());
        ShopResponse shopInfo = response.getValue();
        assertNotNull(shopInfo);
        login(visitor2.getId(), memberUsername2, members.get(memberUsername2));
    }

    @Test
    void getShopInfoInvalidShop() {
        Response<ShopResponse> response = getShopInfo(visitor2.getId(), notShopID);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        ShopResponse shopInfo = response.getValue();
        assertNull(shopInfo);
    }
    //use case 7 #issue 27
    @Test
    void searchProductByNameSuccess() {
        openNewShopResponse=  openShop(visitorLogged.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        shopResponse= openNewShopResponse.getValue();
        newProductResponse1= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity,price,description,category1);
        productResponse1= newProductResponse1.getValue();
        Response<List<ProductResponse>> response = searchProductByName(visitorLogged.getId(),productName1);
        assertFalse(response.isErrorOccurred());
        assertTrue(response.getValue().size()==2);
    }

    @Test
    void searchProductByNameEmpty() {
        Response<List<ProductResponse>> response = searchProductByName(notUserId,"");
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void searchProductByNameNull() {
        Response<List<ProductResponse>> response = searchProductByName(notUserId,null);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void searchProductByNameNotVisitorNotProduct() {
        Response<List<ProductResponse>> response = searchProductByName(visitorLogged.getId(),notProductName);
        assertTrue(response.getValue().isEmpty());
    }

    //use case 8 #issue 32
    @Test
    void addProductToShoppingCartSuccess() {
        Response<ShoppingCartResponse> response= addProductToShoppingCart(visitor2.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        assertFalse(response.isErrorOccurred());
        ShoppingCartResponse shoppingCartResponse= response.getValue();
        assertNotNull(shoppingCartResponse);
        ShoppingBasketResponse shoppingBasketResponse = shoppingCartResponse.baskets.get(shopResponse.getId());
        assertEquals(shoppingBasketResponse.getProductId(productResponse1.getId()).intValue(),productResponse1.getId());
    }
    @Test
    void addProductToShoppingCartZeroQuantity() {
        Response<ShoppingCartResponse> response= addProductToShoppingCart(visitor2.getId(),shopResponse.getId(),productResponse2.getId(),illegalquantity);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void addProductToShoppingCartNegativeQuantity() {
        Response<ShoppingCartResponse> response= addProductToShoppingCart(visitor2.getId(),shopResponse.getId(),productResponse2.getId(),negativeQuantity);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void addProductToShoppingCartNotVisitor() {
        Response<ShoppingCartResponse> response= addProductToShoppingCart(notUserId,shopResponse.getId(),productResponse2.getId(),quantity);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void addProductToShoppingCartInvalidShopID() {
        Response<ShoppingCartResponse> response= addProductToShoppingCart(visitor2.getId(),notShopID,productResponse2.getId(),quantity);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void addProductToShoppingCartInvalidProductID() {
        Response<ShoppingCartResponse> response= addProductToShoppingCart(visitor2.getId(),visitor2.getId(),notProductID,quantity);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    
    //use case 10 #issue 48
    //TODO: policy purchase checks
    @Test
    void purchaseShoppingCartSuccess()
    {
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street);
        assertFalse(response2.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response2.getValue();
        assertTrue(purchaseHistoryResponse.get(0).getProductIdToQuantity().size()==2); //Ive changed here
        for (PurchaseHistoryResponse res:purchaseHistoryResponse
             ) {
            assertTrue(res.getShopId()==shopResponse.getId());
        }
    }
    @Test
    void purchaseShoppingCart1BasketFail()
    {
        //TODO here too - what happens if you cant buy an item in the basket?
        Response<ShopResponse> openNewShopResponse2=  openShop(visitor2.getId(),memberPhone,creditCard,GenerateNewShopName(),shopDescriptionValid,shopLocation);
        ShopResponse shopResponse2= openNewShopResponse2.getValue();
        newProductResponse3= addProduct(visitor2.getId(),shopResponse2.getId(),productName3,quantity,price,description,category2);
        productResponse3= newProductResponse3.getValue();
        addProductToShoppingCart(visitor3.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        addProductToShoppingCart(visitor3.getId(),shopResponse2.getId(),productResponse3.getId(),quantity*2);
        Response<List<PurchaseHistoryResponse>> response2= purchaseShoppingCart(visitor3.getId(),creditCard,date,cvs,country,city,street);
        int counterFailed=0;
        int counterSuccess=0;
        assertFalse(response2.isErrorOccurred());
        for (PurchaseHistoryResponse phr:response2.getValue()
        ) {
            if(phr.getPurchaseError()!=null)
                counterFailed++;
                else counterSuccess++;
        }
        assertTrue(counterFailed==1 && counterSuccess==1);
    }
    @Test
    void purchaseShoppingCartIllegalQuantity() {
        //Ive added todo in purchaseCart for this dilema
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity*2);
        Response<List<PurchaseHistoryResponse>> response= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street);
        assertFalse(response.isErrorOccurred());
        for (PurchaseHistoryResponse phr:response.getValue()
             ) {
                assertTrue(phr.getPurchaseError()!=null);
        }
    }
    @Test
    void purchaseShoppingCartEmpty() {
        Response<List<PurchaseHistoryResponse>> response= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void purchaseShoppingCartNotVisitor() {
        Response<List<PurchaseHistoryResponse>> response= purchaseShoppingCart(notUserId,creditCard,date,cvs,country,city,street);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void purchaseShoppingCartIllegalCreditCard() {
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        Response<List<PurchaseHistoryResponse>> response= purchaseShoppingCart(visitor4.getId(),invalidCreditNumber,date,cvs,country,city,street);
        assertTrue(response.isErrorOccurred());
        assertFalse(response.getErrorMessage().contains("fatal"));
        assertNull(response.getValue());
    }
    @Test
    void purchaseShoppingCartCloseShop() {
        addProductToShoppingCart(visitor4.getId(),shopResponse.getId(),productResponse1.getId(),quantity);
        closeShop(visitorLogged.getId(),shopResponse.getId());
        Response<List<PurchaseHistoryResponse>> response= purchaseShoppingCart(visitor4.getId(),creditCard,date,cvs,country,city,street);
        assertFalse(response.isErrorOccurred());
        List<PurchaseHistoryResponse> purchaseHistoryResponse= response.getValue();
        assertTrue(purchaseHistoryResponse.isEmpty());
    }

}


