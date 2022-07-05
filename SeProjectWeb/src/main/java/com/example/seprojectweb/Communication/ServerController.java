package com.example.seprojectweb.Communication;

import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;

import com.example.seprojectweb.InitData.InitLoadData;
import com.example.seprojectweb.Logger;
import com.example.seprojectweb.Domain.Market.Responses.*;
import com.example.seprojectweb.Service.MarketHandler.MarketHandler;
import com.example.seprojectweb.Service.SystemHandler.SystemHandler;
import com.example.seprojectweb.Service.VisitorHandler.VisitorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController("/")

@RequestMapping("/server/")
public class ServerController {

    //private static final Logger LOG = LoggerFactory.getLogger(ServerController.class);

    private final VisitorHandler visitorHandler;
    private final SystemHandler systemHandler;
    private final MarketHandler marketHandler;
    private final Logger logger;
    private final WebSocketController webSocketController;
    private static final String initDataFilePath = "initData.txt";

    @Autowired
    public ServerController(VisitorHandler vh, SystemHandler sh, MarketHandler mh, WebSocketController ws, Logger lg) {
        visitorHandler = vh;
        systemHandler = sh;
        marketHandler = mh;
        webSocketController = ws;
        logger = lg;
       initDataFromInitFile();
    }

    private void initDataFromInitFile() {

//        LoadData loadData = new LoadData(visitorHandler, systemHandler, marketHandler, webSocketController);
//        try {
//            loadData.loadData(initDataFilePath);
//        }
//        catch (InnerLogicException e){
//            System.err.println("init data failed due of logic error.\n" + e.getMessage());
//            logger.logError("init data failed due of logic error.\n "+ e.getMessage());
//            System.exit(1);
//        }
//        catch (IOException e){
//            System.err.println("init data failed due of io error.\nerror: " + e.getMessage());
//            logger.logError("init data failed due of io error.\n error: "+ e.getMessage());
//            System.exit(1);
//        }
//        catch (IllegalAccessError | Exception e) {
//            System.err.println("init data failed due of syntax error.\nerror:" + e.getMessage());
//            logger.logError("init data failed due of syntax error.\nerror: "+ e.getMessage());
//            System.exit(1);
//        }

        // this function prepare the db for load tests.
      //  InitLoadData initDataClass = new InitLoadData(visitorHandler, marketHandler, webSocketController);
      //  initDataClass.initData("initCSV.csv");
    }

    @PostMapping("visit")
    public Response<VisitorResponse> visitSystem() {
        return visitorHandler.visitSystem();
    }

    @PostMapping("leave")
    public Response<VisitorResponse> leaveSystem(@RequestParam("visitorId") Integer visitorId) {
        return visitorHandler.leaveSystem(visitorId);
    }

    @PostMapping("register")
    public Response<MemberResponse> register(@RequestParam("visitorId") Integer visitorId,
                                             @RequestParam("username") String username,
                                             @RequestParam("password") String password,
                                             @RequestParam("email") String email) {
        return visitorHandler.register(visitorId, username, password, email);
    }

    @PostMapping("login")
    public Response<VisitorResponse> login(@RequestParam("visitorId") Integer visitorId, @RequestParam("username") String username, @RequestParam("password") String password) {
        return visitorHandler.login(visitorId, username, password, webSocketController);
    }

    @PostMapping("logout")
    public Response<VisitorResponse> logout(@RequestParam("visitorId") Integer visitorId) {
        return visitorHandler.logout(visitorId);
    }

    @PostMapping("addProductToShoppingCart")
    public Response<ShoppingCartResponse> addProductToShoppingCart(@RequestParam("visitorId") Integer visitorId,
                                                                   @RequestParam("shopId") Integer shopId,
                                                                   @RequestParam("productId") Integer productId,
                                                                   @RequestParam("quantity") Integer quantity) {
        return marketHandler.addProductToShoppingCart(visitorId, shopId, productId, quantity);
    }

