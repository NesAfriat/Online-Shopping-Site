package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import Domain.Market.DiscountPolicies.*;
import Domain.Market.Notifications.IMemberObserver;
import Domain.Market.PurchasePolicies.PurchasePolicy;
import Domain.Market.Responses.*;

import java.util.*;
import java.util.stream.Collectors;

public class MarketRepresentative {
    private static final String DEFAULT_ADMIN_USERNAME = "HannaLaslo";
    private static final String DEFAULT_ADMIN_PASSWORD = "TheQueen";
    private final VisitorController visitorController;
    private final ShopController shopController;
    private final PurchaseCashier purchaseCashier;
    private final RoleController roleController;
    private List<Member> admins;

    private MarketRepresentative() {
        visitorController = VisitorController.getInstance();
        shopController = ShopController.getInstance();
        purchaseCashier = PurchaseCashier.getInstance();
        roleController = RoleController.getInstance();
        admins = loadAdmins();
    }

    public Response<List<MemberResponse>> getAllMembers(int visitorId) {
        try {
            Member m = visitorController.getVisitorLoggedIn(visitorId);
            if(!isAdmin(m)){
                return new Response<>("ur not admin!");
            }
            List<Member> members = visitorController.getMembersList();
            List<MemberResponse> outPut = new LinkedList<>();
            for (Member mi: members) {
                outPut.add(new MemberResponse(mi));
            }
            return new Response<>(outPut);
        } catch (InnerLogicException e) {
            return new Response<>("error: " + e.getMessage());
        }catch (Exception e){
            return new Response<>("fatal error: " + e.getMessage());
        }
    }

    private static class MarketRepresentativeHolder {
        private static final MarketRepresentative instance = new MarketRepresentative();
    }

    public static MarketRepresentative getInstance() {
        return MarketRepresentative.MarketRepresentativeHolder.instance;
    }


    private List<Member> loadAdmins() {
        Member admin = null;
        try {
            admin = visitorController.getMember(DEFAULT_ADMIN_USERNAME);
        }
        catch (InnerLogicException ignored){
            try{
                Visitor visitor = visitorController.visitSystem();
                admin = visitorController.register(visitor.getId(), DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
            }
            catch (InnerLogicException e){
                Logger.getInstance().logError("init system failed " + "\nerror message: " + e.getMessage() + "\n\n");
//                throw new InnerLogicException("failed to init system. can't load market's admins." + e.getMessage());
            }
        }
        return Collections.singletonList(admin);
    }

    public Response<ShoppingCartResponse> getShoppingCart(int visitorId) {
        try {
            ShoppingCart shoppingCart = visitorController.getShoppingCart(visitorId);
            Logger.getInstance().logEvent("get shopping success: visitor " + visitorId + "\n\n");
            return new Response<>(new ShoppingCartResponse(shoppingCart));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get shopping cart failed: visitor " + visitorId + " didn't got shopping cart " +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("get shopping cart error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("get shop histories failed: visitor " + visitorId + " didn't got shop " +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("get shopping cart shop fatal error: " + e.getMessage());
        }
    }

    public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity) {
        try {
            Product prod = shopController.verifyProductExist(shopID, productID);
            ShoppingCart shoppingCart = visitorController.addProductToShoppingCart(visitorID, shopID, prod, quantity);
            Logger.getInstance().logEvent("a new product: "+ productID+" was added to visitor: "+visitorID+" from shop: "+ shopID+ "with quantity: "+ quantity + "\n\n");
            return new Response<>(new ShoppingCartResponse(shoppingCart));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("add Product To ShoppingCart failed for visitor: "+visitorID+"in shop: "+shopID+" with product: "+productID + " and quantity: "+ quantity +
                    "\nerror message: "+e.getMessage() + "\n\n");
            return new Response<>("add product to shopping cart error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("add Product To ShoppingCart failed for visitor: "+visitorID+"in shop: "+shopID+" with product: "+productID + " and quantity: "+ quantity +
                    "\nerror message: "+e.getMessage() + "\n\n");
            return new Response<>("get product to shopping cart shop fatal error: " + e.getMessage());
        }
    }

