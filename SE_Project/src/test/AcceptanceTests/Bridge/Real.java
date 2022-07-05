package AcceptanceTests.Bridge;


import AcceptanceTests.MemberObserverForTests;
import Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import Domain.Market.Notifications.IMemberObserver;
import Domain.Market.Responses.*;
import Service.MarketHandler.MarketHandler;
import Service.SystemHandler.SystemHandler;
import Service.VisitorHandler.VisitorHandler;

import java.util.List;


public class Real implements TestsBridge {
    VisitorHandler visitorHandler;
    SystemHandler systemHandler;
    MarketHandler marketHandler;
    public Real(VisitorHandler visitorHandler, SystemHandler systemHandler, MarketHandler marketHandler)
    {
        this.visitorHandler= visitorHandler;
        this.systemHandler= systemHandler;
        this.marketHandler= marketHandler;
    }
    @Override
    public boolean initMarket() {
        return systemHandler.initMarket();
    }

    @Override
    public Response<VisitorResponse> visitSystem() {
        return visitorHandler.visitSystem();
    }

    @Override
    public Response<VisitorResponse> leaveSystem(int visitorId) {
        return visitorHandler.leaveSystem(visitorId);
    }

    @Override
    public Response<MemberResponse> register(int visitorId, String username, String password, String email) {
        return visitorHandler.register(visitorId,username,password,email);
    }

    @Override
    public Response<VisitorResponse> login(int visitorId, String username, String password) {
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        return visitorHandler.login(visitorId,username,password, memberObserver);
    }

    @Override
    public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver) {
        return visitorHandler.login(visitorId,username,password, memberObserver);
    }

    @Override
    public Response<VisitorResponse> logout(int visitorId) {
        return visitorHandler.logout(visitorId);
    }

    @Override
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId) {
        return marketHandler.getShopPurchaseHistory(visitorId,shopId);
    }

    @Override
    public Response<ShopResponse> getShopInfo(int visitorId, int shopId) {
        return marketHandler.getShopInfo(visitorId,shopId);
    }

    @Override
    public Response<List<ProductResponse>> searchProductByName(int visitorId, String productName) {
        return marketHandler.searchProductByName(visitorId,productName);
    }

    @Override
    public Response<List<ProductResponse>> searchProductByCategory(int visitorId, String category) {
        return marketHandler.searchProductByCategory(visitorId,category);
    }

    @Override
    public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord) {
        return marketHandler.searchProductByKeyWord(visitorId,keyWord);
    }

    @Override
    public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity) {
        return marketHandler.addProductToShoppingCart(visitorID,shopID,productID,quantity);
    }

    @Override
    public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID) {
        return marketHandler.removeProductFromShoppingCart(visitorID, shopID, productID);
    }

    @Override
    public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street) {
        return marketHandler.purchaseShoppingCart(visitorID, creditCardNumber, date, cvs,country, city, street);
    }

    @Override
    public

    Response<ShopResponse> openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) {
        return marketHandler.openShop(visitorID, memberPhone, creditCard,shopName, shopDescription, shopLocation);
    }

    @Override
    public Response<RoleResponse> assignShopOwner(int visitorId, String usernameToAssign , int shopID) {
        return marketHandler.assignShopOwner(visitorId,usernameToAssign,shopID);
    }

    @Override
    public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign, int shopID) {
        return marketHandler.assignShopOwner(visitorId,usernameToAssign,shopID);
    }

    @Override
    public Response<ProductResponse> addProduct(int visitorId, int shopId, String productName,int quantity, double price, String description,String category) {
        return marketHandler.addProduct(visitorId,shopId,productName,quantity,price,description,category);
    }

    @Override
    public Response<ProductResponse> removeProduct(int visitorId, int shopId, int productId) {
        return marketHandler.removeProduct(visitorId,shopId,productId);
    }

    @Override
    public Response<ProductResponse> updateProductPrice(int visitorId, int shopId, int productId, double price) {
        return marketHandler.updateProductPrice(visitorId, shopId,productId,price);
    }


    @Override
    public Response<ProductResponse> addProductAmount(int visitorIdAssign, int shopId, int productId, int amount) {
        return marketHandler.addProductAmount(visitorIdAssign, shopId, productId, amount);
    }

    @Override
    public Response<ProductResponse> reduceProductAmount(int visitorIdAssign, int shopId, int productId, int amount) {
        return marketHandler.reduceProductAmount(visitorIdAssign, shopId, productId, amount);
    }

    @Override
    public Response<ProductResponse> changeProductName(int visitorIdAssign, int shopId, int productId, String newName) {
        return marketHandler.changeProductName(visitorIdAssign, shopId, productId, newName);
    }

    @Override
    public Response<ProductResponse> changeProductDescription(int visitorIdAssign, int shopId, int productId, String newDescription) {
        return marketHandler.changeProductDescription(visitorIdAssign, shopId, productId, newDescription);
    }

    @Override
    public Response<ShopResponse> closeShop(int visitorIdFounder, int shopId) {
        return marketHandler.closeShop(visitorIdFounder,shopId);
    }

    @Override
    public Response<List<RoleResponse>> getShopRoleInfo(int visitorId, int shopId) {
        return marketHandler.getShopRoleInfo(visitorId,shopId);
    }

    @Override
    public Response<ShopManagerResponse> setPermission(int visitorId, int shopId, String memberUserName, int permission) {
        return marketHandler.setPermission(visitorId, shopId, memberUserName, permission);
    }

    @Override
    public Response<ShopManagerResponse> removePermission(int visitorId, int shopId, String memberUserName, int permission) {
        return marketHandler.removePermission(visitorId, shopId, memberUserName, permission);
    }

    @Override
    public Response<RoleResponse> getPermissionsOfMember(int visitorId, String memberUserName, int shopId) {
        return marketHandler.getPermissionsOfMember(visitorId, memberUserName, shopId);
    }

    @Override
    public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId) {
        return marketHandler.addProductDiscount(visitorId, shopId, percentage, expireYear,expireMonth,  expireDay, productId);
    }

    @Override
    public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category) {
        return marketHandler.addCategoryDiscount(visitorId, shopId, percentage, expireYear,expireMonth,  expireDay, category);
    }

    @Override
    public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay) {
        return marketHandler.addTotalShopDiscount(visitorId, shopId, percentage, expireYear,expireMonth,  expireDay);
    }

    @Override
    public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay) {
        return marketHandler.addXorDiscount(visitorId,shopId,  discountAId,  discountBId, expireYear, expireMonth, expireDay);
    }

    @Override
    public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) {
        return marketHandler.addPQCondition(visitorId, shopId, type, discountId, productId, minProductQuantity);
    }

    @Override
    public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) {
        return marketHandler.addTBPCondition(visitorId, shopId, type,  discountId, minBasketPrice);
    }

    @Override
    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity) {
        return marketHandler.addAtMostFromProductPolicy(visitorId, shopId, productId, maxQuantity);
    }

    @Override
    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity) {
        return marketHandler.addAtLeastFromProductPolicy(visitorId, shopId, productId, minQuantity);
    }

    @Override
    public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2) {
        return marketHandler.composePurchasePolicies(visitorId,shopId, type,policyId1, policyId2);
    }
}