    @PostMapping("removeProductFromShoppingCart")
    public Response<ShoppingCartResponse> removeProductFromShoppingCart(@RequestParam("visitorId") Integer visitorId,
                                                                        @RequestParam("shopId") Integer shopId,
                                                                        @RequestParam("productId") Integer productId) {
        return marketHandler.removeProductFromShoppingCart(visitorId, shopId, productId);
    }

    @PostMapping("purchaseShoppingCart")
    public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(@RequestParam("visitorId") Integer visitorId,
                                                                        @RequestParam("creditCardNumber") String creditCardNumber,
                                                                        @RequestParam("date") String date,
                                                                        @RequestParam("cvs") String cvs,
                                                                        @RequestParam("country") String country,
                                                                        @RequestParam("city") String city,
                                                                        @RequestParam("street") String street,
                                                                        @RequestParam("zip") String zip) {
        return marketHandler.purchaseShoppingCart(visitorId, creditCardNumber, date, cvs, country, city, street, zip);
    }

    @GetMapping("getShopPurchaseHistory")
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(@RequestParam("visitorId") Integer visitorId,
                                                                          @RequestParam("shopId") Integer shopId) {
        return marketHandler.getShopPurchaseHistory(visitorId, shopId);
    }

    @GetMapping("getShopInfo")
    public Response<ShopResponse> getShopInfo(@RequestParam("visitorId") Integer visitorId,
                                              @RequestParam("shopId") Integer shopId) {
        return marketHandler.getShopInfo(visitorId, shopId);
    }

    @GetMapping("searchProductByName")
    public Response<List<ProductResponse>> searchProductByName(@RequestParam("visitorId") Integer visitorId,
                                                               @RequestParam("productName") String productName) {
        return marketHandler.searchProductByName(visitorId, productName);
    }

    @GetMapping("searchProductByCategory")
    public Response<List<ProductResponse>> searchProductByCategory(@RequestParam("visitorId") Integer visitorId,
                                                                   @RequestParam("category") String category) {
        return marketHandler.searchProductByCategory(visitorId, category);
    }

    @GetMapping("searchProductByKeyWord")
    public Response<List<ProductResponse>> searchProductByKeyWord(@RequestParam("visitorId") Integer visitorId,
                                                                  @RequestParam("keyWord") String keyWord) {
        return marketHandler.searchProductByKeyWord(visitorId, keyWord);
    }

    @PostMapping("openShop")
    public Response<ShopResponse> openShop(@RequestParam("visitorId") Integer visitorId,
                                           @RequestParam("memberPhone") String memberPhone,
                                           @RequestParam("creditCard") String creditCard,
                                           @RequestParam("shopName") String shopName,
                                           @RequestParam("shopDescription") String shopDescription,
                                           @RequestParam("shopLocation") String shopLocation) {
        return marketHandler.openShop(visitorId, memberPhone, creditCard, shopName, shopDescription, shopLocation);
    }

    @PostMapping("closeShop")
    public Response<ShopResponse> closeShop(@RequestParam("visitorId") Integer visitorIdFounder,
                                            @RequestParam("shopId") Integer shopId) {
        return marketHandler.closeShop(visitorIdFounder, shopId);
    }

    @PostMapping("reOpenShop")
    public Response<ShopResponse> reOpenShop(@RequestParam("visitorId") Integer visitorIdFounder,
                                             @RequestParam("shopId") Integer shopId) {
        return marketHandler.reOpenShop(visitorIdFounder, shopId);
    }

    @PostMapping("assignShopOwner")
    public Response<RoleResponse> assignShopOwner(@RequestParam("visitorId") Integer visitorId,
                                                  @RequestParam("usernameToAssign") String usernameToAssign,
                                                  @RequestParam("shopId") Integer shopId) {
        return marketHandler.assignShopOwner(visitorId, usernameToAssign, shopId);
    }

    @PostMapping("assignShopManager")
    public Response<RoleResponse> assignShopManager(@RequestParam("visitorId") Integer visitorId,
                                                    @RequestParam("usernameToAssign") String usernameToAssign,
                                                    @RequestParam("shopId") Integer shopId) {
        return marketHandler.assignShopManager(visitorId, usernameToAssign, shopId);
    }