    public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID) {
        try {
            ShoppingCart shoppingCart = visitorController.removeProductFromShoppingCart(visitorID, shopID, productID);
            Logger.getInstance().logEvent("a new product: "+ productID+" was remove to visitor: "+visitorID+" from shop: "+ shopID + "\n\n");
            return new Response<>(new ShoppingCartResponse(shoppingCart));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("remove Product To ShoppingCart failed for visitor: "+visitorID+"in shop: "+shopID+" with product: "+productID+
                    "\nerror message: "+e.getMessage() + "\n\n");
            return new Response<>("remove product from shopping cart error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("remove Product To ShoppingCart failed for visitor: "+visitorID+"in shop: "+shopID+" with product: "+productID +
                    "\nerror message: "+e.getMessage() + "\n\n");
            return new Response<>("remove product from shopping cart shop fatal error: " + e.getMessage());
        }
    }

    public Response<List<PurchaseHistoryResponse>> purchaseCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street) {
        try {
            List<PurchaseHistory> purchaseHistories = purchaseCashier.purchaseCart(visitorID, creditCardNumber, date, cvs, country, city, street);
            List<PurchaseHistoryResponse> output = new LinkedList<>();
            for(PurchaseHistory p: purchaseHistories) {
                PurchaseHistoryResponse pr = new PurchaseHistoryResponse(p);
                output.add(pr);
            }
            Logger.getInstance().logEvent("visitor: "+visitorID+" has purchased his cart" + "\n\n");
            return new Response<>(output);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: "+visitorID+" has failed to purchased his cart\nerror message: "+e.getMessage() + "\n\n");
            return new Response<>("buying shopping cart error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorID+" has failed to purchased his cart\nerror message: "+e.getMessage() + "\n\n");
            return new Response<>("buying shopping cart fatal error: " + e.getMessage());
        }
    }

    public Response<ShopResponse> closeShop(int visitId, int shopId) {
        try {
            Shop shop = shopController.closeShop(visitId, shopId);
            Logger.getInstance().logEvent("a founder:"+visitId+" has closed his shop: "+shopId + "\n\n");
            return new Response<>(new ShopResponse(shop));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("a visitor:"+visitId+" has failed to closed his shop: "+shopId + "\n\n");
            return new Response<>("error in closeShop: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("a visitor:"+visitId+" has failed to closed his shop: "+shopId + "\n\n");
            return new Response<>("fatal error in closeShop: " + e.getMessage());
        }

    }

    public Response<ShopResponse> reOpenShop(int visitId, int shopId) {
        try {
            Shop shop = shopController.reOpenShop(visitId, shopId);
            Logger.getInstance().logEvent("a founder:"+visitId+" has re-open his shop: "+shopId + "\n\n");
            return new Response<>(new ShopResponse(shop));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("a visitor:"+visitId+" has failed to re-open his shop: "+shopId + "\n\n");
            return new Response<>("error in reOpenShop: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("a visitor:"+visitId+" has failed to re-open his shop: "+shopId + "\n\n");
            return new Response<>("fatal error in reOpenShop: " + e.getMessage());
        }
    }

    //RETURN ONLY RESPONSE TO SERVICE
    //SECURITY CONSTRAINTS
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId) {
        try {
            //verify visitor is the shop owner or admin
            Member m = visitorController.getVisitorLoggedIn(visitorId);
            String memberUsername = m.getUsername();
            if (!isAdmin(m))
                roleController.verifyShopOwner(shopId, memberUsername);
            List<PurchaseHistory> purchaseHistories = purchaseCashier.getShopHistories(shopId);
            List<PurchaseHistoryResponse> output = purchaseHistories.stream().map(PurchaseHistoryResponse::new).collect(Collectors.toList());
            Logger.getInstance().logEvent("get shop histories success: visitor " + visitorId + " got shop " + shopId + "\n\n");
            return new Response<>(output);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get shop histories failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("getShopPurchaseHistory error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("get shop histories failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("getShopPurchaseHistory fatal error: " + e.getMessage());
        }
    }





    public Response<ShopResponse> getShop(int visitorId, int shopId) {
        try {
            Shop shop = shopController.getShop(visitorId, shopId);
            Logger.getInstance().logEvent("get shop success: visitor " + visitorId + " got shop " + shopId + "\n\n");
            return new Response<>(new ShopResponse(shop));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get shop failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("get shop error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("get shop failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("get shop fatal error: " + e.getMessage());
        }

    }

    public Response<VisitorResponse> visitSystem() {
        try {
            Visitor visitor = visitorController.visitSystem();
            Logger.getInstance().logEvent("visit system success: new visitor with id " + visitor.getId() + " entered the system" + "\n\n");
            return new Response<>(new VisitorResponse(visitor));
        } catch (Exception e) {
            Logger.getInstance().logError("visit system failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("visit system fatal error: " + e.getMessage());
        }
    }

    public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver) {
        try {
            Visitor visitor = visitorController.login(visitorId, username, password, memberObserver);
            Logger.getInstance().logEvent("login success: visitor " + visitorId + " logged in to member " + username + "\n\n");
            return new Response<>(new VisitorResponse(visitor));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("login failed: visitor " + visitorId + " failed to login to member " + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("login error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("login failed: visitor " + visitorId + " failed to login to member " + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("login fatal error: " + e.getMessage());
        }
    }

    public Response<VisitorResponse> logout(int visitorId) {
        try {
            Visitor loggedOut = visitorController.logout(visitorId);
            Logger.getInstance().logEvent("logout success: visitor " + visitorId + " logged out" + "\n\n");
            return new Response<>(new VisitorResponse(loggedOut));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("logout failed: visitor " + visitorId + " failed to logout" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("logout error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("logout failed: visitor " + visitorId + " failed to logout" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("logout fatal error: " + e.getMessage());
        }
    }

    public Response<VisitorResponse> leaveSystem(int visitorId) {
        try {
            Visitor visitor = visitorController.leaveSystem(visitorId);
            Logger.getInstance().logEvent("leave system success: visitor " + visitorId + " leaved the system" + "\n\n");
            return new Response<>(new VisitorResponse(visitor));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("leave system failed: visitor " + visitorId + " failed to leave the system" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("leave system error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("leave system failed: visitor " + visitorId + " failed to leave the system" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("leave system fatal error: " + e.getMessage());
        }
    }

    public Response<MemberResponse> register(int visitorId, String username, String password) {
        try {
            Member member = visitorController.register(visitorId, username, password);
            Logger.getInstance().logEvent("register success: visitor " + visitorId + " registered new member with username " + username + "\n\n");
            return new Response<>(new MemberResponse(member));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("register failed: visitor " + visitorId + " failed to register new member with username "
                    + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("register error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("register failed: visitor " + visitorId + " failed to register new member with username "
                    + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("register fatal error: " + e.getMessage());
        }
    }

    public Response<ShopResponse> openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) {
        try {
            Shop shop = shopController.openShop(visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);
            Logger.getInstance().logEvent("open shop success: visitor " + visitorID + " open new shop with id " + shop.getShopId() + "\n\n");
            return new Response<>(new ShopResponse(shop));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("open shop failed: visitor " + visitorID + " failed to open new shop" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("openShop error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("open shop failed: visitor " + visitorID + " failed to open new shop" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("openShop fatal error: " + e.getMessage());
        }
    }

    /**
     * @param visitorId
     * @param shopId    the shop to remove the items from
     * @param productId the item to remove
     * @return the item that was removed
     */
    public Response<ProductResponse> removeProductFromShop(int visitorId, int shopId, int productId) {
        try {
            Product product = shopController.removeProductFromShop(visitorId, shopId, productId);
            Logger.getInstance().logEvent("remove product from shop success: visitor " + visitorId + " removed product "
                    + productId + " from shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("remove product from shop failed: visitor " + visitorId + " failed to remove product "
                    + productId + " from shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("removeProduct error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("remove product from shop failed: visitor " + visitorId + " failed to remove product "
                    + productId + " from shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("removeProduct fatal error: " + e.getMessage());
        }
    }

    /**
     * y
     *
     * @param visitorId
     * @param shopId      id must be positive
     * @param productName name of the product
     * @param price       the price must be even
     * @param description description of the item
     * @param quantity    the quantity in stock
     * @return the produce after it was added
     */
    public Response<ProductResponse> addProductToShop(int visitorId, int shopId, String productName, double price, String description, int quantity, String category) {
        try {
            roleController.verifyIsShopOwner(visitorId, shopId);
            Product product = shopController.addProductToShop(visitorId, shopId, productName, price, description, quantity, category);
            Logger.getInstance().logEvent("add product to shop success: visitor " + visitorId + " added product "
                    + product.getId() + " to shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("add product to shop failed: visitor " + visitorId + " failed to add product to shop "
                    + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("addProduct error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("add product to shop failed: visitor " + visitorId + " failed to add product to shop "
                    + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("addProduct fatal error: " + e.getMessage());
        }
    }

    public Response<ProductResponse> changeProductDescription(int visitorId, int shopId, int productId, String newDescription) {
        try {
            Product product = shopController.changeProductDescription(visitorId, shopId, productId, newDescription);
            Logger.getInstance().logEvent("change product description success: visitor " + visitorId + " changed product "
                    + product.getId() + " description at shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("change product description failed: visitor " + visitorId + " failed to change product "
                    + productId + " description at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("change product description error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("change product description failed: visitor " + visitorId + " failed to change product "
                    + productId + " description at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("change product description fatal error: " + e.getMessage());
        }
    }

    public Response<ProductResponse> changeProductName(int visitorId, int shopId, int productId, String newName) {
        try {
            Product product = shopController.changeProductName(visitorId, shopId, productId, newName);
            Logger.getInstance().logEvent("change product name success: visitor " + visitorId + " changed product "
                    + product.getId() + " name at shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("change product name failed: visitor " + visitorId + " failed to change product "
                    + productId + " name at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("change product Name error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("change product name failed: visitor " + visitorId + " failed to change product "
                    + productId + " name at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("change product Name fatal error: " + e.getMessage());
        }
    }

    public Response<ProductResponse> changeProductPrice(int visitorId, int shopId, int productId, double newPrice) {
        try {
            Product product = shopController.changeProductPrice(visitorId, shopId, productId, newPrice);
            Logger.getInstance().logEvent("change product price success: visitor " + visitorId + " changed product "
                    + product.getId() + " price at shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("change product price failed: visitor " + visitorId + " failed to change product "
                    + productId + " price at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("change product Price error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("change product price failed: visitor " + visitorId + " failed to change product "
                    + productId + " price at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("change product Price fatal error: " + e.getMessage());
        }
    }

    public Response<ProductResponse> addProductQuantity(int visitorId, int shopId, int productId, int toAdd) {
        try {
            Product product = shopController.addProductQuantity(visitorId, shopId, productId, toAdd);
            Logger.getInstance().logEvent("add product quantity success: visitor " + visitorId + " added product "
                    + product.getId() + " quantity at shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("add product quantity failed: visitor " + visitorId + " failed to add product "
                    + productId + " quantity at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add product quantity error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("add product quantity failed: visitor " + visitorId + " failed to add product "
                    + productId + " quantity at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add product quantity fatal error: " + e.getMessage());
        }
    }

    public Response<ProductResponse> reduceProductQuantity(int visitorId, int shopId, int productId, int toReduce) {
        try {
            Product product = shopController.reduceProductQuantity(visitorId, shopId, productId, toReduce);
            Logger.getInstance().logEvent("reduce product quantity success: visitor " + visitorId + " reduced product "
                    + product.getId() + " quantity at shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("reduce product quantity failed: visitor " + visitorId + " failed to reduce product "
                    + productId + " quantity at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("reduce product quantity error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("reduce product quantity failed: visitor " + visitorId + " failed to reduce product "
                    + productId + " quantity at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("reduce product quantity fatal error: " + e.getMessage());
        }
    }

    public Response<List<ProductResponse>> searchProductByName(int visitorId, String name) {
        try {
            HashMap<Shop, List<Product>> productsAndShop = shopController.searchProductByName(visitorId, name);
            return hashToProducts(productsAndShop);
        }
        catch (Exception e) {
            Logger.getInstance().logError("search product by name failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("searchProductByName fatal error: "+e.getMessage());
        }
    }

    public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord) {
        try {
            HashMap<Shop, List<Product>> productsAndShop = shopController.searchProductByKeyWord(visitorId, keyWord);
            return hashToProducts(productsAndShop);
        } catch (Exception e) {
            Logger.getInstance().logError("search product by key word failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("searchProductByKeyWord fatal error: "+e.getMessage());
        }
    }

    public Response<List<ProductResponse>> searchProductByKeyCategory(int visitorId, String category) {
        try {
            HashMap<Shop, List<Product>> productsAndShop = shopController.searchProductByCategory(visitorId, category);
            return hashToProducts(productsAndShop);
        } catch (Exception e) {
            Logger.getInstance().logError("search product by category failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("searchProductByKeyCategory fatal error: "+e.getMessage());
        }
    }

    //help functions
    private Response<List<ProductResponse>> hashToProducts(HashMap<Shop, List<Product>> productsAndShop) {
        List<ProductResponse> outPut = new ArrayList<>();
        for (Map.Entry<Shop, List<Product>> shopProducts : productsAndShop.entrySet()) {
            for (Product p : shopProducts.getValue()) {
                outPut.add(new ProductResponse(shopProducts.getKey().getShopId(), p));
            }
        }
        return new Response<>(outPut);
    }

    public Response<RoleResponse> assignShopOwner(int visitorId, String usernameToAssign, int shopID) {
        try {
            ShopOwner shopOwner = roleController.assignShopOwner(visitorId, usernameToAssign, shopID);
            Logger.getInstance().logEvent("assign shop owner success: visitor " + visitorId + " assigned member " + usernameToAssign +
                    " as owner in shop " + shopID + "\n\n");
            return new Response<>(new ShopOwnerResponse(usernameToAssign, shopOwner));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("assign shop owner error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("assign shop owner fatal error: " + e.getMessage());
        }

    }

    public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign, int shopID) {
        try {
            ShopManager shopManager = roleController.assignShopManager(visitorId, usernameToAssign, shopID);
            Logger.getInstance().logEvent("assign shop manager success: visitor " + visitorId + " assigned member " + usernameToAssign +
                    " as manager in shop " + shopID + "\n\n");
            return new Response<>(new ShopManagerResponse(usernameToAssign, shopManager));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("assign shop manager failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as manager in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("assign shop manager error: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("assign shop manager failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as manager in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("assign shop manager fatal error: " + e.getMessage());
        }
    }

    public Response<ShopManagerResponse> setPermission(int visitorId, int shopId, String memberUserName, int permission) {
        try {
            ShopManager shopManager = roleController.setManagerPermission(visitorId, shopId, permission, memberUserName);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has set permission: "+permission+" on member: "+ memberUserName+"in shop: "+shopId + "\n\n");
            return new Response<>(new ShopManagerResponse(memberUserName, shopManager ));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to set permission on user: "+memberUserName + "\n\n");
            return new Response<>("error in add product permission:" + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to set permission on user: "+memberUserName + "\n\n");
            return new Response<>("fatal error in add product permission" + e.getMessage());
        }
    }

    public Response<ShopManagerResponse> removePermission(int visitorId, int shopId, String memberUserName, int permission) {
        try {
            ShopManager shopManager = roleController.removeManagerPermission(visitorId, shopId, permission, memberUserName);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has remove permission: "+permission+
                    " from member: "+ memberUserName+"in shop: "+shopId + "\n\n");
            return new Response<>(new ShopManagerResponse(memberUserName, shopManager));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to remove permission on user: "+memberUserName + "\n\n");
            return new Response<>("error in remove product permission:" + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to remove permission on user: "+memberUserName + "\n\n");
            return new Response<>("fatal error in remove product permission" + e.getMessage());
        }
    }

    public Response<List<RoleResponse>> getShopManagementInfo(int visitorId, int shopId) {
        try {
            List<RoleResponse> output = new LinkedList<>();
            Set<Map.Entry<String, Role>> res = roleController.getShopManagementInfo(visitorId, shopId);
            for (Map.Entry<String, Role> r : res) {
                if (r.getValue() instanceof ShopManager) {
                    output.add(new ShopManagerResponse(r.getKey(), (ShopManager) r.getValue()));
                } else if (r.getValue() instanceof ShopOwner) {
                    output.add(new ShopOwnerResponse(r.getKey(), (ShopOwner) r.getValue()));
                } else {
                    throw new InnerLogicException("this is fatal in getOwners");
                }
            }
            return new Response<>(output);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to get management info in shop: "+shopId + "\n\n");
            return new Response<>("error in getShopManagement: " + e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to get management info in shop: "+shopId + "\n\n");
            return new Response<>("fatal error in getShopManagement: " + e.getMessage());
        }
    }

    public Response<RoleResponse> getPermissionsOfMember(int visitorId, String memberUserName, int shopId) {
        try {
            ShopManager shopManager = roleController.getPermissionsOfMember(visitorId, memberUserName, shopId);
            //logger
            return new Response<>(new ShopManagerResponse(memberUserName, shopManager));
        } catch (InnerLogicException e) {
            return new Response<>("error in getPermissionsOfMember: " + e.getMessage() + "\n\n");
        } catch (Exception e) {
            return new Response<>("fatal error in getPermissionsOfMember: " + e.getMessage());
        }
    }

    public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId){
        try{
            Date date = new GregorianCalendar(expireYear, expireMonth - 1, expireDay).getTime(); // months are zero based indexed
            SingleProductDiscount discount = shopController.addProductDiscount(visitorId, shopId, percentage, date, productId);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added product discount: "+ discount.getId()+" to shop: "+shopId + "\n\n");
            return new Response<>(new SingleProductDiscountResponse(shopId, discount));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add product discount to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add product discount failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add product discount to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add product discount" + e.getMessage() + "\n\n");
        }
    }

    public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category){
        try{
            Date date = new GregorianCalendar(expireYear, expireMonth - 1, expireDay).getTime(); // months are zero based indexed
            CategoryDiscount discount = shopController.addCategoryDiscount(visitorId, shopId, percentage, date, category);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added category discount: "+ discount.getId()+" to shop: "+shopId + "\n\n");
            return new Response<>(new CategoryDiscountResponse(shopId, discount));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add category discount to shop: "+shopId+
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add category discount failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add category discount to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add category discount" + e.getMessage() + "\n\n");
        }
    }

    public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay){
        try{
            Date date = new GregorianCalendar(expireYear, expireMonth - 1, expireDay).getTime(); // months are zero based indexed
            TotalShopDiscount discount = shopController.addTotalShopDiscount(visitorId, shopId, percentage, date);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added entire shop discount: "+ discount.getId()+" to shop: "+shopId + "\n\n");
            return new Response<>(new TotalShopDiscountResponse(shopId, discount));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add entire shop discount to shop: "+shopId+
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add entire shop discount failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add entire shop discount to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add entire shop discount" + e.getMessage() + "\n\n");
        }
    }

    public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay){
        try{
            XorDiscount discount = shopController.addXorDiscount(visitorId, shopId, discountAId, discountBId);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added xor discount: "+ discount.getId()+" to shop: "+shopId + "\n\n");
            return new Response<>(new XorDiscountResponse(shopId, discount));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add xor discount to shop: "+shopId+
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add xor discount failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add xor discount to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add xor discount" + e.getMessage() + "\n\n");
        }
    }

    public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity){
        try{
            Discount discount = shopController.addPQCondition(visitorId, shopId, type, discountId, productId, minProductQuantity);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added product quantity condition to shop" +
                    shopId + "\n\n");
            return new Response<>(DiscountResponse.buildDiscountResponse(shopId, discount));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add product quantity condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("add product quantity condition failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to add product quantity condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add product quantity condition" + e.getMessage());
        }
    }

    public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) {
        try{
            Discount discount = shopController.addTBPCondition(visitorId, shopId, type, discountId, minBasketPrice);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added total basket price condition to shop" +
                    shopId + "\n\n");
            return new Response<>(DiscountResponse.buildDiscountResponse(shopId, discount));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add total basket price condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("add product quantity condition failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add total basket price condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add product quantity condition" + e.getMessage());
        }
    }
    public Response<MemberResponse> cancelMembership(int visitorId, String userName) throws InnerLogicException {
        if(!isLoggedInAdmin(visitorId)){
            return new Response<>("u dont have permission to cancel a membership");
        }
        if(roleController.isRoleExists(userName)){
            return new Response<>(userName+" have role in shop");
        }
        try {
            return new Response<>(new MemberResponse(visitorController.unRegister(userName)));
        }catch (InnerLogicException e){
            return new Response<>(" cancel membership error: " + e.getMessage());
        }catch (Exception e){
            return new Response<>("fatal cancel membership error: "+ e.getMessage());
        }
    }
    private boolean isLoggedInAdmin(int id) throws InnerLogicException {
        Member m = visitorController.getVisitorLoggedIn(id);
        return admins.contains(m);
    }
    private boolean isAdmin(Member m) {
        return admins.contains(m);
    }

    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity){
        try{
            PurchasePolicy purchasePolicy = shopController.addAtMostFromProductPolicy(visitorId, shopId, productId, maxQuantity);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added purchase policy, at most: "+ maxQuantity+" from product: " + productId + " to shop: "+shopId + "\n\n");
            return new Response<>(new PurchasePolicyResponse(purchasePolicy));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add 'at most from product' policy to shop: "+shopId+
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add 'at most from product' policy failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add 'at most from product' policy to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add 'at most from product' policy" + e.getMessage() + "\n\n");
        }
    }

    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity){
        try{
            PurchasePolicy purchasePolicy = shopController.addAtLeastFromProductPolicy(visitorId, shopId, productId, minQuantity);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has added purchase policy, at least: "+ minQuantity+" from product: " + productId + " to shop: "+shopId + "\n\n");
            return new Response<>(new PurchasePolicyResponse(purchasePolicy));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to add 'at least from product' policy to shop: "+shopId+
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("add 'at least from product' policy failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to add 'at least from product' policy to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in add 'at least from product' policy" + e.getMessage() + "\n\n");
        }
    }

    public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2){
        try{
            PurchasePolicy composePurchasePolicy = shopController.composePurchasePolicies(visitorId, shopId, type, policyId1, policyId2);
            Logger.getInstance().logEvent("visitor: "+visitorId+" has composed purchase polices from: "+ policyId1 + " and " + policyId2 + " from type " + type +
                    "to shop: "+shopId + "\n\n");
            return new Response<>(new PurchasePolicyResponse(composePurchasePolicy));
        }
        catch (InnerLogicException e){
            Logger.getInstance().logEvent("visitor: "+visitorId+" has failed to composed purchase polices to shop: "+shopId+
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("composed purchase polices failed: " + e.getMessage());
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: "+visitorId+" has failed to composed purchase polices to shop: "+shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal error in composed purchase polices" + e.getMessage() + "\n\n");
        }
    }



    /***
     *
     * @param visitorId
     * @param userName
     * @param shopId
     * @return
     */
    public Response<List<ShopOwnerResponse>> removeShopOwner(int visitorId,String userName, int shopId) {
        try {
            HashMap<String,ShopOwner> shopOwnersRemoved = roleController.removeShopOwner(visitorId,userName,shopId);
            List<ShopOwnerResponse> res = new LinkedList<>();
            for (Map.Entry<String,ShopOwner> entry: shopOwnersRemoved.entrySet()){
                res.add(new ShopOwnerResponse(entry.getKey(),entry.getValue()));
            }
            return new Response<>(res);

        } catch (InnerLogicException e) {
            return new Response<>("error remove shop owner: "+ e.getMessage());
        }catch (Exception e){
            return new Response<>("fatal remove shop owner: "+ e.getMessage());
        }
    }
    public Response<List<ShopResponse>> getAllShops (int visitorId){
        try {
            Member m = visitorController.getVisitorLoggedIn(visitorId);
            List<ShopResponse> outPut = new LinkedList<>();
            List<Shop> shops = shopController.getListOfShops();
            for (Shop s:shops){
                outPut.add(new ShopResponse(s));
            }
            return new Response<>(outPut);
        }catch (InnerLogicException e){
            return new Response<>("error: " + e.getMessage());
        }catch (Exception e) {
            return new Response<>("fatal error: " + e.getMessage());
        }
    }
}
