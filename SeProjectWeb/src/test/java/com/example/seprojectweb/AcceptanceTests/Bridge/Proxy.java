package com.example.seprojectweb.AcceptanceTests.Bridge;

import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Responses.*;

import java.util.List;

public class Proxy implements TestsBridge {
    @Override
    public boolean initMarket() {
        return false;
    }

    @Override
    public Response<VisitorResponse> visitSystem() {
        return null;
    }

    @Override
    public Response<VisitorResponse> leaveSystem(int visitorId) {
        return null;
    }

    @Override
    public Response<MemberResponse> register(int visitorId, String username, String password,String email) {
        return null;
    }

    @Override
    public Response<VisitorResponse> login(int visitorId, String username, String password) {
        return null;
    }

    @Override
    public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver) {
        return null;
    }

    @Override
    public Response<VisitorResponse> logout(int visitorId) {
        return null;
    }

    @Override
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId) {
        return null;
    }

    @Override
    public Response<ShopResponse> getShopInfo(int visitorId, int shopId) {
        return null;
    }

    @Override
    public Response<List<ProductResponse>> searchProductByName(int visitorId, String productName) {
        return null;
    }

    @Override
    public Response<List<ProductResponse>> searchProductByCategory(int visitorId, String category) {
        return null;
    }

    @Override
    public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord) {
        return null;
    }

    @Override
    public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity) {
        return null;
    }

    @Override
    public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID) {
        return null;
    }

    @Override
    public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip)  {
        return null;
    }

    @Override
    public Response<ShopResponse> openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation)
    {
        return null;
    }

    @Override
    public Response<RoleResponse> assignShopOwner(int visitorId, String usernameToAssign , int shopID) {
        return null;
    }

    @Override
    public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign, int shopID) {
        return null;
    }

    @Override
    public Response<ProductResponse> addProduct(int visitorId, int shopId, String productName,int quantity, double price, String description,String category){
        return null;
    }

    @Override
    public Response<ProductResponse> removeProduct(int visitorId, int shopId, int productId) {
        return null;
    }

    @Override
    public Response<ProductResponse> updateProductPrice(int visitorIdAssign, int shopId, int productId, double price) {
        return null;
    }

    @Override
    public Response<ProductResponse> addProductAmount(int visitorIdAssign, int shopId, int productId, int amount) {
        return null;
    }

    @Override
    public Response<ProductResponse> reduceProductAmount(int visitorIdAssign, int shopId, int productId, int amount) {
        return null;
    }

    @Override
    public Response<ProductResponse> changeProductName(int visitorIdAssign, int shopId, int productId, String newName) {
        return null;
    }

    @Override
    public Response<ProductResponse> changeProductDescription(int visitorIdAssign, int shopId, int productId, String newDescription) {
        return null;
    }

    @Override
    public Response<ShopResponse> closeShop(int visitorIdFounder, int shopId) {
        return null;
    }

    @Override
    public Response<List<RoleResponse>> getShopRoleInfo(int visitorId, int shopId) {
        return null;
    }

    @Override
    public Response<ShopManagerResponse> setPermission(int visitorId, int shopId, String memberUserName, int permission) {
        return null;
    }

    @Override
    public Response<ShopManagerResponse> removePermission(int visitorId, int shopId, String memberUserName, int permission) {
        return null;
    }

    @Override
    public Response<RoleResponse> getPermissionsOfMember(int visitorId, String memberUserName, int shopId) {
        return null;
    }

    @Override
    public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId) {
        return null;
    }

    @Override
    public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category) {
        return null;
    }

    @Override
    public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay) {
        return null;
    }

    @Override
    public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay) {
        return null;
    }

    @Override
    public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) {
        return null;
    }

    @Override
    public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) {
        return null;
    }

    @Override
    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity) {
        return null;
    }

    @Override
    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity) {
        return null;
    }

    @Override
    public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2) {
        return null;
    }

    @Override
    public Response<List<ShopOwnerResponse>> removeShopOwner(int visitorId, String memberUserName, int shopId) {
        return null;
    }

    @Override
    public Response<List<VisitorResponse>> getAllVisitors(int visitorId) {
        return null;
    }

    @Override
    public Response<List<ShopOwnerResponse>> getAllOwners(int visitorId) {
        return null;
    }

    @Override
    public Response<BidResponse> bidProduct(int visitorId, int shopId, int productId, int quantity, double price) {
        return null;
    }

    @Override
    public Response<BidResponse> approveBid(int visitorId, int shopId, int bidId) {
        return null;
    }

    @Override
    public Response<BidResponse> incrementBidPrice(int visitorId, int shopId, int bidId, double newPrice) {
        return null;
    }

    @Override
    public Response<PurchaseHistoryResponse> purchaseBid(int visitorId, int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
        return null;
    }

    @Override
    public Response<List<BidResponse>> getAllMemberBid(int visitorId) {
        return null;
    }

    @Override
    public Response<List<BidResponse>> getAllShopBid(int visitorId, int shopId) {
        return null;
    }

    @Override
    public Response<List<AssignAgreementResponse>> getAllShopAssignAgreements(int visitorId, int shopID) {
        return null;
    }

    @Override
    public Response<AssignAgreementResponse> approveAssignAgreement(int visitorId, String usernameToAssign, int shopID) {
        return null;
    }

    @Override
    public Response<AssignAgreementResponse> initAssignShopOwner(int visitorId, String usernameToAssign, int shopID) {
        return null;
    }

    @Override
    public Response<List<DailySystemDataResponse>> getSystemData(int visitorId, String startDate, String endDate) {
        return null;
    }
}