    @PostMapping("addProduct")
    public Response<ProductResponse> addProduct(@RequestParam("visitorId") Integer visitorId,
                                                @RequestParam("shopId") Integer shopId,
                                                @RequestParam("productName") String productName,
                                                @RequestParam("quantity") Integer quantity,
                                                @RequestParam("price") Double price,
                                                @RequestParam("description") String description,
                                                @RequestParam("category") String category) {
        return marketHandler.addProduct(visitorId, shopId, productName, quantity, price, description, category);
    }

    @PostMapping("removeProduct")
    public Response<ProductResponse> removeProduct(@RequestParam("visitorId") Integer visitorId,
                                                   @RequestParam("shopId") Integer shopId,
                                                   @RequestParam("productId") Integer productId) {
        return marketHandler.removeProduct(visitorId, shopId, productId);
    }

    @PostMapping("addProductAmount")
    public Response<ProductResponse> addProductAmount(@RequestParam("visitorId") Integer visitorIdAssign,
                                                      @RequestParam("shopId") Integer shopId,
                                                      @RequestParam("productId") Integer productId,
                                                      @RequestParam("amount") Integer amount) {
        return marketHandler.addProductAmount(visitorIdAssign, shopId, productId, amount);
    }

    @PostMapping("reduceProductAmount")
    public Response<ProductResponse> reduceProductAmount(@RequestParam("visitorId") Integer visitorIdAssign,
                                                         @RequestParam("shopId") Integer shopId,
                                                         @RequestParam("productId") Integer productId,
                                                         @RequestParam("amount") Integer amount) {
        return marketHandler.reduceProductAmount(visitorIdAssign, shopId, productId, amount);
    }

    @PostMapping("changeProductName")
    public Response<ProductResponse> changeProductName(@RequestParam("visitorId") Integer visitorIdAssign,
                                                       @RequestParam("shopId") Integer shopId,
                                                       @RequestParam("productId") Integer productId,
                                                       @RequestParam("newName") String newName) {
        return marketHandler.changeProductName(visitorIdAssign, shopId, productId, newName);
    }

    @PostMapping("changeProductDescription")
    public Response<ProductResponse> changeProductDescription(@RequestParam("visitorId") Integer visitorIdAssign,
                                                              @RequestParam("shopId") Integer shopId,
                                                              @RequestParam("productId") Integer productId,
                                                              @RequestParam("newDescription") String newDescription) {
        return marketHandler.changeProductDescription(visitorIdAssign, shopId, productId, newDescription);
    }

    @PostMapping("updateProductPrice")
    public Response<ProductResponse> updateProductPrice(@RequestParam("visitorId") Integer visitorIdAssign,
                                                        @RequestParam("shopId") Integer shopId,
                                                        @RequestParam("productId") Integer productId,
                                                        @RequestParam("price") Double price) {
        return marketHandler.updateProductPrice(visitorIdAssign, shopId, productId, price);
    }

    @GetMapping("getShopRoleInfo")
    public Response<List<RoleResponse>> getShopRoleInfo(@RequestParam("visitorId") Integer visitorId,
                                                        @RequestParam("shopId") Integer shopId) {
        return marketHandler.getShopRoleInfo(visitorId, shopId);
    }

    @GetMapping("getPermissionsOfMember")
    public Response<RoleResponse> getPermissionsOfMember(@RequestParam("visitorId") Integer visitorId,
                                                         @RequestParam("memberUserName") String memberUserName,
                                                         @RequestParam("shopId") Integer shopId) {
        return marketHandler.getPermissionsOfMember(visitorId, memberUserName, shopId);
    }

    @PostMapping("removePermission")
    public Response<ShopManagerResponse> removePermission(@RequestParam("visitorId") Integer visitorId,
                                                          @RequestParam("shopId") Integer shopId,
                                                          @RequestParam("memberUserName") String memberUserName,
                                                          @RequestParam("permission") Integer permission) {
        return marketHandler.removePermission(visitorId, shopId, memberUserName, permission);
    }

