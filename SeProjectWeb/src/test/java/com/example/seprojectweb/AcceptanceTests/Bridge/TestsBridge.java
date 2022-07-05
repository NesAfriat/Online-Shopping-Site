package com.example.seprojectweb.AcceptanceTests.Bridge;

import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Responses.*;

import java.util.List;

public interface TestsBridge {

    //SystemActions
    public boolean initMarket();

    //VisitorActions
    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.m91jizn0v7bz
    public Response<VisitorResponse> visitSystem();

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.1y8zdrlvc1y5
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.b0wrdaa47eqx
    public Response<VisitorResponse> leaveSystem(int visitorId);

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.7yavqg1icq0n
    public Response<MemberResponse> register(int visitorId, String username, String password, String email);

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.pfnewjuuehz
    public Response<VisitorResponse> login(int visitorId, String username, String password);

    public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver);

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.lclpkag44rg6
    public Response<VisitorResponse> logout(int visitorId);

    //MarketActions
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.oj9vk8j4ei9u
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.98hydh4ba24m
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.4epnlanghpus
    public Response<ShopResponse> getShopInfo(int visitorId, int shopId);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.hng9qjdibqpr
    public Response<List<ProductResponse>> searchProductByName(int visitorId, String productName);

    public Response<List<ProductResponse>> searchProductByCategory(int visitorId, String category);

    //low priority
    public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.tiwq517k769e
    public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity);

    public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.jrgs75mxu4r
    public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.e84qubu9kszx
    Response<ShopResponse> openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.637hb4xt0u41
    public Response<RoleResponse> assignShopOwner(int visitorId, String usernameToAssign, int shopID);

    public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign, int shopID);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.1p8xr3ojo90m
    public Response<ProductResponse> addProduct(int visitorId, int shopId, String productName, int quantity, double price, String description, String category);

    public Response<ProductResponse> removeProduct(int visitorId, int shopId, int productId);

    public Response<ProductResponse> updateProductPrice(int visitorIdAssign, int shopId, int productId, double price);

    public Response<ProductResponse> addProductAmount(int visitorIdAssign, int shopId, int productId, int amount);

    public Response<ProductResponse> reduceProductAmount(int visitorIdAssign, int shopId, int productId, int amount);

    public Response<ProductResponse> changeProductName(int visitorIdAssign, int shopId, int productId, String newName);

    public Response<ProductResponse> changeProductDescription(int visitorIdAssign, int shopId, int productId, String newDescription);


    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.qdye3madi9n9
    public Response<ShopResponse> closeShop(int visitorIdFounder, int shopId);

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.18rhj2jkj3ue
    public Response<List<RoleResponse>> getShopRoleInfo(int visitorId, int shopId);

    public Response<ShopManagerResponse> setPermission(int visitorId, int shopId, String memberUserName, int permission);

    public Response<ShopManagerResponse> removePermission(int visitorId, int shopId, String memberUserName, int permission);

    public Response<RoleResponse> getPermissionsOfMember(int visitorId, String memberUserName, int shopId);

    public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId);
    public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category);
    public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay);
    public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay);
    public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity);
    public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice);
    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity);
    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity);
    public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2);

    Response<List<ShopOwnerResponse>> removeShopOwner(int visitorId, String memberUserName, int shopId);
    public Response<List<VisitorResponse>> getAllVisitors(int visitorId);

    public Response<List<ShopOwnerResponse>> getAllOwners(int visitorId);

    public Response<BidResponse> bidProduct(int visitorId, int shopId, int productId, int quantity, double price);

    public Response<BidResponse> approveBid(int visitorId, int shopId, int bidId);

    public Response<BidResponse> incrementBidPrice(int visitorId, int shopId, int bidId, double newPrice);

    public Response<PurchaseHistoryResponse> purchaseBid(int visitorId,int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip);


    public Response<List<BidResponse>> getAllMemberBid(int visitorId);

    public Response<List<BidResponse>> getAllShopBid(int visitorId, int shopId);

    public Response<List<AssignAgreementResponse>> getAllShopAssignAgreements(int visitorId, int shopID);
    public Response<AssignAgreementResponse> approveAssignAgreement(int visitorId, String usernameToAssign, int shopID);
    public Response<AssignAgreementResponse> initAssignShopOwner(int visitorId, String usernameToAssign, int shopID);

    public Response<List<DailySystemDataResponse>> getSystemData(int visitorId, String startDate, String endDate);

}


