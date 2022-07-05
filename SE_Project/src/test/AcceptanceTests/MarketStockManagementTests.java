package AcceptanceTests;

import Domain.Market.Responses.ProductResponse;
import Domain.Market.Responses.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


public class MarketStockManagementTests extends MarketTests{
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
        newProductResponse1= addProduct(visitorLogged.getId(), shopResponse.getId(),productName1,quantity,price,description,category1);
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
    //stock management UC 19 issue 59
    @Test
    void addProductSuccess()
    {
        assertFalse(newProductResponse2.isErrorOccurred());
        assertNotNull(productResponse2);
        assertEquals(productResponse2.getProductName(),productName2);
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
    }
    @Test
    void addProductNotLogged()
    {   visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,quantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
        login(visitorLogged.getId(),memberUsername1,members.get(memberUsername1));
    }
    @Test
    void addProductNoRole()
    {   login(visitor4.getId(),memberUsername4,members.get(visitor4.getId()));
        newProductResponse3= addProduct(visitor4.getId(),shopResponse.getId(),productName3,quantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
        logout(visitor4.getId());
    }
    @Test
    void addProductInvalidShopID()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),notShopID,productName3,quantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductEmptyProductName()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),"",quantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductNullProductName()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),null,quantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductExistProductName()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName1,quantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductZeroQuantity()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,illegalquantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductNegativeQuantity()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,negativeQuantity,price,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductNegativePrice()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,quantity,illegalPrice,description2,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductNullCategory()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,quantity,price,description2,null);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductEmptyCategory()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,quantity,price,description2,"");
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductEmptyDescription()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,quantity,price,"",category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void addProductNullDescription()
    {
        newProductResponse3= addProduct(visitorLogged.getId(),shopResponse.getId(),productName3,quantity,price,null,category2);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    //UC 19 issue 60
    @Test
    void removeProductSuccess()
    {   Response<ProductResponse> response= removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
        assertFalse(response.isErrorOccurred());
        productResponse2 = response.getValue();
        assertNotNull(productResponse2);
        assertEquals(productResponse2.getProductName(),productName2);
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
        newProductResponse2= addProduct(visitorLogged.getId(),shopResponse.getId(),productName2,quantity,price,description,category1);
        productResponse2= newProductResponse2.getValue();
    }
    @Test
    void removeProductNotLogged()
    {   visitLoggedResponse=logout(visitorLogged.getId());
        visitorLogged= visitLoggedResponse.getValue();
        newProductResponse3= removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
        login(visitorLogged.getId(),memberUsername1,members.get(memberUsername1));
    }
    @Test
    void removeProductNoRole()
    {   login(visitor4.getId(),memberUsername4,members.get(visitor4.getId()));
        newProductResponse3= removeProduct(visitor4.getId(),shopResponse.getId(),productResponse2.getId());
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
        logout(visitor4.getId());
    }
    @Test
    void removeProductInvalidShopID()
    {   newProductResponse3= removeProduct(visitorLogged.getId(),notShopID,productResponse2.getId());
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void removeProductRemovedProduct()
    {   newProductResponse2= removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
        newProductResponse3= removeProduct(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId());
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
        newProductResponse2= addProduct(visitorLogged.getId(),shopResponse.getId(),productName2,quantity,price,description,category1);
        productResponse2= newProductResponse2.getValue();
    }
    //UC 19 issue 61
    @Test
    void addAmountSuccess()
    {   newProductResponse2= addProductAmount(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        assertFalse(newProductResponse2.isErrorOccurred());
        productResponse2 = newProductResponse2.getValue();
        assertNotNull(productResponse2);
        assertEquals(productResponse2.getProductName(),productName2);
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
        assertEquals(productResponse2.getQuantity(),quantity*2);
    }
    @Test
    void addAmountNegativeValue()
    {   newProductResponse3= addProductAmount(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),negativeQuantity);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    //UC 19 issue 61
    @Test
    void reduceAmountSuccess()
    {   newProductResponse2= reduceProductAmount(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),quantity);
        assertFalse(newProductResponse2.isErrorOccurred());
        productResponse2 = newProductResponse2.getValue();
        assertNotNull(productResponse2);
        assertEquals(productResponse2.getProductName(),productName2);
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
        assertEquals(productResponse2.getQuantity(),0);
    }
    @Test
    void reduceAmountNegativeValue()
    {   newProductResponse3= reduceProductAmount(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),negativeQuantity);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    //UC 19 issue 61
    @Test
    void changeProductNameSuccess()
    {   newProductResponse2= changeProductName(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),productName2.concat("2"));
        assertFalse(newProductResponse2.isErrorOccurred());
        productResponse2 = newProductResponse2.getValue();
        assertNotNull(productResponse2);
        assertEquals(productResponse2.getProductName(),productName2.concat("2"));
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
    }
    @Test
    void changeProductNameEmptyName()
    {   newProductResponse3= changeProductName(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),"");
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void changeProductNameNullName()
    {   newProductResponse3= changeProductName(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),null);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    //UC 19 issue 61
    @Test
    void changeProductDescriptionSuccess()
    {   newProductResponse2= changeProductDescription(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),description2.concat("2"));
        assertFalse(newProductResponse2.isErrorOccurred());
        productResponse2 = newProductResponse2.getValue();
        assertNotNull(productResponse2);
        assertEquals(productResponse2.getDescription(),description2.concat("2"));
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
    }
    @Test
    void changeProductDescriptionEmpty()
    {   newProductResponse3= changeProductDescription(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),"");
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    @Test
    void changeProductDescriptionNull()
    {   newProductResponse3= changeProductDescription(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),null);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }
    //UC 19 issue 61
    @Test
    void updateProductPriceSuccess()
    {   double newPrice= 5.9;
        newProductResponse2= updateProductPrice(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),newPrice);
        assertFalse(newProductResponse2.isErrorOccurred());
        productResponse2 = newProductResponse2.getValue();
        assertNotNull(productResponse2);
        Assertions.assertEquals(productResponse2.getPrice(),newPrice);
        assertEquals(productResponse2.getShopId(),shopResponse.getId());
    }
    @Test
    void updateProductPriceNegative()
    {   newProductResponse3= updateProductPrice(visitorLogged.getId(),shopResponse.getId(),productResponse2.getId(),illegalPrice);
        assertTrue(newProductResponse3.isErrorOccurred());
        assertFalse(newProductResponse3.getErrorMessage().contains("fatal"));
        productResponse3= newProductResponse3.getValue();
        assertNull(productResponse3);
    }

}