    @PostMapping("setPermission")
    public Response<ShopManagerResponse> setPermission(@RequestParam("visitorId") Integer visitorId,
                                                       @RequestParam("shopId") Integer shopId,
                                                       @RequestParam("memberUserName") String memberUserName,
                                                       @RequestParam("permission") Integer permission) {
        return marketHandler.setPermission(visitorId, shopId, memberUserName, permission);
    }

    @PostMapping("addProductDiscount")
    public Response<SingleProductDiscountResponse> addProductDiscount(@RequestParam("visitorId") Integer visitorId,
                                                                      @RequestParam("shopId") Integer shopId,
                                                                      @RequestParam("percentage") Double percentage,
                                                                      @RequestParam("expireYear") Integer expireYear,
                                                                      @RequestParam("expireMonth") Integer expireMonth,
                                                                      @RequestParam("expireDay") Integer expireDay,
                                                                      @RequestParam("productId") Integer productId) {
        return marketHandler.addProductDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay, productId);
    }

    @PostMapping("addCategoryDiscount")
    public Response<CategoryDiscountResponse> addCategoryDiscount(@RequestParam("visitorId") Integer visitorId,
                                                                  @RequestParam("shopId") Integer shopId,
                                                                  @RequestParam("percentage") Double percentage,
                                                                  @RequestParam("expireYear") Integer expireYear,
                                                                  @RequestParam("expireMonth") Integer expireMonth,
                                                                  @RequestParam("expireDay") Integer expireDay,
                                                                  @RequestParam("category") String category) {
        return marketHandler.addCategoryDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay, category);
    }

    @PostMapping("addTotalShopDiscount")
    public Response<TotalShopDiscountResponse> addTotalShopDiscount(@RequestParam("visitorId") Integer visitorId,
                                                                    @RequestParam("shopId") Integer shopId,
                                                                    @RequestParam("percentage") Double percentage,
                                                                    @RequestParam("expireYear") Integer expireYear,
                                                                    @RequestParam("expireMonth") Integer expireMonth,
                                                                    @RequestParam("expireDay") Integer expireDay) {
        return marketHandler.addTotalShopDiscount(visitorId, shopId, percentage, expireYear, expireMonth, expireDay);
    }

    @PostMapping("addXorDiscount")
    public Response<XorDiscountResponse> addXorDiscount(@RequestParam("visitorId") Integer visitorId,
                                                        @RequestParam("shopId") Integer shopId,
                                                        @RequestParam("discountAid") Integer discountAId,
                                                        @RequestParam("discountBid") Integer discountBId,
                                                        @RequestParam("expireYear") Integer expireYear,
                                                        @RequestParam("expireMonth") Integer expireMonth,
                                                        @RequestParam("expireDay") Integer expireDay) {
        return marketHandler.addXorDiscount(visitorId, shopId, discountAId, discountBId, expireYear, expireMonth, expireDay);
    }

    @PostMapping("addPQCondition")
    public Response<DiscountResponse> addPQCondition(@RequestParam("visitorId") Integer visitorId,
                                                     @RequestParam("shopId") Integer shopId,
                                                     @RequestParam("type") String type,
                                                     @RequestParam("discountId") Integer discountId,
                                                     @RequestParam("productId") Integer productId,
                                                     @RequestParam("minProductQuantity") Integer minProductQuantity) {
        return marketHandler.addPQCondition(visitorId, shopId, parseType(type), discountId, productId, minProductQuantity);
    }

    private CONDITION_COMPOSE_TYPE parseType(String type){
        CONDITION_COMPOSE_TYPE t = switch (type) {
            case "and" -> CONDITION_COMPOSE_TYPE.AND;
            case "or" -> CONDITION_COMPOSE_TYPE.OR;
            case "if" -> CONDITION_COMPOSE_TYPE.IF_THEN;
            case "reset" -> CONDITION_COMPOSE_TYPE.RESET;
            default -> null;
        };
        return t;
    }

    @PostMapping("addTBPCondition")
    public Response<DiscountResponse> addTBPCondition(@RequestParam("visitorId") Integer visitorId,
                                                      @RequestParam("shopId") Integer shopId,
                                                      @RequestParam("type") String type,
                                                      @RequestParam("discountId") Integer discountId,
                                                      @RequestParam("minBasketPrice") Double minBasketPrice) {
        return marketHandler.addTBPCondition(visitorId, shopId, parseType(type), discountId, minBasketPrice);
    }

    @PostMapping("addAtMostFromProductPolicy")
    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(@RequestParam("visitorId") Integer visitorId,
                                                                       @RequestParam("shopId") Integer shopId,
                                                                       @RequestParam("productId") Integer productId,
                                                                       @RequestParam("maxQuantity") Integer maxQuantity) {
        return marketHandler.addAtMostFromProductPolicy(visitorId, shopId, productId, maxQuantity);
    }

    @PostMapping("addAtLeastFromProductPolicy")
    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(@RequestParam("visitorId") Integer visitorId,
                                                                        @RequestParam("shopId") Integer shopId,
                                                                        @RequestParam("productId") Integer productId,
                                                                        @RequestParam("minQuantity") Integer minQuantity) {
        return marketHandler.addAtLeastFromProductPolicy(visitorId, shopId, productId, minQuantity);
    }

    @PostMapping("composePurchasePolicies")
    public Response<PurchasePolicyResponse> composePurchasePolicies(@RequestParam("visitorId") Integer visitorId,
                                                                    @RequestParam("shopId") Integer shopId,
                                                                    @RequestParam("type") String type,
                                                                    @RequestParam("policyId1") Integer policyId1,
                                                                    @RequestParam("policyId2") Integer policyId2) {
        return marketHandler.composePurchasePolicies(visitorId, shopId, parseType(type), policyId1, policyId2);
    }

    @GetMapping("getMemberBids")
    public Response<List<BidResponse>> getVisitorBids(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllMemberBid(visitorId);
    }

    @GetMapping("getShopBids")
    public Response<List<BidResponse>> getShopBids(@RequestParam("visitorId") Integer visitorId,
                                                   @RequestParam("shopId") Integer shopId){
        return marketHandler.getAllShopBid(visitorId, shopId);
    }

    @PostMapping("postBidProduct")
    public Response<BidResponse> bidProduct(@RequestParam("visitorId") Integer visitorId,
                                            @RequestParam("shopId") Integer shopId,
                                            @RequestParam("productId") Integer productId,
                                            @RequestParam("quantity") Integer quantity,
                                            @RequestParam("price") Double price) {
        return marketHandler.bidProduct(visitorId, shopId, productId, quantity, price);
    }

    @PostMapping("approveBid")
    public Response<BidResponse> approveBid(@RequestParam("visitorId") Integer visitorId,
                                            @RequestParam("shopId") Integer shopId,
                                            @RequestParam("bidId") Integer bidId) {
        return marketHandler.approveBid(visitorId, shopId, bidId);
    }

    @PostMapping("incrementBid")
    public Response<BidResponse> incrementBidPrice(@RequestParam("visitorId") Integer visitorId,
                                                   @RequestParam("shopId") Integer shopId,
                                                   @RequestParam("bidId") Integer bidId,
                                                   @RequestParam("price") Double newPrice) {
        return marketHandler.incrementBidPrice(visitorId, shopId, bidId, newPrice);
    }

    @PostMapping("purchaseBid")
    public Response<PurchaseHistoryResponse> purchaseBid(@RequestParam("visitorId") Integer visitorId,
                                                         @RequestParam("bidId") Integer bidId,
                                                         @RequestParam("creditCardNumber") String creditCardNumber,
                                                         @RequestParam("date") String date,
                                                         @RequestParam("cvs") String cvs,
                                                         @RequestParam("country") String country,
                                                         @RequestParam("city") String city,
                                                         @RequestParam("street") String street,
                                                         @RequestParam("zip") String zip){
        return marketHandler.purchaseBid(visitorId, bidId, creditCardNumber, date, cvs, country, city, street,zip);
    }
    // ****************************** UTILS for Client *********************************
    @GetMapping("getVisitorCart")
    public Response<ShoppingCartResponse> getVisitorCart(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getVisitorCart(visitorId);
    }

    @GetMapping("getAllOpenShops")
    public Response<List<ShopResponse>> getShops(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllOpenShops(visitorId);
    }
    @GetMapping("getMyShops")
    public Response<List<ShopResponse>> getMyShops(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getMyShops(visitorId);
    }
    @PostMapping("removeShopOwner")
    public Response<List<ShopOwnerResponse>> removeShopOwner(@RequestParam("visitorId") Integer visitorId,
                                                             @RequestParam("memberUserName") String memberUserName,
                                                             @RequestParam("shopId") Integer shopId) {
        return marketHandler.removeShopOwner(visitorId, memberUserName, shopId);
    }

    @GetMapping("getDiscountPolicies")
    public Response<List<DiscountResponse>> getDiscountsPolicies (@RequestParam("visitorId") Integer visitorId, @RequestParam("shopId") Integer shopId){
        return marketHandler.getDiscountsPolicies(visitorId, shopId);

    }

    @GetMapping("getPurchasePolicies")
    public Response<List<PurchasePolicyResponse>> getPurchasesPolicies (@RequestParam("visitorId") Integer visitorId, @RequestParam("shopId") Integer shopId){
        return marketHandler.getPurchasesPolicies(visitorId,shopId);
    }

    @GetMapping("getIsAdmin")
    public Response<Boolean> getIsAdmin(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.isAdmin(visitorId);
    }

    @PostMapping("initAssignShopOwner")
    public Response<AssignAgreementResponse> initAssignShopOwner(@RequestParam("visitorId") Integer visitorId,
                                                                 @RequestParam("userNameToAssign") String usernameToAssign,
                                                                 @RequestParam("shopId") Integer shopId) {
        return marketHandler.initAssignShopOwner(visitorId, usernameToAssign, shopId);
    }

    @PostMapping("approveAssignShopOwner")
    public Response<AssignAgreementResponse> approveAssignAgreement(@RequestParam("visitorId") Integer visitorId,
                                                                    @RequestParam("userNameToAssign") String usernameToAssign,
                                                                    @RequestParam("shopId") Integer shopId) {
        return marketHandler.approveAssignAgreement(visitorId, usernameToAssign, shopId);
    }

    @GetMapping("getAllShopAssignments")
    public Response<List<AssignAgreementResponse>> getAllShopAssignAgreements(@RequestParam("visitorId") Integer visitorId,
                                                                              @RequestParam("shopId") Integer shopId) {
        return marketHandler.getAllShopAssignAgreements(visitorId, shopId);
    }

    // **************************** Admin functions *************************
    @PostMapping("cancelMemberShip")
    public Response<MemberResponse> cancelMemberShip(@RequestParam("visitorId") Integer visitorId,
                                                     @RequestParam("memberUserName") String memberUserName) {
        return marketHandler.cancelMemberShip(visitorId, memberUserName);
    }

    @GetMapping("getAllMembers")
    public Response<List<MemberResponse>> getAllMembers (@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllMembers(visitorId);
    }

    @GetMapping("getAllShopOwners")
    public Response<List<ShopOwnerResponse>> getAllShopOwners(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllOwners(visitorId);
    }

    @GetMapping("getAllVisitors")
    public Response<List<VisitorResponse>> getAllVisitors (@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllVisitors(visitorId);
    }

//    @GetMapping("getMembersStatus")
//    public Response<Collection<MemberResponse>> getMembersStatus(@RequestParam("visitorId") Integer visitorId){
//        return null;
//    }

    @GetMapping("getNumOfVisitors")
    public Response<List<VisitorResponse>> getNumOfVisitors (@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllVisitors(visitorId);
    }

    @GetMapping("getMembersStatus")
    public Response<List<MemberResponse>> getMembersStatus(@RequestParam("visitorId") Integer visitorId){
        return marketHandler.getAllMembers(visitorId);
    }

    @GetMapping("getDailyData")
    public Response<List<DailySystemDataResponse>> getSystemData(@RequestParam("visitorId") Integer visitorId,
                                                                 @RequestParam("startDate") String startDate,
                                                                 @RequestParam("endDate") String endDate){
        return systemHandler.getSystemData(visitorId, startDate, endDate);
    }

}
