package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import Domain.Market.DiscountPolicies.*;
import Domain.Market.PurchasePolicies.PurchasePolicy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopController {
    private Integer nextShopId;
    private Map<Integer, Shop> shops;
    private RoleController roleController;

    private ShopController() {
        shops = new ConcurrentHashMap<>();
        nextShopId = 0;
        roleController = RoleController.getInstance();
    }




    private static class ShopControllerHolder {
        private static final ShopController instance = new ShopController();
    }

    public List<Shop> getListOfShops() {
        return new ArrayList<>(shops.values());
    }

    // payment info - credit card info
    // delivery location -
    // id and phone of founder
    //
    public Shop openShop(int visitorID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) throws InnerLogicException {
        if (shopName==null || isShopExist(shopName)) {
            throw new InnerLogicException("Shop with name: " + shopName + " already exist");
        }
        if(shopName.trim().isEmpty()){
            throw new InnerLogicException("Shop name cannot be empty");
        }
        if(shopDescription==null || shopDescription.trim().isEmpty()){
            throw new InnerLogicException("Shop description cannot be empty");
        }
        if(creditCard==null || creditCard.trim().isEmpty()){
            throw new InnerLogicException("Shop creditCard cannot be empty");
        }
        if(!creditCard.matches("[0-9]*")){
            throw new InnerLogicException("Shop creditCard cannot have letters");
        }
        if(memberPhone==null || !memberPhone.matches("[0-9]*")){
            throw new InnerLogicException("Shop phone cannot have letters");
        }
        if(memberPhone.length()!=10){
            throw new InnerLogicException("Shop phone is not in the right length");
        }
        String memberUserName = roleController.shopOpened(visitorID, nextShopId);
        Shop shop = new Shop(memberUserName, visitorID, memberPhone, creditCard, shopName, shopDescription, shopLocation, nextShopId);
        shops.put(nextShopId, shop);
        nextShopId++;
        return shop;
    }

    private boolean isShopExist(String shopName) {
        return shops.values().stream().anyMatch(s -> s.getName().equals(shopName));
    }

    public Product verifyProductExist(int shopID, int productID) throws InnerLogicException {
        if(!shops.containsKey(shopID)){
            throw new InnerLogicException("Error in add product to cart, shop: "+shopID+" does not exist");
        }
        if(!shops.get(shopID).containsProduct(productID)){
            throw new InnerLogicException("Error...");
        }
        return shops.get(shopID).getProduct(productID);
    }

    public boolean verifyOpenShop(Integer shopID) throws InnerLogicException {
        if(!shops.containsKey(shopID)){
            return false;
        }
        return shops.get(shopID).isOpen();
    }

    public Shop closeShop(int visitId, int shopId) throws InnerLogicException {
        if(!shops.containsKey(shopId)){
            throw new InnerLogicException("no shop with id: "+shopId + " exist");
        }
        String userName = roleController.getUserName(visitId);
        if(!shops.get(shopId).getFounder().equals(userName)){
            throw new InnerLogicException("user: "+ userName+" tried to close a shop - but he isn't the founder");
        }
        if(!shops.get(shopId).isOpen()){
            throw new InnerLogicException("shop is already closed");
        }
        shops.get(shopId).close();
        return shops.get(shopId);
    }

    public Shop reOpenShop(int visitId, int shopId) throws InnerLogicException {
        if(!shops.containsKey(shopId)){
            throw new InnerLogicException("no shop with id: "+shopId + " exist");
        }
        String userName = roleController.getUserName(visitId);
        if(!shops.get(shopId).getFounder().equals(userName)){
            throw new InnerLogicException("user: "+ userName+" tried to close a shop - but he isn't the founder");
        }
        shops.get(shopId).open();
        return shops.get(shopId);
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



    public static ShopController getInstance() {
        return ShopController.ShopControllerHolder.instance;
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
        //TODO need to check if shop is open
        HashMap<Shop, List<Product>> outPut = new HashMap<>();
        for (Shop s : shops) {
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
        return outPut;
    }

    /**
     * @param str   a product category
     * @param shops the shops in the marken
     * @return hash map with all products and there shop
     */
    public HashMap<Shop, List<Product>> filterProductsByCategory(int visitorId, String str, List<Shop> shops) {
        return filterHelper(str, shops);
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
        this.nextShopId = shops.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * @param visitorId
     * @param shopId          shop id to add
     * @param productName product name
     * @param price       product price
     * @param description prudict description
     * @param quantity    product quantity
     * @return the product that was added
     */
    public Product addProductToShop(int visitorId, int shopId, String productName, double price, String description, int quantity, String category) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).addProduct(productName, price, description, quantity, category);
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

    /**
     * @param id shop id
     * @return the shop was requested
     */
    public Shop getShop(int visitorId, int id) throws InnerLogicException {
        if (!shops.containsKey(id)) {
            throw new InnerLogicException("shop with id: " + id + "does not exists");
        }
        String userName = roleController.getUserName(visitorId);
        if(!shops.get(id).isOpen() && !shops.get(id).getFounder().equals(userName)){ //if shop is close and you are not the owner
            throw new InnerLogicException("tried to get a closed shop");
        }
        return shops.get(id);
    }

    public Product changeProductDescription(int visitorId, int shopId, int productId, String newDescription) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).changeProductDescription(productId, newDescription);
    }

    public Product changeProductName(int visitorId, int shopId, int productId, String newName) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).changeProductName(productId, newName);
    }

    public Product changeProductPrice(int visitorId, int shopId, int productId, double newPrice) throws InnerLogicException {
        roleController.verifyIsShopOwner(visitorId, shopId);
        return getShop(visitorId, shopId).changeProductPrice(productId, newPrice);
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

    //for tests
    public void clear() {
        shops = new HashMap<>();
    }

    public void set(HashMap<Integer, Shop> shops) {
        this.shops = shops;
    }


    // this function acquire the shop with shopId lock
    // most use releaseShopRoleAssign in the end of the critical section!

    public void acquireShopRoleAssign(int shopId){
        if(shops.containsKey(shopId)) {
            Shop shop = shops.get(shopId);
            shop.acquireRoleAssign();
        }
    }

    public void releaseShopRoleAssign(int shopId){
        if(shops.containsKey(shopId)) {
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

    public PurchasePolicy composePurchasePolicies(int visitorId, int shopId, CONDITION_COMPOSE_TYPE type, int policyId1 , int policyId2) throws InnerLogicException {
        Shop shop = getShop(visitorId, shopId);
        roleController.verifyIsShopOwner(visitorId, shopId);
        return shop.composePurchasePolicies(type, policyId1, policyId2);
    }

}
