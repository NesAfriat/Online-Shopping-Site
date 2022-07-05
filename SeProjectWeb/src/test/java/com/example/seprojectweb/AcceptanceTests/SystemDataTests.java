package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.AcceptanceTests.Bridge.Real;
import com.example.seprojectweb.AcceptanceTests.Bridge.TestsBridge;
import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.MarketRepresentative;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Responses.*;
import com.example.seprojectweb.Domain.Market.SystemData.SystemDataLogger;
import com.example.seprojectweb.Domain.PersistenceManager;
import com.example.seprojectweb.Service.MarketHandler.MarketHandler;
import com.example.seprojectweb.Service.SystemHandler.SystemHandler;
import com.example.seprojectweb.Service.VisitorHandler.VisitorHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class SystemDataTests{

        static TestsBridge testsBridge;


        VisitorResponse visitorAdmin;
        VisitorResponse visitorShopOwner1;
        VisitorResponse visitorShopOwner2;
        VisitorResponse visitorShopManager1;
        VisitorResponse visitorShopManager2;
        VisitorResponse visitorRoleLess;

        public SystemDataTests(){
                PersistenceManager.setDBConnection("test");
                testsBridge = new Real(new VisitorHandler(),new SystemHandler(),new MarketHandler());
        }


        @BeforeEach
        void setUp() {
        }

        @AfterEach
        void tearDown() {

        }

        @Test
        void getSystemData_simple_case_success()
        {
                visitorAdmin = visitSystem().getValue();
                MemberObserverForTests adminMailBox = new MemberObserverForTests();
                MemberResponse memberResponse = login(visitorAdmin.getId(), MarketRepresentative.DEFAULT_ADMIN_USERNAME,
                        MarketRepresentative.DEFAULT_ADMIN_PASSWORD, adminMailBox).getValue().getLoggedIn();
                String toDay = dateToString(new Date());
                List<DailySystemDataResponse> dataList = getSystemData(visitorAdmin.getId(), toDay, toDay).getValue();
                assertEquals(dataList.size(), 1);

        }

        private String dateToString(Date date){
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                return formatter.format(date);
        }





        // service methods

        public boolean initMarket() {
                return testsBridge.initMarket();
        }

        public Response<VisitorResponse> visitSystem() {
                return testsBridge.visitSystem();
        }

        public Response<VisitorResponse> leaveSystem(int visitorId) {
                return testsBridge.leaveSystem(visitorId);
        }

        public Response<MemberResponse> register(int visitorId, String username, String password,String email) {
                return testsBridge.register(visitorId,username,password,email);
        }

        public Response<VisitorResponse> login(int visitorId, String username, String password) {
                IMemberObserver observer = new MemberObserverForTests();
                return testsBridge.login(visitorId, username, password, observer);
        }

        public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver) {
                return testsBridge.login(visitorId, username, password, memberObserver);
        }

        public Response<VisitorResponse> logout(int visitorId) {
                return testsBridge.logout(visitorId);
        }

        public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId) {
                return testsBridge.getShopPurchaseHistory(visitorId,shopId);
        }

        public Response<ShopResponse> getShopInfo(int visitorId, int shopId) {
                return testsBridge.getShopInfo(visitorId,shopId);
        }

        public Response<List<ProductResponse>> searchProductByName(int visitorId, String productName) {
                return testsBridge.searchProductByName(visitorId,productName);
        }

        public Response<List<ProductResponse>> searchProductByCategory(int visitorId, String category) {
                return testsBridge.searchProductByCategory(visitorId,category);
        }

        public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord) {
                return testsBridge.searchProductByKeyWord(visitorId,keyWord);
        }

        public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity) {
                return testsBridge.addProductToShoppingCart(visitorID,shopID, productID, quantity);
        }

        public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
                return testsBridge.purchaseShoppingCart(visitorID, creditCardNumber, date, cvs,country, city, street, zip);
        }

        public Response<ShopResponse> openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) {
                return testsBridge.openShop(visitorID, memberPhone, creditCard,shopName, shopDescription, shopLocation);
        }

        public Response<RoleResponse> assignShopOwner(int visitorId, String usernameToAssign , int shopID) {
                return testsBridge.assignShopOwner(visitorId,usernameToAssign,shopID);
        }
        public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign , int shopID){
                return testsBridge.assignShopManager(visitorId,usernameToAssign,shopID);
        }

        public Response<ProductResponse> addProduct(int visitorId, int shopId, String productName,int quantity, double price, String description,String category) {
                return testsBridge.addProduct(visitorId,shopId,productName,quantity,price,description,category);
        }

        public Response<ProductResponse> removeProduct(int visitorId, int shopId, int productId) {
                return testsBridge.removeProduct(visitorId,shopId,productId);
        }

        public Response<ProductResponse> updateProductPrice(int visitorId, int shopId, int productId, double price) {
                return testsBridge.updateProductPrice(visitorId, shopId,productId,price);
        }

        public Response<ProductResponse> addProductAmount(int visitorId, int shopId, int productId, int amount)
        {
                return testsBridge.addProductAmount(visitorId,shopId,productId,amount);
        }
        public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID){
                return testsBridge.removeProductFromShoppingCart(visitorID, shopID, productID);
        }
        public Response<ProductResponse> reduceProductAmount(int visitorId, int shopId, int productId, int amount)
        {
                return testsBridge.reduceProductAmount(visitorId,shopId,productId,amount);
        }
        public Response<ProductResponse> changeProductName(int visitorId, int shopId, int productId,String newName){
                return testsBridge.changeProductName(visitorId,shopId,productId,newName);
        }
        public Response<ProductResponse> changeProductDescription(int visitorId, int shopId, int productId,String newDescription)
        {
                return testsBridge.changeProductDescription(visitorId,shopId,productId,newDescription);
        }

        public Response<ShopResponse> closeShop(int visitorIdFounder, int shopId) {
                return testsBridge.closeShop(visitorIdFounder,shopId);
        }

        public Response<List<RoleResponse>> getShopRoleInfo(int visitorId, int shopId) {
                return testsBridge.getShopRoleInfo(visitorId,shopId);
        }

        public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId){
                return testsBridge.addProductDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay, productId);
        }
        public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category){
                return testsBridge.addCategoryDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay, category);
        }
        public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay){
                return testsBridge.addTotalShopDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay);
        }
        public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay){
                return testsBridge.addXorDiscount(visitorId, shopId, discountAId, discountBId, expireYear, expireMonth, expireDay);
        }
        public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity){
                return testsBridge.addPQCondition(visitorId, shopId, type, discountId, productId, minProductQuantity);
        }
        public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice){
                return testsBridge.addTBPCondition(visitorId, shopId, type, discountId, minBasketPrice);
        }
        public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity){
                return testsBridge. addAtMostFromProductPolicy(visitorId, shopId, productId, maxQuantity);
        }
        public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity){
                return testsBridge.addAtLeastFromProductPolicy(visitorId, shopId, productId, minQuantity);
        }
        public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2){
                return testsBridge.composePurchasePolicies(visitorId, shopId, type, policyId1, policyId2);
        }

        public Response<List<ShopOwnerResponse>> removeShopOwner(int visitorId, String memberUserName, int shopId) {
                return testsBridge.removeShopOwner(visitorId, memberUserName, shopId);
        }
        public Response<List<VisitorResponse>> getAllVisitors(int visitorId){
                return testsBridge.getAllVisitors(visitorId);
        }
        public Response<List<ShopOwnerResponse>> getAllOwners(int visitorId){
                return testsBridge.getAllOwners(visitorId);
        }


        public Response<BidResponse> bidProduct(int visitorId, int shopId, int productId, int quantity, double price) {
                return testsBridge.bidProduct(visitorId, shopId, productId, quantity, price);
        }

        public Response<BidResponse> approveBid(int visitorId, int shopId, int bidId) {
                return testsBridge.approveBid(visitorId, shopId, bidId);
        }

        public Response<BidResponse> incrementBidPrice(int visitorId, int shopId, int bidId, double newPrice) {
                return testsBridge.incrementBidPrice(visitorId, shopId, bidId, newPrice);
        }

        public Response<PurchaseHistoryResponse> purchaseBid(int visitorId, int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
                return testsBridge.purchaseBid(visitorId, bidId, creditCardNumber, date, cvs, country, city, street, zip);
        }


        public Response<List<BidResponse>> getAllMemberBid(int visitorId) {
                return testsBridge.getAllMemberBid(visitorId);
        }


        public Response<List<BidResponse>> getAllShopBid(int visitorId, int shopId) {
                return testsBridge.getAllShopBid(visitorId, shopId);
        }

        public Response<List<AssignAgreementResponse>> getAllShopAssignAgreements(int visitorId, int shopID) {
                return testsBridge.getAllShopAssignAgreements(visitorId, shopID);
        }

        public Response<AssignAgreementResponse> approveAssignAgreement(int visitorId, String usernameToAssign, int shopID) {
                return testsBridge.approveAssignAgreement(visitorId, usernameToAssign, shopID);
        }

        public Response<AssignAgreementResponse> initAssignShopOwner(int visitorId, String usernameToAssign, int shopID) {
                return testsBridge.initAssignShopOwner(visitorId, usernameToAssign, shopID);
        }

        public Response<List<DailySystemDataResponse>> getSystemData(int visitorId, String startDate, String endDate) {
                return testsBridge.getSystemData(visitorId, startDate, endDate);
        }


}
