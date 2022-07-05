package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.*;
import com.example.seprojectweb.Domain.Market.PurchasePolicies.PurchasePolicy;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;
import com.example.seprojectweb.Domain.PersistenceManager;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopController {
    //private AtomicInteger nextShopId;
    private Map<Integer, Shop> shops;
    private final RoleController roleController;

    private ShopController() {
        shops = new ConcurrentHashMap<>();
        //nextShopId = new AtomicInteger(0);
        roleController = RoleController.getInstance();
        shops = loadAllOpenShops();
    }

    public static ShopController getInstance() {
        return ShopControllerHolder.instance;
    }



    // payment info - credit card info
    // delivery location -
    // id and phone of founder
    //
    public Shop openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) throws InnerLogicException {
        if (shopName == null || isShopExist(shopName)) {
            throw new InnerLogicException("Shop with name: " + shopName + " already exist");
        }
        if (shopName.trim().isEmpty()) {
            throw new InnerLogicException("Shop name cannot be empty");
        }
        if (shopDescription == null || shopDescription.trim().isEmpty()) {
            throw new InnerLogicException("Shop description cannot be empty");
        }
        if (creditCard == null || creditCard.trim().isEmpty()) {
            throw new InnerLogicException("Shop creditCard cannot be empty");
        }
        if (!creditCard.matches("[0-9]*")) {
            throw new InnerLogicException("Shop creditCard cannot have letters");
        }
        if (memberPhone == null || !memberPhone.matches("[0-9]*")) {
            throw new InnerLogicException("Shop phone cannot have letters");
        }
        if (memberPhone.length() != 10) {
            throw new InnerLogicException("Shop phone is not in the right length");
        }

        VisitorController visitorController = VisitorController.getInstance();
        Member founder = visitorController.getVisitorLoggedIn(visitorID);
        //init connection
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();

        Shop shop = new Shop(founder, visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation);

        dbConnection.persist(shop);

        ShopOwner shopOwner =  roleController.assignFounder(founder, shop);
        //remove him from shop if database fails
        dbConnection.addFailure(()->roleController.removeAllShop(shopOwner));
        dbConnection.persist(shopOwner);


        shops.put(shop.getShopId(), shop);
        dbConnection.addFailure(()->shops.remove(shop.getShopId()));
        //nextShopId.incrementAndGet();
        dbConnection.commitTransaction();
        dbConnection.closeConnections();

        return shop;
    }
    public ConcurrentHashMap<Integer,Shop> loadAllOpenShops(){
        ConcurrentHashMap<Integer,Shop> outPut = new ConcurrentHashMap<>();
        List shops = PersistenceManager.findAll(Shop.class);
        for (Object s : shops){
            Shop si = (Shop)s;
            outPut.put(si.getShopId(),si);
        }
        return outPut;
    }
    //check only on exiting
    private boolean isShopExist(String shopName) {
        return shops.values().stream().anyMatch(s -> s.getName().equals(shopName));
    }


    public boolean verifyOpenShop(Integer shopID)  {
        if (!isExistShop(shopID)) {
            return false;
        }
        return shops.get(shopID).isOpen();
    }

    public Shop closeShop(int visitId, int shopId) throws InnerLogicException {

        Shop s = getShop(shopId);
        String userName = roleController.getUserName(visitId);
        if (!s.getFounder().equals(userName)) {
            throw new InnerLogicException("user: " + userName + " tried to close a shop - but he isn't the founder");
        }
        if (!s.isOpen()) {
            throw new InnerLogicException("shop is already closed");
        }
        s.close();
        return s;
    }


    public Shop reOpenShop(int visitId, int shopId) throws InnerLogicException {
        Shop s = getShop(shopId);
        String userName = roleController.getUserName(visitId);
        if (!s.getFounder().equals(userName)) {
            throw new InnerLogicException("user: " + userName + " tried to close a shop - but he isn't the founder");
        }
        s.open();
        return s;
    }

    public boolean validatePurchasePolicy(int shopId, int visitorId, ShoppingBasket basket) {
        //TODO look up on this
        return true;
//        try {
//            Shop shop = getShop(visitorId, shopId);
//            return shop.validatePurchasePolicy(visitorId, basket);
//        } catch (InnerLogicException e) {
//            return false;
//        }
    }

    public double calculateShopDiscount(int shopId, int visitorId, ShoppingBasket basket) {
        return 0.0;
//        try {
//            Shop shop = getShop(visitorId, shopId);
//            return shop.calculateDiscount(visitorId, basket);
//        } catch (InnerLogicException e) {
//            return 0;
//        }
    }

    public AssignAgreement initAssignShopOwner(int visitorId, String usernameToAssign, int shopID) throws InnerLogicException {
        RoleController.getInstance().verifyIsShopOwner(visitorId, shopID);
        Shop shop = getShop(visitorId, shopID);
        String assignor = VisitorController.getInstance().getVisitorLoggedIn(visitorId).getUsername();
        return shop.initAssignShopOwner(usernameToAssign, assignor);
    }

    public AssignAgreement approveAssignAgreement(int visitorId, String usernameToApprove, int shopID) throws InnerLogicException {
        RoleController.getInstance().verifyIsShopOwner(visitorId, shopID);
        Shop shop = getShop(visitorId, shopID);
        String approver = VisitorController.getInstance().getVisitorLoggedIn(visitorId).getUsername();
        return shop.approveAssignAgreement(usernameToApprove, approver);
    }

    public List<AssignAgreement> getAllShopAssignAgreements(int visitorId, int shopID) throws InnerLogicException {
        RoleController.getInstance().verifyIsShopOwner(visitorId, shopID);
        Shop shop = getShop(visitorId, shopID);
        return shop.getAllAssignAgreementsList();
    }


    public void updateOnShopOwnerRemovals(int visitorId, int shopID, Set<String> removedOwners) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopID);
        shop.updateApprovingCollections(removedOwners);
    }



    //calls the help functions.
    public HashMap<Shop, List<Product>> searchProductByName(int visitorId, String str) {
        return filterProductsByName(visitorId, str, new ArrayList<>(shops.values()));
    }

    public HashMap<Shop, List<Product>> searchProductByKeyWord(int visitorId, String str) {
        return filterProductsByKeyWord(visitorId, str, new ArrayList<>(shops.values()));
    }

    public HashMap<Shop, List<Product>> searchProductByCategory(int visitorId, String str) {
        return filterProductsByCategory(visitorId, str, new ArrayList<>(shops.values()));
    }

    /**
     * @param str   keyword to search with
     * @param shops shops to search
     * @return products with the prefix of the keyword
     */
    public HashMap<Shop, List<Product>> filterProductsByKeyWord(int visitorId, String str, List<Shop> shops) {

        HashMap<Shop, List<Product>> outPut = new HashMap<>();
        for (Shop s : shops) {
            //check if shop is open
            if(s.isOpen()) {
                List<Product> shopProducts = new LinkedList<>();
                Map<Integer, Product> products = s.getProducts();
                for (Product p : products.values()) {
                    if (p.getProductName().startsWith(str)) {
                        shopProducts.add(p);
                    }
                }
                if (!shopProducts.isEmpty()) {
                    outPut.put(s, shopProducts);
                }
            }
        }
        return outPut;
    }

    /**
     * @param str   a product category
     * @param shops the shops in the marken
     * @return hash map with all products and there shop
     */
    public HashMap<Shop, List<Product>> filterProductsByCategory(int visitorId, String str, List<Shop> shops) {
        return filterHelper2(str, shops);
    }

    /**
     * @param str   name of a product to search
     * @param shops list of a shops to search
     * @return the product with the imput names
     */
    public HashMap<Shop, List<Product>> filterProductsByName(int visitorId, String str, List<Shop> shops) {
        return filterHelper(str, shops);
    }

    public void setShops(HashMap<Integer, Shop> shops) {
        this.shops = shops;
        //nextShopId.set(shops.keySet().stream().max(Integer::compareTo).orElse(0));
    }

    /**
     * @param visitorId
     * @param shopId      shop id to add
     * @param productName product name
     * @param price       product price
     * @param description prudict description
     * @param quantity    product quantity
     * @return the product that was added
     */
    public Product addProductToShop(int visitorId, int shopId, String productName, double price, String description, int quantity, String category) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        //open database connection
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();

        Product p = getShop(visitorId, shopId).addProduct(productName, price, description, quantity, category,dbConnection);
        //commit transaction and close db;

        dbConnection.commitTransaction();
        dbConnection.closeConnections();
        return p;
    }


    /**
     * @param shopId    the shop unique id
     * @param productId the product unique id
     * @return the product that was removed
     */
    public Product removeProductFromShop(int visitorId, int shopId, int productId) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).removeProduct(productId);
    }

    /**
     * @param shopId    the shop id
     * @param productId the items to add
     */
    public void getProductFromShop(int visitorId, int shopId, int productId) throws InnerLogicException {
        getShop(visitorId, shopId).getProduct(productId);
    }
    public Shop getShop(int visitorId, int id) throws InnerLogicException {
        Shop s = getShop(id);
        if (!s.isOpen()) { //if shop is close and you are not the owner
            try{
                Visitor visitor = VisitorController.getInstance().getVisitor(visitorId);
                if (visitor.getLoggedIn().getUsername().equals(s.getFounder())){
                    return s;
                }
            }
            catch (InnerLogicException e){
                throw new InnerLogicException("tried to get a closed shop");
            }
        }

        return s;
    }

    public Shop getShop(int shopId) throws InnerLogicException {
        if (!isExistShop(shopId)) {
            throw new InnerLogicException("shop with id: " + shopId + "does not exists");
        }
        return shops.get(shopId);
    }
    private boolean isExistShop(int shopID){
        Shop s;
        if (shops.containsKey(shopID)){
            return true;
        } else if ((s = (Shop) PersistenceManager.find(Shop.class,shopID))!=null) {
            shops.put(shopID,s);
            return true;
        }
        return false;
    }
