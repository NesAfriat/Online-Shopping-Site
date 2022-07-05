package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.*;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.PurchasePolicies.PurchasePolicy;
import com.example.seprojectweb.Domain.Market.Responses.*;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;
import com.example.seprojectweb.Domain.Market.SystemData.DailySystemData;
import com.example.seprojectweb.Domain.Market.SystemData.SystemDataLogger;
import com.example.seprojectweb.Domain.PersistenceManager;
import com.example.seprojectweb.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class MarketRepresentative {

    public static final String DEFAULT_ADMIN_USERNAME = "HannaLaslo";
    public static final String DEFAULT_ADMIN_PASSWORD = "TheQueen";
    private final VisitorController visitorController;
    private final ShopController shopController;
    private final PurchaseCashier purchaseCashier;
    private final RoleController roleController;
    private Collection<Member> admins;

    private SystemDataLogger systemDataLogger;

    private MarketRepresentative() {
        visitorController = VisitorController.getInstance();
        shopController = ShopController.getInstance();
        purchaseCashier = PurchaseCashier.getInstance();
        roleController = RoleController.getInstance();
        admins = loadAdmins();
        systemDataLogger = new SystemDataLogger();
        //admins = new LinkedList<>();
    }

    public Response<List<DiscountResponse>> getDiscountsPolicies(Integer visitorId, Integer shopId) {
        try{
            List<Discount> res = shopController.getDiscountsPolicies(visitorId, shopId);
            List<DiscountResponse> output = new LinkedList<>();
            for(Discount d: res){
                output.add(DiscountResponse.buildDiscountResponse(shopId, d));
            }
            return new Response<>(output);
        }catch (InnerLogicException e){
            return new Response<>("error - couldn't get discount policies\nError message: "+e.getMessage());
        }catch (Exception e){
            Logger.getInstance().logError("getDiscountsPolicies failed for visitor " + visitorId + "at shop " +shopId +
                    "\n error message:" + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<PurchasePolicyResponse>> getPurchasesPolicies(Integer visitorId, Integer shopId) {
        try{
            List<PurchasePolicy> res = shopController.getPurchasesPolicies(visitorId, shopId);
            List<PurchasePolicyResponse> output = new LinkedList<>();
            for(PurchasePolicy d: res){
                output.add(new PurchasePolicyResponse(d));
            }
            return new Response<>(output);
        }catch (InnerLogicException e){
            return new Response<>("error - couldn't get purchase policies\nError message: "+e.getMessage());
        }catch (Exception e){
            Logger.getInstance().logError("getPurchasesPolicies failed for visitor " + visitorId + "at shop " +shopId +
                    "\n error message:" + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<ShoppingCartResponse> getVisitorCart(Integer visitorId) {
        try{
            ShoppingCart res = visitorController.getShoppingCart(visitorId);
            return new Response<>(new ShoppingCartResponse(res));
        }catch(InnerLogicException e){
            return new Response<>("error - couldn't get your cart\nError message: "+e.getMessage());
        }catch(Exception e){
            Logger.getInstance().logError("getVisitorCart failed for visitor " + visitorId +
                    "\n error message:" + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public void initMarket() {
        this.admins = visitorController.getAllAdmins();
        if (admins == null || admins.isEmpty()){
            try {
                Visitor visitor = visitorController.visitSystem();
                Member admin = visitorController.adminRegister(visitor.getId(), DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
                admins = new ConcurrentLinkedQueue<>();
                admins.add(admin);
            }
            catch (Exception e) {
                Logger.getInstance().logError("load admin failed " + "\nerror message: " + e.getMessage() + "\n\n");
                throw new RuntimeException("can not init market with out admins");
            }
        }
    }


    private static class MarketRepresentativeHolder {
        private static final MarketRepresentative instance = new MarketRepresentative();
    }

    public static MarketRepresentative getInstance() {
        return MarketRepresentativeHolder.instance;
    }

    private Collection<Member> loadAdmins() {
        // get all admins from database
        this.admins = visitorController.getAllAdmins();
        if (admins == null || admins.isEmpty()){
            try {
                Visitor visitor = visitorController.visitSystem();
                Member admin = visitorController.adminRegister(visitor.getId(), DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
                admins = new ConcurrentLinkedQueue<>();
                admins.add(admin);
                visitorController.leaveSystem(visitor.getId());
            }
            catch (Exception e) {
                Logger.getInstance().logError("load admin failed " + "\nerror message: " + e.getMessage() + "\n\n");
                throw new RuntimeException("can not init market with out admins");
            }
        }

        return admins;
    }

    public Response<MemberResponse> addAdmin(int visitorId, String userName){
        try {
            Member loggedIn = visitorController.getVisitorLoggedIn(visitorId);
            if(!admins.contains(loggedIn))
            {
                throw new InnerLogicException(loggedIn.getUsername() + " tried to add admin, but has no permission to do so");
            }
            Member newAdmin = visitorController.getMember(userName);
            newAdmin.setAdmin(true);
            PersistenceManager.update(newAdmin);
            admins.add(newAdmin);
            return new Response<>(new MemberResponse(newAdmin));

        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("add admin failed: visitor " + visitorId + " faild to make " + userName+ " admin" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new admin\nError message: "+e.getMessage());
        }catch (IllegalAccessError e) {
            Logger.getInstance().logError("add admin failed: visitor " + visitorId + " faild to make " + userName+ " admin" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("add admin failed: visitor " + visitorId + " faild to make " + userName+ " admin" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }


    public void noticeAdmins() {
        for(Member admin : admins){
            admin.sendDataUpdateNotice();
        }
    }
    public Response<List<DailySystemDataResponse>> getSystemData(int visitorId, String startDate, String endDate){
        try {
            Member loggedin = visitorController.getVisitorLoggedIn(visitorId);
            if(!admins.contains(loggedin)) throw new InnerLogicException("visitor " + visitorId + "tried to do admin action but is not logged is as admin");
            List<DailySystemDataResponse> output = new LinkedList<>();
            for (DailySystemData data :systemDataLogger.getSystemData(startDate, endDate)) {
                output.add(new DailySystemDataResponse(data));
            }

            Logger.getInstance().logEvent("get system data success: " + visitorId + "got system data from date " +
                    startDate + " to date " + endDate);

            return new Response<>(output);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get system data failed: " + visitorId + "got system data from date " +
                            startDate + " to date " + endDate +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add product to your cart\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("get system data failed: " + visitorId + "got system data from date " +
                    startDate + " to date " + endDate +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("get system data failed: " + visitorId + "got system data from date " +
                    startDate + " to date " + endDate +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }


    public Response<ShoppingCartResponse> getShoppingCart(int visitorId) {
        try {
            ShoppingCart shoppingCart = visitorController.getShoppingCart(visitorId);
            Logger.getInstance().logEvent("get shopping success: visitor " + visitorId + "\n\n");
            return new Response<>(new ShoppingCartResponse(shoppingCart));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get shopping cart failed: visitor " + visitorId + " didn't got shopping cart " +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't get your cart\nError message: "+e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("get shop histories failed: visitor " + visitorId + " didn't got shop " +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<ShoppingCartResponse> addProductToShoppingCart(int visitorID, int shopID, int productID, int quantity) {
        try {
            Product prod = shopController.verifyProductExist(shopID, productID);
            ShoppingCart shoppingCart = visitorController.addProductToShoppingCart(visitorID, shopID, prod, quantity);
            Logger.getInstance().logEvent("a new product: " + productID + " was added to visitor: " + visitorID + " from shop: " + shopID + "with quantity: " + quantity + "\n\n");
            return new Response<>(new ShoppingCartResponse(shoppingCart));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("add Product To ShoppingCart failed for visitor: " + visitorID + "in shop: " + shopID + " with product: " + productID + " and quantity: " + quantity +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add product to your cart\nError message: "+e.getMessage());
        }catch (IllegalAccessError e) {
            Logger.getInstance().logError("add Product To ShoppingCart failed for visitor: " + visitorID + "in shop: " + shopID + " with product: " + productID + " and quantity: " + quantity +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("add Product To ShoppingCart failed for visitor: " + visitorID + "in shop: " + shopID + " with product: " + productID + " and quantity: " + quantity +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<ShoppingCartResponse> removeProductFromShoppingCart(int visitorID, int shopID, int productID) {
        try {
            ShoppingCart shoppingCart = visitorController.removeProductFromShoppingCart(visitorID, shopID, productID);
            Logger.getInstance().logEvent("a new product: " + productID + " was remove to visitor: " + visitorID + " from shop: " + shopID + "\n\n");
            return new Response<>(new ShoppingCartResponse(shoppingCart));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("remove Product To ShoppingCart failed for visitor: " + visitorID + "in shop: " + shopID + " with product: " + productID +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't remove product from your cart\nError message: "+e.getMessage());
        }
        catch (IllegalAccessError e) {
            Logger.getInstance().logError("remove Product To ShoppingCart failed for visitor: " + visitorID + "in shop: " + shopID + " with product: " + productID +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("remove Product To ShoppingCart failed for visitor: " + visitorID + "in shop: " + shopID + " with product: " + productID +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<PurchaseHistoryResponse>> purchaseCart(int visitorID, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
        try {
            List<PurchaseHistory> purchaseHistories = purchaseCashier.purchaseCart(visitorID, creditCardNumber, date, cvs, country, city, street, zip);
            List<PurchaseHistoryResponse> output = new LinkedList<>();
            for (PurchaseHistory p : purchaseHistories) {
                PurchaseHistoryResponse pr = new PurchaseHistoryResponse(p);
                output.add(pr);
            }
            Logger.getInstance().logEvent("visitor: " + visitorID + " has purchased his cart" + "\n\n");
            return new Response<>(output);
        }
        catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorID + " has failed to purchased his cart\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't purchase your cart\nError message: "+e.getMessage());
        }
        catch (IllegalAccessError e) {
            Logger.getInstance().logError("visitor: " + visitorID + " has failed to purchased his cart\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("visitor: " + visitorID + " has failed to purchased his cart\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<ShopResponse> closeShop(int visitId, int shopId) {
        try {
            Shop shop = shopController.closeShop(visitId, shopId);
            Logger.getInstance().logEvent("a founder:" + visitId + " has closed his shop: " + shopId + "\n\n");
            return new Response<>(new ShopResponse(shop));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("a visitor:" + visitId + " has failed to closed his shop: " + shopId + "\n\n");
            return new Response<>("error - couldn't close shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("a visitor:" + visitId + " has failed to closed his shop: " + shopId + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("a visitor:" + visitId + " has failed to closed his shop: " + shopId + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }

    }

    public Response<ShopResponse> reOpenShop(int visitId, int shopId) {
        try {
            Shop shop = shopController.reOpenShop(visitId, shopId);
            Logger.getInstance().logEvent("a founder:" + visitId + " has re-open his shop: " + shopId + "\n\n");
            return new Response<>(new ShopResponse(shop));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("a visitor:" + visitId + " has failed to re-open his shop: " + shopId + "\n\n");
            return new Response<>("error - couldn't re-open shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("a visitor:" + visitId + " has failed to re-open his shop: " + shopId + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("a visitor:" + visitId + " has failed to re-open his shop: " + shopId + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    //RETURN ONLY RESPONSE TO SERVICE
    //SECURITY CONSTRAINTS
    public Response<List<PurchaseHistoryResponse>> getShopPurchaseHistory(int visitorId, int shopId) {
        try {
            //verify visitor is the shop owner or admin
            String memberUsername = visitorController.getVisitorLoggedIn(visitorId).getUsername();
            if (!pIsAdmin(visitorId))
                roleController.verifyShopOwner(shopId, memberUsername);
            List<PurchaseHistory> purchaseHistories = purchaseCashier.getShopHistories(shopId);
            List<PurchaseHistoryResponse> output = purchaseHistories.stream().map(PurchaseHistoryResponse::new).collect(Collectors.toList());
            Logger.getInstance().logEvent("get shop histories success: visitor " + visitorId + " got shop " + shopId + "\n\n");
            return new Response<>(output);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get shop histories failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't collect shop purchase history\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("get shop histories failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("get shop histories failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't get shop info\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("get shop failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("get shop failed: visitor " + visitorId + " didn't got shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }

    }

    public Response<VisitorResponse> visitSystem() {
        try {
            Visitor visitor = visitorController.visitSystem();
            Logger.getInstance().logEvent("visit system success: new visitor with id " + visitor.getId() + " entered the system" + "\n\n");
            systemDataLogger.updateVisitEvent();
            return new Response<>(new VisitorResponse(visitor));
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("visit system failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("visit system failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver) {
        try {
            Visitor visitor = visitorController.login(visitorId, username, password, memberObserver);
            Logger.getInstance().logEvent("login success: visitor " + visitorId + " logged in to member " + username + "\n\n");
            systemDataLogger.updateLoginEvent(username, visitor.getLoggedIn().isAdmin());
            return new Response<>(new VisitorResponse(visitor));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("login failed: visitor " + visitorId + " failed to login to member " + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't login\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("login failed: visitor " + visitorId + " failed to login to member " + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("login failed: visitor " + visitorId + " failed to login to member " + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't logout\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("logout fatal failed: visitor " + visitorId + " failed to logout" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("logout fatal failed: visitor " + visitorId + " failed to logout" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't leave system\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("leave system failed: visitor " + visitorId + " failed to leave the system" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("leave system failed: visitor " + visitorId + " failed to leave the system" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't register new member\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("register failed: visitor " + visitorId + " failed to register new member with username "
                    + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("register failed: visitor " + visitorId + " failed to register new member with username "
                    + username +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't open shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("open shop failed: visitor " + visitorID + " failed to open new shop" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("open shop failed: visitor " + visitorID + " failed to open new shop" +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't remove product from shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("remove product from shop failed: visitor " + visitorId + " failed to remove product "
                    + productId + " from shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("remove product from shop failed: visitor " + visitorId + " failed to remove product "
                    + productId + " from shop " + shopId +
                    "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            roleController.verifyManageInventoryPermission(visitorId, shopId);
            Product product = shopController.addProductToShop(visitorId, shopId, productName, price, description, quantity, category);
            Logger.getInstance().logEvent("add product to shop success: visitor " + visitorId + " added product "
                    + product.getId() + " to shop " + shopId + "\n\n");
            return new Response<>(new ProductResponse(shopId, product));
        }
        catch (InnerLogicException e) {
            Logger.getInstance().logEvent("add product to shop failed: visitor " + visitorId + " failed to add product to shop "
                    + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new product to shop\nError message: "+e.getMessage());
        }
        catch (IllegalAccessError e) {
            Logger.getInstance().logError("add product to shop failed: visitor " + visitorId + " failed to add product to shop "
                    + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("add product to shop failed: visitor " + visitorId + " failed to add product to shop "
                    + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't update product description\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("change product description failed: visitor " + visitorId + " failed to change product "
                    + productId + " description at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("change product description failed: visitor " + visitorId + " failed to change product "
                    + productId + " description at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't update product name\nError message: "+e.getMessage());
        } catch (IllegalAccessError e){
            Logger.getInstance().logError("change product name failed: visitor " + visitorId + " failed to change product "
                    + productId + " name at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("change product name failed: visitor " + visitorId + " failed to change product "
                    + productId + " name at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't update product price\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e){            Logger.getInstance().logError("change product price failed: visitor " + visitorId + " failed to change product "
                + productId + " price at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");}
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
            return new Response<>("error - couldn't update product quantity\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add product quantity failed: visitor " + visitorId + " failed to add product "
                    + productId + " quantity at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            return new Response<>("error - couldn't update product description\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("reduce product quantity failed: visitor " + visitorId + " failed to reduce product "
                    + productId + " quantity at shop " + shopId + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<ProductResponse>> searchProductByName(int visitorId, String name) {
        try {
            HashMap<Shop, List<Product>> productsAndShop = shopController.searchProductByName(visitorId, name);
            return hashToProducts(productsAndShop);
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("search product by name failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<ProductResponse>> searchProductByKeyWord(int visitorId, String keyWord) {
        try {
            HashMap<Shop, List<Product>> productsAndShop = shopController.searchProductByKeyWord(visitorId, keyWord);
            return hashToProducts(productsAndShop);
        } catch (IllegalAccessError |Exception e) {
            Logger.getInstance().logError("search product by key word failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<ProductResponse>> searchProductByKeyCategory(int visitorId, String category) {
        try {
            HashMap<Shop, List<Product>> productsAndShop = shopController.searchProductByCategory(visitorId, category);
            return hashToProducts(productsAndShop);
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("search product by category failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
            ShopOwner shopOwner = roleController.assignShopOwner(shopController.getShop(shopID) ,visitorId, usernameToAssign);
            Logger.getInstance().logEvent("assign shop owner success: visitor " + visitorId + " assigned member " + usernameToAssign +
                    " as owner in shop " + shopID + "\n\n");
            return new Response<>(new ShopOwnerResponse(usernameToAssign, shopOwner));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't assign new owner to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<AssignAgreementResponse> initAssignShopOwner(int visitorId, String usernameToAssign, int shopID) {
        try {
            AssignAgreement assignAgreement = shopController.initAssignShopOwner(visitorId, usernameToAssign, shopID);
            Logger.getInstance().logEvent("init assign shop owner success: visitor " + visitorId + " assigned member " + usernameToAssign +
                    " as owner in shop " + shopID + "\n\n");
            return new Response<>(new AssignAgreementResponse(assignAgreement));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't assign new owner to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<AssignAgreementResponse> approveAssignAgreement(int visitorId, String usernameToAssign, int shopID) {
        try {
            AssignAgreement assignAgreement = shopController.approveAssignAgreement(visitorId, usernameToAssign, shopID);
            Logger.getInstance().logEvent("approved assign success: visitor " + visitorId + " assigned member " + usernameToAssign +
                    " as owner in shop " + shopID + "\n\n");
            return new Response<>(new AssignAgreementResponse(assignAgreement));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't assign new owner to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("assign shop owner failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as Owner in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<AssignAgreementResponse>> getAllShopAssignAgreements(int visitorId, int shopID) {
        try {
            List<AssignAgreement> assignAgreements = shopController.getAllShopAssignAgreements(visitorId, shopID);
            Logger.getInstance().logEvent("get all assign agreements success: visitor " + visitorId +
                    " shop " + shopID + "\n\n");
            List<AssignAgreementResponse> assignAgreementResponses = new ArrayList<>();
            for (AssignAgreement assignAgreement : assignAgreements){
                assignAgreementResponses.add(new AssignAgreementResponse(assignAgreement));
            }
            return new Response<>(assignAgreementResponses);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("get all assign agreements failed: visitor " + visitorId + " failed" +
                    " in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't assign new owner to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("get all assign agreements fatal failed: visitor " + visitorId + " failed" +
                    " in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<RoleResponse> assignShopManager(int visitorId, String usernameToAssign, int shopID) {

        try {
            ShopManager shopManager = roleController.assignShopManager(shopController.getShop(shopID),visitorId, usernameToAssign);
            Logger.getInstance().logEvent("assign shop manager success: visitor " + visitorId + " assigned member " + usernameToAssign +
                    " as manager in shop " + shopID + "\n\n");
            return new Response<>(new ShopManagerResponse(usernameToAssign, shopManager));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("assign shop manager failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as manager in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't assign new manager to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("assign shop manager failed: visitor " + visitorId + " failed to assign member " + usernameToAssign +
                    " as manager in shop " + shopID + "\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<ShopManagerResponse> setPermission(int visitorId, int shopId, String memberUserName, int permission) {
        try {
            ShopManager shopManager = roleController.setManagerPermission(visitorId, shopId, permission, memberUserName);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has set permission: " + permission + " on member: " + memberUserName + "in shop: " + shopId + "\n\n");
            return new Response<>(new ShopManagerResponse(memberUserName, shopManager));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to set permission on user: " + memberUserName + "\n\n");
            return new Response<>("error - couldn't set new permission to manager\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to set permission on user: " + memberUserName + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<ShopManagerResponse> removePermission(int visitorId, int shopId, String memberUserName, int permission) {
        try {
            ShopManager shopManager = roleController.removeManagerPermission(visitorId, shopId, permission, memberUserName);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has remove permission: " + permission +
                    " from member: " + memberUserName + "in shop: " + shopId + "\n\n");
            return new Response<>(new ShopManagerResponse(memberUserName, shopManager));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to remove permission on user: " + memberUserName + "\n\n");
            return new Response<>("error - couldn't remove permission from manager\nError message: "+e.getMessage());
        } catch (IllegalAccessError |Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to remove permission on user: " + memberUserName + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
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
                    throw new InnerLogicException("this is fatal in getMyShops");
                }
            }
            return new Response<>(output);
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to get management info in shop: " + shopId + "\n\n");
            return new Response<>("error - couldn't get shop management info\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to get management info in shop: " + shopId + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<RoleResponse> getPermissionsOfMember(int visitorId, String memberUserName, int shopId) {
        try {
            ShopManager shopManager = roleController.getPermissionsOfMember(visitorId, memberUserName, shopId);
            //logger
            return new Response<>(new ShopManagerResponse(memberUserName, shopManager));
        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't get permission of a manager\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("get Permissions Of Member failed\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<SingleProductDiscountResponse> addProductDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, int productId) {
        try {
            Date date = new GregorianCalendar(expireYear, expireMonth - 1, expireDay).getTime(); // months are zero based indexed
            SingleProductDiscount discount = shopController.addProductDiscount(visitorId, shopId, percentage, date, productId);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added product discount: " + discount.getId() + " to shop: " + shopId + "\n\n");
            return new Response<>(new SingleProductDiscountResponse(shopId, discount));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add product discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new discount to product\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add discount failed: visitor " + visitorId + " has failed to add product discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<CategoryDiscountResponse> addCategoryDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay, String category) {
        try {
            Date date = new GregorianCalendar(expireYear, expireMonth - 1, expireDay).getTime(); // months are zero based indexed
            CategoryDiscount discount = shopController.addCategoryDiscount(visitorId, shopId, percentage, date, category);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added category discount: " + discount.getId() + " to shop: " + shopId + "\n\n");
            return new Response<>(new CategoryDiscountResponse(shopId, discount));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add category discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new discount to category\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add discount failed:  visitor " + visitorId + " has failed to add category discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<TotalShopDiscountResponse> addTotalShopDiscount(int visitorId, int shopId, double percentage, int expireYear, int expireMonth, int expireDay) {
        try {
            Date date = new GregorianCalendar(expireYear, expireMonth - 1, expireDay).getTime(); // months are zero based indexed
            TotalShopDiscount discount = shopController.addTotalShopDiscount(visitorId, shopId, percentage, date);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added entire shop discount: " + discount.getId() + " to shop: " + shopId + "\n\n");
            return new Response<>(new TotalShopDiscountResponse(shopId, discount));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add entire shop discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new discount to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add discount failed: visitor " + visitorId + " has failed to add entire shop discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<XorDiscountResponse> addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId, int expireYear, int expireMonth, int expireDay) {
        try {
            XorDiscount discount = shopController.addXorDiscount(visitorId, shopId, discountAId, discountBId);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added xor discount: " + discount.getId() + " to shop: " + shopId + "\n\n");
            return new Response<>(new XorDiscountResponse(shopId, discount));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add xor discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't composite discounts\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add condition failed: visitor " + visitorId + " has failed to add xor discount to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<DiscountResponse> addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) {
        try {
            if(type == null)
                return new Response<>("error - cannot parse type");
            Discount discount = shopController.addPQCondition(visitorId, shopId, type, discountId, productId, minProductQuantity);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added product quantity condition to shop" +
                    shopId + "\n\n");
            return new Response<>(DiscountResponse.buildDiscountResponse(shopId, discount));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add product quantity condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't set product quantity discount\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add condition failed: visitor " + visitorId + " has failed to add product quantity condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<DiscountResponse> addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) {
        try {
            if(type == null)
                return new Response<>("error - cannot parse type");
            Discount discount = shopController.addTBPCondition(visitorId, shopId, type, discountId, minBasketPrice);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added total basket price condition to shop" +
                    shopId + "\n\n");
            return new Response<>(DiscountResponse.buildDiscountResponse(shopId, discount));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add total basket price condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't set discount to total basket price\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("add condition failed: visitor " + visitorId + " has failed to add total basket price condition to shop" +
                    shopId + "\n" + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<MemberResponse> cancelMembership(int visitorId, String userName) {

        try {
            if (!pIsAdmin(visitorId)) {
                return new Response<>("u dont have permission to cancel a membership");
            }
            if (roleController.isRoleExists(userName)) {
                return new Response<>(userName + " have role in shop");
            }
            return new Response<>(new MemberResponse(visitorController.unRegister(userName)));
        } catch (InnerLogicException e) {
            Logger.getInstance().logError("cancel Membership failed: visitor: " + visitorId + " has failed to cancel his member ship\n" + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't cancel membership of member\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    private boolean pIsAdmin(int id) throws InnerLogicException {
        Member m = visitorController.getVisitorLoggedIn(id);
        return admins.contains(m);
    }

    public Response<Boolean> isAdmin(Integer visitorId) {
        try{
            Boolean res = pIsAdmin(visitorId);
            return new Response<>(res);
        }catch(InnerLogicException e){
            return new Response<>("error - couldn't assert if member is an admin\nError message: "+e.getMessage());
        }catch(IllegalAccessError | Exception e){
            Logger.getInstance().logError("is admin failed: check if visitor " + visitorId + "fail\n" + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }
    public Response<PurchasePolicyResponse> addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity) {
        try {
            PurchasePolicy purchasePolicy = shopController.addAtMostFromProductPolicy(visitorId, shopId, productId, maxQuantity);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added purchase policy, at most: " + maxQuantity + " from product: " + productId + " to shop: " + shopId + "\n\n");
            return new Response<>(new PurchasePolicyResponse(purchasePolicy));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add 'at most from product' policy to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new 'at most' purchase policy to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to add 'at most from product' policy to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<PurchasePolicyResponse> addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity) {
        try {
            PurchasePolicy purchasePolicy = shopController.addAtLeastFromProductPolicy(visitorId, shopId, productId, minQuantity);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has added purchase policy, at least: " + minQuantity + " from product: " + productId + " to shop: " + shopId + "\n\n");
            return new Response<>(new PurchasePolicyResponse(purchasePolicy));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to add 'at least from product' policy to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't add new 'at least' purchase policy to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to add 'at least from product' policy to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<PurchasePolicyResponse> composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2) {
        try {
            if(type == null)
                return new Response<>("error - cannot parse type");
            PurchasePolicy composePurchasePolicy = shopController.composePurchasePolicies(visitorId, shopId, type, policyId1, policyId2);
            Logger.getInstance().logEvent("visitor: " + visitorId + " has composed purchase polices from: " + policyId1 + " and " + policyId2 + " from type " + type +
                    "to shop: " + shopId + "\n\n");
            return new Response<>(new PurchasePolicyResponse(composePurchasePolicy));
        } catch (InnerLogicException e) {
            Logger.getInstance().logEvent("visitor: " + visitorId + " has failed to composed purchase polices to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("error - couldn't compose new policy to shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("visitor: " + visitorId + " has failed to composed purchase polices to shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    /***
     *
     * @param visitorId
     * @param userName
     * @param shopId
     * @return
     */
    public Response<List<ShopOwnerResponse>> removeShopOwner(int visitorId, String userName, int shopId) {
        try {
            HashMap<String, ShopOwner> shopOwnersRemoved = roleController.removeShopOwner(visitorId, userName, shopId);
            List<ShopOwnerResponse> res = new LinkedList<>();
            for (Map.Entry<String, ShopOwner> entry : shopOwnersRemoved.entrySet()) {
                res.add(new ShopOwnerResponse(entry.getKey(), entry.getValue()));
            }
            return new Response<>(res);

        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't remove owner from shop\nError message: "+e.getMessage());
        } catch (IllegalAccessError | Exception e) {
            Logger.getInstance().logError("remove Shop Owner failed: visitor: " + visitorId + " has failed to remove " + userName + "from shop: " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<ShopResponse>> getAllOpenShops(int visitorId) {
        try {
//            Member m = visitorController.getVisitorLoggedIn(visitorId);
            List<Shop> shops = shopController.getListOfShops();
            List<ShopResponse> outPut = new LinkedList<>();
            for (Shop s:shops)
              outPut.add(new ShopResponse(s));
            return new Response<>(outPut);

        }catch (IllegalAccessError | Exception e){
            Logger.getInstance().logError("get All Open Shops failed: visitor: " + visitorId + " has failed to get all open shops" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<ShopResponse>> getMyShops(Integer visitorId) {
        try{
            Member member = visitorController.getVisitorLoggedIn(visitorId);
            List<Shop> memberShops = roleController.getMemberShops(member);
            //Set<Integer> managedShops = roleController.getListOfShops(visitorId);
            //List<Shop> shops = shopController.getListOfShops(visitorId, managedShops);
            List<ShopResponse> outPut = new LinkedList<>();
            for (Shop s:memberShops)
                outPut.add(new ShopResponse(s));
            return new Response<>(outPut);
        }
        catch (InnerLogicException e) {
            return new Response<>("error - couldn't get your shops\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("get my  Shops failed: visitor: " + visitorId + " has failed to get his shops" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch(Exception e) {
            Logger.getInstance().logError("get my  Shops failed: visitor: " + visitorId + " has failed to get his shops" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: " + e.getMessage() + "\n\n");
        }
    }

    public Response<List<MemberResponse>> getAllMembers(int visitorId) {
        try {
            if(!pIsAdmin(visitorId)){
                return new Response<>("ur not admin!");
            }
            List<Member> members = visitorController.getMembersList();
            List<MemberResponse> outPut = new LinkedList<>();
            for (Member mi: members) {
                outPut.add(new MemberResponse(mi));
            }
            return new Response<>(outPut);
        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't get all members\nError message: "+e.getMessage());
        }catch (IllegalAccessError e) {
            Logger.getInstance().logError("get All members failed: visitor: " + visitorId + " has failed to get all members" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e){
            Logger.getInstance().logError("get All members failed: visitor: " + visitorId + " has failed to get all members" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }
    public Response<List<VisitorResponse>> getAllVisitors(int visitorId){
        try{
            if(!pIsAdmin(visitorId)){
                return new Response<>("ur not admin!");
            }
            List<VisitorResponse> outPut = new LinkedList<>();
            List<Visitor> visitors = visitorController.getAllVisitors();
            for (Visitor visitor: visitors){
                outPut.add(new VisitorResponse(visitor));
            }
            return new Response<>(outPut);
        }
        catch (InnerLogicException e) {
            return new Response<>("error - couldn't get all visitors\nError message: "+e.getMessage());
        }
        catch (IllegalAccessError e) {
            Logger.getInstance().logError("get All visitors failed: visitor: " + visitorId + " has failed to get all visitors" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e){
            Logger.getInstance().logError("get All visitors failed: visitor: " + visitorId + " has failed to get all visitors" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }

    }
    public Response<List<ShopOwnerResponse>> getAllShopOwners(int visitorId){
        try {
            if(!pIsAdmin(visitorId)){
                return new Response<>("ur not admin!");
            }
            List<ShopOwnerResponse> outPut = new LinkedList<>();
            List<ShopOwner> shopOwners = roleController.getALLOwners();
            for (ShopOwner shopOwner: shopOwners){
                outPut.add(new ShopOwnerResponse(shopOwner.getMemberID(),shopOwner));
            }
            return new Response<>(outPut);
        }
        catch (InnerLogicException e) {
            return new Response<>("error - couldn't get all shops owners\nError message: "+e.getMessage());
        }
        catch (IllegalAccessError e) {
            Logger.getInstance().logError("get All shop owners failed: visitor: " + visitorId + " has failed to get all shop owners" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e){
            Logger.getInstance().logError("get All shop owners failed: visitor: " + visitorId + " has failed to get all shop owners" +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }

    }

    public Response<BidResponse> bidProduct(int visitorId, int shopId, int productId, int quantity, double price)  {
        try {
            return new Response<>(new BidResponse(visitorController.bidProduct(visitorId, shopId, productId, quantity, price), shopId));
        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't place a bid on a product\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("bid Product failed: visitor " + visitorId + " has failed to bid on product " + productId+
                    " in shop " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("bid Product failed: visitor " + visitorId + " has failed to bid on product " + productId+
                    " in shop " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<BidResponse> approveBid(int visitorId, int shopId, int bidId)  {
        try {
            String loggedInUserName = visitorController.getVisitorLoggedIn(visitorId).getUsername();
            return new Response<>(new BidResponse(shopController.approveBid(loggedInUserName, shopId, bidId), shopId));
        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't approved the bid on a product\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("approve Bid failed: visitor " + visitorId + " has failed to approve bid " + bidId +
                    " in shop " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("approve Bid failed: visitor " + visitorId + " has failed to approve bid " + bidId +
                    " in shop " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<BidResponse> incrementBidPrice(int visitorId, int shopId, int bidId, double newPrice) {
        try {
        visitorController.getVisitorLoggedIn(visitorId);
        String loggedInUserName = visitorController.getVisitorLoggedIn(visitorId).getUsername();
        return new Response<>(new BidResponse(shopController.incrementBidPrice(shopId, bidId, loggedInUserName, newPrice), shopId));
        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't set new price to a bid on a product\nError message: "+e.getMessage());
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("increment Bid Price failed: visitor " + visitorId + " has failed to increment Bid Price " + bidId +
                    " in shop " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("increment Bid Price failed: visitor " + visitorId + " has failed to increment Bid Price " + bidId +
                    " in shop " + shopId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }


    public Response<PurchaseHistoryResponse> purchaseBid(int visitorId,int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) {
        try {
            return new Response<>(new PurchaseHistoryResponse(visitorController.purchaseBid(visitorId, bidId, creditCardNumber, date, cvs, country, city, street, zip)));
        } catch (InnerLogicException e) {
            return new Response<>("error - couldn't purchase the product via bid\nError message: "+e.getMessage());
        }catch (IllegalAccessError e) {
            Logger.getInstance().logError("purchase Bid failed: visitor " + visitorId + " has failed to purchase Bid " + bidId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (Exception e) {
            Logger.getInstance().logError("purchase Bid failed: visitor " + visitorId + " has failed to purchase Bid " + bidId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<BidResponse>> getAllMemberBid(int visitorId) {
        try{
            List<BidResponse> bidResponseList= new LinkedList<>();
            for (Bid bid: visitorController.getAllMemberBid(visitorId)){
                bidResponseList.add(new BidResponse(bid, bid.getShopId()));
            }
            return new Response<>(bidResponseList);
        }
        catch (IllegalAccessError e) {
            Logger.getInstance().logError("get All Member Bid failed: visitor " + visitorId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (InnerLogicException e) {
            return new Response<>("error - couldn't get all your bids\nError message: "+e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("get All Member Bid failed: visitor " + visitorId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }

    public Response<List<BidResponse>> getAllShopBid(int visitorId, int shopId) {
        try{
            List<BidResponse> bidResponseList= new LinkedList<>();
            for (Bid bid: shopController.getAllShopBid(visitorId, shopId)){
                bidResponseList.add(new BidResponse(bid, bid.getShopId()));
            }
            return new Response<>(bidResponseList);
        } catch (IllegalAccessError e) {
            Logger.getInstance().logError("get All Shop Bid failed: visitor " + visitorId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
        catch (InnerLogicException e) {
            return new Response<>("error - couldn't get all shop's products bids\nError message: "+e.getMessage());
        } catch (Exception e) {
            Logger.getInstance().logError("get All Shop Bid failed: visitor " + visitorId +
                    "\n\nerror message: " + e.getMessage() + "\n\n");
            return new Response<>("fatal - Unexpected error!\nError message: "+e.getMessage()+"\n\n");
        }
    }


}
