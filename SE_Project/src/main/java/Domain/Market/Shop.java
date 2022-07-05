package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import Domain.Market.Conditions.ConditionFactory;
import Domain.Market.Conditions.ICondition;
import Domain.Market.DiscountPolicies.*;
import Domain.Market.Notifications.IShopObserver;
import Domain.Market.Notifications.ShopNotification;
import Domain.Market.PurchasePolicies.PurchasePolicy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Shop {

    private final int shopId;
    private final String Founder;           // Founder username
    private final int FounderID;
    private final String FounderPhone;
    private final String FounderCreditCard; //TODO: hashed
    private boolean isOpen;
    private String name;
    private String description;
    private String location;

    private final Map<Integer, Discount> discountsPolicy;
    private final AtomicInteger nextDiscountId;

    private final Map<Integer, PurchasePolicy> purchasePolicy;
    private final AtomicInteger nextPurchasePolicyId;

    private Map<Integer,Product> products;
    private AtomicInteger nextProductId;
    private boolean roleAssignLock;

    private List<IShopObserver> shopObservers;


    public Shop(int id){

        this.shopId = id;
        products = new ConcurrentHashMap<>();
        nextProductId = new AtomicInteger(0);
        Founder = "";
        FounderID = 0;
        FounderCreditCard = "";
        FounderPhone = "";
        isOpen = true;
        discountsPolicy = new HashMap<>();
        nextDiscountId = new AtomicInteger(0);
        purchasePolicy = new HashMap<>();
        nextPurchasePolicyId = new AtomicInteger(0);
        this.shopObservers = new LinkedList<>();

    }


    public Shop(String memberUserName, int memberID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation, int shopID) {
        shopId = shopID;
        Founder = memberUserName;
        FounderID = memberID;
        FounderPhone = memberPhone;
        FounderCreditCard = creditCard;
        name = shopName;
        description = shopDescription;
        location = shopLocation;
        products = new ConcurrentHashMap<>();
        nextProductId = new AtomicInteger(0);
        isOpen = true;
        discountsPolicy = new HashMap<>();
        nextDiscountId = new AtomicInteger(0);
        purchasePolicy = new HashMap<>();
        nextPurchasePolicyId = new AtomicInteger(0);
        this.shopObservers = new LinkedList<>();
    }

    public boolean isOpen(){
        return isOpen;
    }
    private int getNextProductId(){
       return nextProductId.incrementAndGet();
    }
    public int getShopId() {
        return shopId;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public String getName(){
        return name;
    }

    /**
     *

     * @param productName item name
     * @param price the item price in double
     * @param description the description of the item
     * @param quantity the quantity of the item
     * @return
     */
    public Product addProduct(String productName, double price, String description, int quantity,String category) throws InnerLogicException {
        if(products.values().stream().anyMatch(product -> product.getProductName().equals(productName))){
            throw new InnerLogicException("product with the same name already exist");
        }
        int currId = getNextProductId();
        Product p = new Product(currId,productName,price,description,quantity,category);
        products.put(currId, p);
        return p;
    }

    /**
     *
     * @param id the product id
     * @param toReduce a positive number to reduce
     * @return the product with the new quantity
     */
    public Product reduceQuantity(int id, int toReduce) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.addToQuantity(-1*toReduce);
        p.release();
        return p;
    }

    /**
     *
     * @param id the product id
     * @param toAdd the number of items to add
     * @return return the new product obj
     */
    public Product addQuantity(int id, int toAdd) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.addToQuantity(toAdd);
        p.release();
        return p;
    }

    /**
     *
     * @param id the item unique number
     * @return the removed product
     */
    public Product removeProduct(int id) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.setQuantity(0); // IMPORTANT! so purchase won't be able to buy the product
        p.release();
        return products.remove(id);
    }

    /**
     *
     * @param id the item unique number
     * @param newName the new product name
     * @return the product with the new name
     */
    public Product changeProductName(int id, String newName) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.setProductName(newName);
        products.put(id,p);
        p.release();
        return p;
    }

    /**
     *
     * @param id the item unique number
     * @param newDescription the new product description
     * @return the product with the new description
     */
    public Product changeProductDescription(int id, String newDescription) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.setDescription(newDescription);
        products.put(id,p);
        p.release();
        return p;
    }

    /**
     *
     * @param id the item unique number
     * @param newPrice the new product price
     * @return the product with the new price
     */
    public Product changeProductPrice(int id, double newPrice) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.setPrice(newPrice);
        products.put(id,p);
        p.release();
        return p;
    }

    /**
     *
     * @param id the item unique number
     * @return the quantity of the product left in the shop
     */
    public int getQuantity(int id) throws InnerLogicException {
        return getProduct(id).getQuantity();
    }

    /**
     *
     * @param id the item unique number
     * @return the product be id, if there is no product for this id exception is thrown
     */
    public Product getProduct(int id) throws InnerLogicException {
        if (!products.containsKey(id)){
            throw new InnerLogicException("the items with id: "+ id+" not exists");
        }
        return products.get(id);
    }
    public void set(HashMap<Integer,Product> products){
        this.products = products;
        this.nextProductId = new AtomicInteger(products.keySet().stream().max(Integer::compareTo).orElse(0));
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }

    public void setNextProductId(int nextProductId) {
        this.nextProductId.set(nextProductId);
    }

    public void clear(){
        this.products = new HashMap<>();
    }

    public boolean containsProduct(int id){
        return products.containsKey(id);
    }


    public String getFounder() {
        return Founder;
    }

    public int getFounderID() {
        return FounderID;
    }

    public String getPhone() {
        return FounderPhone;
    }

    public String getCreditCard() {
        return FounderCreditCard;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void close() {
        isOpen = false;
        notifyShopObservers("shop " + name + " id " + shopId + " is now close");
    }

    public void open() {
        isOpen = true;
        notifyShopObservers("shop " + name + " id " + shopId + " is now reopen");
    }

    public synchronized void acquireRoleAssign() {
        while (roleAssignLock) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        roleAssignLock = true;
    }

    public synchronized void releaseRoleAssign() {
        roleAssignLock = false;
        notifyAll();
    }

    //***********purchase policy***********
    public boolean validatePurchasePolicy(ShoppingBasket basket) {
        for (PurchasePolicy policy : purchasePolicy.values()){
            if (!policy.checkPolicy(basket))
                return false;
        }

        return true;
    }

    public PurchasePolicy addAtMostFromProductPolicy(int productId, int maxQuantity) throws InnerLogicException {
        Product product = getProduct(productId);
        int purchasePolicyId = nextPurchasePolicyId.getAndIncrement();
        ICondition condition = ConditionFactory.buildAtMostProductQuantityCondition(product, maxQuantity);
        PurchasePolicy policy = new PurchasePolicy(purchasePolicyId, condition);
        purchasePolicy.put(purchasePolicyId, policy);
        return policy;
    }

    public PurchasePolicy addAtLeastFromProductPolicy(int productId, int maxQuantity) throws InnerLogicException {
        Product product = getProduct(productId);
        int purchasePolicyId = nextPurchasePolicyId.getAndIncrement();
        ICondition condition = ConditionFactory.buildAtLeastProductQuantityCondition(product, maxQuantity);
        PurchasePolicy policy = new PurchasePolicy(purchasePolicyId, condition);
        purchasePolicy.put(purchasePolicyId, policy);
        return policy;
    }

    public PurchasePolicy composePurchasePolicies(CONDITION_COMPOSE_TYPE type, int policyId1 , int policyId2) throws InnerLogicException {
        PurchasePolicy policy1 = purchasePolicy.remove(policyId1);
        if (policy1 == null){
            throw new InnerLogicException("couldn't find purchase policy with id: " + policyId1);
        }
        PurchasePolicy policy2 = purchasePolicy.remove(policyId2);
        if (policy2 == null){
            purchasePolicy.put(policyId1, policy1); // undo remove
            throw new InnerLogicException("couldn't find purchase policy with id: " + policyId2);
        }
        ICondition composedPolicy = ConditionFactory.getComposedCondition(type, policy1.getCondition(), policy2.getCondition());
        int newPolicyId = nextPurchasePolicyId.getAndIncrement();
        PurchasePolicy newPolicy = new PurchasePolicy(newPolicyId, composedPolicy);
        purchasePolicy.put(newPolicyId, newPolicy);
        return newPolicy;
    }


    //***********discount policy***********
    public void applyDiscounts(ShoppingBasket shoppingBasket) {
        for (Discount discount: discountsPolicy.values()) {
            discount.applyDiscount(shoppingBasket);
        }
    }

    public SingleProductDiscount addProductDiscount(double percentage, Date lastValidDate, int productId) throws InnerLogicException {
        Product product = getProduct(productId);
        int discountId = nextDiscountId.getAndIncrement();
        SingleProductDiscount singleProductDiscount = new SingleProductDiscount(discountId ,percentage, lastValidDate, product);
        discountsPolicy.put(discountId, singleProductDiscount);
        return singleProductDiscount;
    }

    public CategoryDiscount addCategoryDiscount(double percentage, Date lastValidDate, String category) throws InnerLogicException {
        int discountId = nextDiscountId.getAndIncrement();
        CategoryDiscount categoryDiscount = new CategoryDiscount(discountId ,percentage, lastValidDate, category);
        discountsPolicy.put(discountId, categoryDiscount);
        return categoryDiscount;
    }

    public TotalShopDiscount addTotalShopDiscount(double percentage, Date lastValidDate) throws InnerLogicException {
        int discountId = nextDiscountId.getAndIncrement();
        TotalShopDiscount totalShopDiscount = new TotalShopDiscount(discountId ,percentage, lastValidDate);
        discountsPolicy.put(discountId, totalShopDiscount);
        return totalShopDiscount;
    }

    public XorDiscount addXorDiscount(int discountAId, int discountBId) throws InnerLogicException {
        Discount discountA = getDiscount(discountAId);
        Discount discountB = getDiscount(discountBId);
        int discountId = nextDiscountId.getAndIncrement();
        XorDiscount xorDiscount = new XorDiscount(discountId, discountA, discountB);
        discountsPolicy.remove(discountAId);
        discountsPolicy.remove(discountBId);
        discountsPolicy.put(discountId, xorDiscount);
        return xorDiscount;
    }

    // TBP = Total Basket Price
    public Discount addTBPCondition(CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) throws InnerLogicException {
        ICondition condition = ConditionFactory.buildTotalBasketPriceCondition(minBasketPrice);
        Discount discount = getDiscount(discountId);
        discount.addCompositeCondition(type, condition);
        return discount;
    }

    // PQ = Product Quantity
    public Discount addPQCondition(CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) throws InnerLogicException {
        Product product = getProduct(productId);
        ICondition condition = ConditionFactory.buildAtLeastProductQuantityCondition(product, minProductQuantity);
        Discount discount = getDiscount(discountId);
        discount.addCompositeCondition(type, condition);
        return discount;
    }

    private Discount getDiscount(int discountId) throws InnerLogicException {
        if (!discountsPolicy.containsKey(discountId)){
            throw new InnerLogicException("no such discount with id: " + discountId);
        }
        return discountsPolicy.get(discountId);
    }


    //***********notifications***********

    public void notifyShopObservers(String message){
        ShopNotification shopNotification = new ShopNotification(shopId, message);
        for(IShopObserver observer: shopObservers){
            observer.sendNotification(shopNotification);
        }
    }

    public void registerToShopOwnerNotification(IShopObserver shopObserver){
        shopObservers.add(shopObserver);
    }




}
