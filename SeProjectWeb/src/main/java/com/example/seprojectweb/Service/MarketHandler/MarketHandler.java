package com.example.seprojectweb.Service.MarketHandler;


import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.MarketRepresentative;
import com.example.seprojectweb.Domain.Market.Responses.*;
import com.example.seprojectweb.Service.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketHandler {
    MarketRepresentative marketRepresentative;

    public MarketHandler() {
        marketRepresentative = MarketRepresentative.getInstance();
    }

    /***
     * add a product to the visitor shopping cart
     * @param visitorID
     * @param shopID
     * @param productID
     * @param quantity
     * @return the updated shopping cart
     */
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.tiwq517k769e
    public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity) {
        if (quantity <= 0) {
            return new Response<>("error - invalid quantity");
        }
        return marketRepresentative.addProductToShoppingCart(visitorID, shopID, productID, quantity);


    }

    /***
     * remove a product from the visitor shopping cart
     * @param visitorID
     * @param shopID
     * @param productID
     * @return the updated shopping cart
     */
    public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID) {
        return marketRepresentative.removeProductFromShoppingCart(visitorID, shopID, productID);
    }

    /***
     * purchasing the visitor shopping cart
     * @param visitorID
     * @param creditCardNumber
     * @param date
     * @param cvs
     * @param country
     * @param city
     * @param street
     * @return return a list of all purchases history
     */

    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.jrgs75mxu4r
    public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
        return marketRepresentative.purchaseCart(visitorID, creditCardNumber, date, cvs, country, city, street, zip);
    }


    /***
     * get the purchase history of a shop
     * @param visitorId
     * @param shopId
     * @return return a list of all purchases done in a shop
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.oj9vk8j4ei9u
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.98hydh4ba24m
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId) {
        return marketRepresentative.getShopPurchaseHistory(visitorId, shopId);
    }

    /***
     * get the info of a shop
     * @param visitorId
     * @param shopId
     * @return
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.4epnlanghpus
    public Response<ShopResponse> getShopInfo(int visitorId, int shopId) {
        return marketRepresentative.getShop(visitorId, shopId);
    }

    //TODO: add shop id in productResponse
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.hng9qjdibqpr

    /**
     * @param visitorId
     * @param productName name of a product not null
     * @return list of a product with the same name of the input
     */
    public Response<List<ProductResponse>> searchProductByName(int visitorId, String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return new Response<>("error - invalid product name");
        }
        return marketRepresentative.searchProductByName(visitorId, productName);
    }

    /**
     * @param visitorId
     * @param category  category of products
     * @return product from the input category
     */
    public Response<List<ProductResponse>> searchProductByCategory(int visitorId, String category) {
        return marketRepresentative.searchProductByKeyCategory(visitorId, category);
    }

    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.e84qubu9kszx
    public Response<ShopResponse> openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) {
        return marketRepresentative.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
    }

    /**
     * @param visitorId
     * @param keyWord   prefix key word
     * @return the product of the prefix keyword
     */
    public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord) {
        return marketRepresentative.searchProductByKeyWord(visitorId, keyWord);
    }


    /***
     * assign a shop owner to an existing shop
     * @param visitorId
     * @param usernameToAssign
     * @param shopID
     * @return the assigned shop owner
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.637hb4xt0u41
    public Response<AssignAgreementResponse> initAssignShopOwner(int visitorId, String usernameToAssign, int shopID) {
        if (!ServiceUtils.isValidUsername(usernameToAssign)) {
            return new Response<>("invalid username");
        }
        return marketRepresentative.initAssignShopOwner(visitorId, usernameToAssign, shopID);
    }

    /***
     * approve a shop owner assign agreement
     * @param visitorId
     * @param usernameToAssign
     * @param shopID
     * @return the assigned shop owner
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.637hb4xt0u41
    public Response<AssignAgreementResponse> approveAssignAgreement(int visitorId, String usernameToAssign, int shopID) {
        if (!ServiceUtils.isValidUsername(usernameToAssign)) {
            return new Response<>("invalid username");
        }
        return marketRepresentative.approveAssignAgreement(visitorId, usernameToAssign, shopID);
    }

    /***
     * get all active assign agreements of a shop
     * @param visitorId
     * @param shopID
     * @return the assigned shop owner
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.637hb4xt0u41
    public Response<List<AssignAgreementResponse>> getAllShopAssignAgreements(int visitorId, int shopID) {
        return marketRepresentative.getAllShopAssignAgreements(visitorId, shopID);
    }

    /***
     * init a shop owner agreement to an existing shop
     * @param visitorId
     * @param usernameToAssign
     * @param shopID
     * @return the assign agreement
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.637hb4xt0u41
    public Response<RoleResponse> assignShopOwner(int visitorId, String usernameToAssign, int shopID) {
        if (!ServiceUtils.isValidUsername(usernameToAssign)) {
            return new Response<>("invalid username");
        }
        return marketRepresentative.assignShopOwner(visitorId, usernameToAssign, shopID);
    }

    /***
     * assign a shop manager to an existing shop
     * @param visitorId
     * @param usernameToAssign
     * @param shopID
     * @return the assigned shop manager
     */
    public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign, int shopID) {
        if (!ServiceUtils.isValidUsername(usernameToAssign)) {
            return new Response<>("invalid username");
        }
        return marketRepresentative.assignShopManager(visitorId, usernameToAssign, shopID);
    }


    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.1p8xr3ojo90m

    /**
     * @param visitorId
     * @param shopId
     * @param productName name of the new product
     * @param price       of the new product (double)
     * @param description the new product
     * @param quantity    the new product
     * @return a response with the new product
     */
    public Response<ProductResponse> addProduct(int visitorId, int shopId, String productName, int quantity, double price, String description, String category) {
        if (productName == null || productName.trim().isEmpty()) {
            return new Response<>("product name cannot be empty");
        }
        if (quantity <= 0) {
            return new Response<>("product quantity must be higher then zero");
        }
        if (price <= 0) {
            return new Response<>("product price must be higher then zero");
        }
        if (description == null || description.trim().isEmpty()) {
            return new Response<>("description cannot be empty");
        }
        if (category == null || category.trim().isEmpty()) {
            return new Response<>("category cannot be empty");
        }
        return marketRepresentative.addProductToShop(visitorId, shopId, productName, price, description, quantity, category);
    }

    /**
     * @param visitorId
     * @param shopId
     * @param productId
     * @return a response with the product that was removed
     */
    public Response<ProductResponse> removeProduct(int visitorId, int shopId, int productId) {
        return marketRepresentative.removeProductFromShop(visitorId, shopId, productId);
    }

    /**
     * @param visitorIdAssign
     * @param shopId
     * @param productId
     * @param amount          a positive number to add
     * @return the new product with the new amount
     */
    public Response<ProductResponse> addProductAmount(int visitorIdAssign, int shopId, int productId, int amount) {
        if (amount <= 0) {
            return new Response<>("amount cannot be negative or equal to zero");
        }
        return marketRepresentative.addProductQuantity(visitorIdAssign, shopId, productId, amount);
    }

    /**
     * @param visitorIdAssign
     * @param shopId
     * @param productId
     * @param amount          a positive number to remove
     * @return the new product with the new amount
     */
    public Response<ProductResponse> reduceProductAmount(int visitorIdAssign, int shopId, int productId, int amount) {
        if (amount <= 0) {
            return new Response<>("amount cannot be negative or equal to zero");
        }
        return marketRepresentative.reduceProductQuantity(visitorIdAssign, shopId, productId, amount);
    }

    /**
     * @param visitorIdAssign
     * @param shopId
     * @param productId
     * @param newName
     * @returna response with the produce that contains the new name
     */
    public Response<ProductResponse> changeProductName(int visitorIdAssign, int shopId, int productId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return new Response<>("product name cannot be empty");
        }
        return marketRepresentative.changeProductName(visitorIdAssign, shopId, productId, newName);
    }

    /***
     *
     * @param visitorIdAssign
     * @param shopId
     * @param productId
     * @param newDescription
     * @return response with the produce that contains the new description
     */
    public Response<ProductResponse> changeProductDescription(int visitorIdAssign, int shopId, int productId, String newDescription) {
        if (newDescription == null || newDescription.trim().isEmpty()) {
            return new Response<>("description cannot be empty");
        }
        return marketRepresentative.changeProductDescription(visitorIdAssign, shopId, productId, newDescription);
    }

    /***
     *
     * @param visitorIdAssign
     * @param shopId
     * @param productId
     * @param price
     * @return response with the produce that contains the new price
     */
    public Response<ProductResponse> updateProductPrice(int visitorIdAssign, int shopId, int productId, double price) {
        if (price <= 0) {
            return new Response<>("product price must be higher then zero");
        }
        return marketRepresentative.changeProductPrice(visitorIdAssign, shopId, productId, price);
    }

    /***
     * Founder closing his shop
     * @param visitorIdFounder
     * @param shopId
     * @return the closed shop
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.qdye3madi9n9
    public Response<ShopResponse> closeShop(int visitorIdFounder, int shopId) {
        return marketRepresentative.closeShop(visitorIdFounder, shopId);
    }

    /***
     * Founder re-opening his shop
     * @param visitorIdFounder
     * @param shopId
     * @return the re-opened shop
     */
    public Response<ShopResponse> reOpenShop(int visitorIdFounder, int shopId) {
        return marketRepresentative.reOpenShop(visitorIdFounder, shopId);
    }

    /***
     * return an overview of the shop management
     * @param visitorId
     * @param shopId
     * @return
     */
    //use case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.18rhj2jkj3ue
    public Response<List<RoleResponse>> getShopRoleInfo(int visitorId, int shopId) {
        return marketRepresentative.getShopManagementInfo(visitorId, shopId);
    }

    /***
     *
     * @param visitorId
     * @param memberUserName
     * @param shopId
     * @return
     */
    public Response<RoleResponse> getPermissionsOfMember(int visitorId, String memberUserName, int shopId) {
        return marketRepresentative.getPermissionsOfMember(visitorId, memberUserName, shopId);
    }

    /***
     *
     * @param visitorId
     * @param shopId
     * @param memberUserName
     * @param permission
     * @return
     */
    public Response<ShopManagerResponse> removePermission(int visitorId, int shopId, String memberUserName, int permission) {
        return marketRepresentative.removePermission(visitorId, shopId, memberUserName, permission);
    }

    /***
     *
     * @param visitorId
     * @param visitorId
     * @param shopId
     * @param memberUserName
     * @param permission
     * @return
     */
    public Response<ShopManagerResponse> setPermission(int visitorId, int shopId, String memberUserName, int permission) {
        return marketRepresentative.setPermission(visitorId, shopId, memberUserName, permission);
    }

    public Response<MemberResponse> cancelMemberShip(int visitorId, String memberUserName) {
        return marketRepresentative.cancelMembership(visitorId, memberUserName);
    }


    public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId) {
        return marketRepresentative.addProductDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay, productId);
    }

    public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category) {
        return marketRepresentative.addCategoryDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay, category);
    }

    public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay) {
        return marketRepresentative.addTotalShopDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay);
    }

    public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay) {
        return marketRepresentative.addXorDiscount(visitorId, shopId, discountAId, discountBId, expireYear, expireMonth, expireDay);
    }

    public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) {
        return marketRepresentative.addPQCondition(visitorId, shopId, type, discountId, productId, minProductQuantity);
    }

    public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) {
        return marketRepresentative.addTBPCondition(visitorId, shopId, type, discountId, minBasketPrice);
    }

    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity) {
        return marketRepresentative.addAtMostFromProductPolicy(visitorId, shopId, productId, maxQuantity);
    }

    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity) {
        return marketRepresentative.addAtLeastFromProductPolicy(visitorId, shopId, productId, minQuantity);
    }

    public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2) {
        return marketRepresentative.composePurchasePolicies(visitorId, shopId, type, policyId1, policyId2);
    }
    public Response<List<ShopResponse>> getAllOpenShops(int visitorId){
       return marketRepresentative.getAllOpenShops(visitorId);
    }
    public Response<List<MemberResponse>> getAllMembers (int visitorId){
        return marketRepresentative.getAllMembers(visitorId);
    }
    public Response<List<ShopOwnerResponse>> removeShopOwner(int visitorId, String memberUserName, int shopId) {
        return marketRepresentative.removeShopOwner(visitorId, memberUserName, shopId);
    }

    public Response<List<VisitorResponse>> getAllVisitors(int visitorId){
        return marketRepresentative.getAllVisitors(visitorId);
    }
    public Response<List<ShopOwnerResponse>> getAllOwners(int visitorId) {
        return marketRepresentative.getAllShopOwners(visitorId);
    }

    public Response<List<ShopResponse>> getMyShops(Integer visitorId) {
        return marketRepresentative.getMyShops(visitorId);
    }

    public Response<List<DiscountResponse>> getDiscountsPolicies(Integer visitorId, Integer shopId) {
        return marketRepresentative.getDiscountsPolicies(visitorId, shopId);
    }

    public Response<List<PurchasePolicyResponse>> getPurchasesPolicies(Integer visitorId, Integer shopId) {
        return marketRepresentative.getPurchasesPolicies(visitorId, shopId);
    }

    public Response<Boolean> isAdmin(Integer visitorId) {
        return marketRepresentative.isAdmin(visitorId);
    }

    public Response<ShoppingCartResponse> getVisitorCart(Integer visitorId) {
        return marketRepresentative.getVisitorCart(visitorId);
    }

    public Response<BidResponse> bidProduct(int visitorId, int shopId, int productId, int quantity, double price) {
        return marketRepresentative.bidProduct(visitorId, shopId, productId, quantity, price);
    }

    public Response<BidResponse> approveBid(int visitorId, int shopId, int bidId) {
        return marketRepresentative.approveBid(visitorId, shopId, bidId);
    }

    public Response<BidResponse> incrementBidPrice(int visitorId, int shopId, int bidId, double newPrice) {
        return marketRepresentative.incrementBidPrice(visitorId, shopId, bidId, newPrice);
    }

    public Response<PurchaseHistoryResponse> purchaseBid(int visitorId,int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
        return marketRepresentative.purchaseBid(visitorId, bidId, creditCardNumber, date, cvs, country, city, street, zip);
    }

    public Response<List<BidResponse>> getAllMemberBid(int visitorId) {
        return marketRepresentative.getAllMemberBid(visitorId);
    }

    public Response<List<BidResponse>> getAllShopBid(int visitorId, int shopId) {
        return marketRepresentative.getAllShopBid(visitorId, shopId);
    }

    public void initMarket() {
        marketRepresentative.initMarket();
    }
}
