package AcceptanceTests;

import AcceptanceTests.Bridge.*;
import Domain.Market.Notifications.IMemberObserver;
import Domain.Market.Responses.*;
import Service.MarketHandler.MarketHandler;
import Service.SystemHandler.SystemHandler;
import Service.VisitorHandler.VisitorHandler;

import java.util.HashMap;
import java.util.List;

//TODO: mocking, sychornized tests
class ProjectTests {
    static TestsBridge testsBridge;

    //visitor objects
    int notUserId;
    Response<VisitorResponse> visitResponse1;
    Response<VisitorResponse> visitLoggedResponse;
    Response<VisitorResponse> visitResponseAdmin;
    Response<VisitorResponse> loginUserResponse;
    VisitorResponse visitor1;

    MemberObserverForTests visitorLoggedMemberObserver;
    VisitorResponse visitorLogged;
    VisitorResponse visitorAdmin;
    String adminUser= "HannaLaslo";
    String adminPass= "TheQueen";
    HashMap<String, String> members = new HashMap<>();
    String userNameChanging;
    Integer signedUserCount=0;
    String password;
    String email;
    Integer signedEmailCount=0;

    //shopObjects
    Response<ShopResponse> openNewShopResponse;
    ShopResponse shopResponse;
    Response<ProductResponse> newProductResponse1;
    ProductResponse productResponse1;
    String shopNameExist= "ShopExist";
    String shopDescriptionValid= "SomeDesctription";
    String shopName= "MyShop";
    static Integer shopCounter=0;
    String memberPhone= "0525332201";
    String creditCard= "123456789101112131415";
    String shopLocation = "mazePinatMafoTLV";
    String productName1= "Blue shoes";
    int quantity= 3;
    int quantity1 = 1;
    double price= 7.90;
    String description= "looks great in the dark";
    String category1= "Clothes";
    public ProjectTests() {
        testsBridge = new Real(new VisitorHandler(),new SystemHandler(),new MarketHandler());
        this.notUserId = -1000;
        this.visitLoggedResponse=visitSystem();
        this.visitorLogged=visitLoggedResponse.getValue();
        this.userNameChanging = "YeshLi";
        this.password = "MaleKesef1";
        this.email= "email@gmail.com";
        members.put("UserLogged","Password1");
        members.put("User2","Password2");
        members.put("User3","Password3");
        members.put("User4","Password4");
        RegisterUsers();
    }

    private void RegisterUsers()
    {
        visitResponse1=visitSystem();
        visitor1= visitResponse1.getValue();
        members.forEach((name,pass) -> register(visitor1.getId(),name,pass,name.concat("@gmail.com")));
        leaveSystem(visitResponse1.getValue().getId());
        visitor1=null;
    }
    String GenerateNewShopName()
    {
        shopCounter++;
        String newName= shopName.concat(shopCounter.toString());
        return newName;
    }

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

    public Response<List<PurchaseHistoryResponse>> purchaseShoppingCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street) {
        return testsBridge.purchaseShoppingCart(visitorID, creditCardNumber, date, cvs,country, city, street);
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

}