//    public List<Shop> getShops(List<Integer> toBring) throws InnerLogicException {
//        DBConnection dbConnection = beginTransaction();
//        List<Shop> outPut = new LinkedList<>();
//        for (int shopId: toBring){
//            Shop s = shops.get(shopId);
//            outPut.add((s == null)?(Shop) dbConnection.find(Shop.class, shopId):s);
//        }
//        commitTransaction(dbConnection);
//        return outPut;
//    }


    public Product changeProductDescription(int visitorId, int shopId, int productId, String newDescription) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId,shopId).changeProductDescription(productId,newDescription);
    }

    public Product changeProductName(int visitorId, int shopId, int productId, String newName) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId,shopId).changeProductName(productId,newName);
    }

    public Product changeProductPrice(int visitorId, int shopId, int productId, double newPrice) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId,shopId).changeProductPrice(productId,newPrice);
    }

    public Product reduceProductQuantity(int visitorId, int shopId, int productId, int reduceBe) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).reduceQuantity(productId, reduceBe);
    }

    public Product addProductQuantity(int visitorId, int shopId, int productId, int addBe) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).addQuantity(productId, addBe);
    }

    private HashMap<Shop, List<Product>> filterHelper(Object toCmp, List<Shop> shops) {
        HashMap<Shop, List<Product>> outPut = new HashMap<>();
        for (Shop s : shops) {
            if (s.isOpen()) {
                List<Product> shopProducts = new LinkedList<>();
                Map<Integer, Product> products = s.getProducts();
                for (Product p : products.values()) {
                    if (p.getProductName().equals(toCmp)) {
                        shopProducts.add(p);
                    }
                }
                if (!shopProducts.isEmpty()) {
                    outPut.put(s, shopProducts);
                }
            }
        }
        return outPut;
    }

    private HashMap<Shop, List<Product>> filterHelper2(Object toCmp, List<Shop> shops) {
        HashMap<Shop, List<Product>> outPut = new HashMap<>();
        for (Shop s : shops) {
            if (s.isOpen()) {
                List<Product> shopProducts = new LinkedList<>();
                Map<Integer, Product> products = s.getProducts();
                for (Product p : products.values()) {
                    if (p.getCategory().equals(toCmp)) {
                        shopProducts.add(p);
                    }
                }
                if (!shopProducts.isEmpty()) {
                    outPut.put(s, shopProducts);
                }
            }
        }
        return outPut;
    }

    //for tests
    public void clear() {
        shops = new HashMap<>();
    }

    public void set(HashMap<Integer, Shop> shops) {
        this.shops = shops;
    }

    //TODO lock check in persistence
    public void acquireShopRoleAssign(int shopId) {
        if (shops.containsKey(shopId)) {
            Shop shop = shops.get(shopId);
            shop.acquireRoleAssign();
        }
    }


    // this function acquire the shop with shopId lock
    // most use releaseShopRoleAssign in the end of the critical section!

    public void releaseShopRoleAssign(int shopId) {
        if (shops.containsKey(shopId)) {
            Shop shop = shops.get(shopId);
            shop.releaseRoleAssign();
        }
    }

    public SingleProductDiscount addProductDiscount(int visitorId, int shopId, double percentage, Date lastValidDate, int productId) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addProductDiscount(percentage, lastValidDate, productId);
    }

    public CategoryDiscount addCategoryDiscount(int visitorId, int shopId, double percentage, Date lastValidDate, String category) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addCategoryDiscount(percentage, lastValidDate, category);
    }

    public TotalShopDiscount addTotalShopDiscount(int visitorId, int shopId, double percentage, Date lastValidDate) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addTotalShopDiscount(percentage, lastValidDate);
    }

    public XorDiscount addXorDiscount(int visitorId, int shopId, int discountAId, int discountBId) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addXorDiscount(discountAId, discountBId);
    }

    // PQ = Product Quantity
    public Discount addPQCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addPQCondition(type, discountId, productId, minProductQuantity);
    }

    // TBP = Total Basket Price
    public Discount addTBPCondition(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addTBPCondition(type, discountId, minBasketPrice);
    }

    public PurchasePolicy addAtMostFromProductPolicy(int visitorId, int shopId, int productId, int maxQuantity) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addAtMostFromProductPolicy(productId, maxQuantity);
    }

    public PurchasePolicy addAtLeastFromProductPolicy(int visitorId, int shopId, int productId, int minQuantity) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.addAtLeastFromProductPolicy(productId, minQuantity);
    }

    public PurchasePolicy composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.composePurchasePolicies(type, policyId1, policyId2);
    }

    public List<Shop> getListOfShops() {
        List<Shop> outPut = new LinkedList<>();
        for (Shop s:shops.values()){
            if(s.isOpen()) {
                outPut.add(s);
            }
        }
        return outPut;
    }

    public List<Shop> getListOfShops(int visitorId, Set<Integer> managedShops) {
        List<Shop> outPut = new LinkedList<>();
        for (Integer shopId : managedShops){
            Shop s = null;
            try{
                s = getShop(visitorId, shopId);
            } catch (InnerLogicException e) {
                //do nothing
            }
            if(s != null){
                outPut.add(s);
            }
        }
        return outPut;
    }

    public List<Discount> getDiscountsPolicies(Integer visitorId, Integer shopId) throws InnerLogicException {
        return getShop(visitorId,shopId).getDiscountPolicies();
    }

    public List<PurchasePolicy> getPurchasesPolicies(Integer visitorId, Integer shopId) throws InnerLogicException {
        return getShop(visitorId,shopId).getPurchasePolicies();
    }

    public Bid approveBid(String approverUserName, int shopId, int bidId) throws InnerLogicException {
        Shop shop = shops.get(shopId);
        if(shop == null) throw new InnerLogicException("shop with id " + shopId + " is not exist");
        return shop.approveBid(approverUserName, bidId);
    }

    public Bid incrementBidPrice(int shopId, int bidId, String approverUserName, double newPrice) throws InnerLogicException {
        Shop shop = shops.get(shopId);
        if(shop == null) throw new InnerLogicException("shop with id " + shopId + " is not exist");
        return shop.incrementBidPrice(bidId, approverUserName, newPrice);
    }

    public Product verifyProductExist(int shopID, int productID) throws InnerLogicException {
        return getShop(shopID).getProduct(productID);
    }

    public List<Bid> getAllShopBid(int visitorId, int shopId) throws InnerLogicException {
        Member loggedIn = VisitorController.getInstance().getVisitorLoggedIn(visitorId);
        Shop shop = getShop(visitorId, shopId);
        return shop.getAllBid(loggedIn);
    }




    private static class ShopControllerHolder {
        private static final ShopController instance = new ShopController();
    }
    public Shop getShopWithConnection(int shopId, DBConnection conn) throws InnerLogicException {
        Shop ourPut = shops.containsKey(shopId)?shops.get(shopId):(Shop) conn.find(Shop.class,shopId);
        if(ourPut == null){
            throw new InnerLogicException("shop does now exists");
        }
        return ourPut;
    }
}
