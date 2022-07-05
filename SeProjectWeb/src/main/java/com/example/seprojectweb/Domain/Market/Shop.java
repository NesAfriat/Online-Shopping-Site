package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Conditions.CONDITION_COMPOSE_TYPE;
import com.example.seprojectweb.Domain.Market.Conditions.ConditionFactory;
import com.example.seprojectweb.Domain.Market.Conditions.Condition;
import com.example.seprojectweb.Domain.Market.DiscountPolicies.*;
import com.example.seprojectweb.Domain.Market.Notifications.IShopObserver;
import com.example.seprojectweb.Domain.Market.Notifications.ShopNotification;
import com.example.seprojectweb.Domain.Market.PurchasePolicies.PurchasePolicy;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;
import com.example.seprojectweb.Domain.PersistenceManager;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@Entity
@NamedQuery(name="Shop.findAll", query="select s FROM Shop s")
public class Shop {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int shopId;




    private String Founder;           // Founder username
    private int FounderID;
    private String FounderPhone;
    private String AccountToDeposit; //TODO: hashed





    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<Integer, Discount> discountsPolicy;
    //private AtomicInteger nextDiscountId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<Integer, PurchasePolicy> purchasePolicy;
    private boolean isOpen;
    private String name;
    private String description;
    private String location;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Map<String, AssignAgreement> assignAgreementMap;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    private Map<Integer,Bid> activeBids;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<Integer, Product> products;

    @Transient
    private boolean roleAssignLock;

    @ManyToMany(targetEntity = Member.class, fetch = FetchType.EAGER)
    private List<IShopObserver> shopObservers;

//    @ManyToMany(targetEntity = Bid.class, fetch = FetchType.EAGER)



    public Shop(int id) {

        this.shopId = id;
        products = new ConcurrentHashMap<>();
        Founder = "";
        FounderID = 0;
        AccountToDeposit = "";
        FounderPhone = "";
        isOpen = true;
        discountsPolicy = new HashMap<>();
        //nextDiscountId = new AtomicInteger(0);
        purchasePolicy = new HashMap<>();
        this.shopObservers = new LinkedList<>();
        this.activeBids = new ConcurrentHashMap<>();
        assignAgreementMap = new ConcurrentHashMap<>();

    }


    public Shop(Member founder, int memberID, String memberPhone, String creditCard, String shopName, String shopDescription, String shopLocation) {
        //shopId = shopID;
        Founder = founder.getUsername();
        FounderID = memberID;
        FounderPhone = memberPhone;
        AccountToDeposit = creditCard;
        name = shopName;
        description = shopDescription;
        location = shopLocation;
        products = new ConcurrentHashMap<>();
        isOpen = true;
        discountsPolicy = new HashMap<>();
        //nextDiscountId = new AtomicInteger(0);
        purchasePolicy = new HashMap<>();
        this.shopObservers = new LinkedList<>();
        shopObservers.add(founder);
        this.activeBids = new ConcurrentHashMap<>();
        assignAgreementMap = new ConcurrentHashMap<>();
    }

    public Shop() {
        roleAssignLock = false;
        this.shopObservers = new LinkedList<>();
        assignAgreementMap = new ConcurrentHashMap<>();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getShopId() {
        return shopId;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    /**
     * @param productName item name
     * @param price       the item price in double
     * @param description the description of the item
     * @param quantity    the quantity of the item
     * @return
     */
    public Product addProduct(String productName, double price, String description, int quantity, String category, DBConnection dbConnection) throws InnerLogicException {
        if (products.values().stream().anyMatch(product -> product.getProductName().equals(productName))) {
            throw new InnerLogicException("product with the same name already exist");
        }

        Product p = new Product(productName, price, description, quantity, category);
        //remove item if the operation failed
        dbConnection.addFailure(()->products.remove(p.getId()));
        dbConnection.persist(p);
        products.put(p.getId(), p);
        dbConnection.update(this);
        return p;
    }



    /**
     * @param id       the product id
     * @param toReduce a positive number to reduce
     * @return the product with the new quantity
     */
    public Product reduceQuantity(int id, int toReduce) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        p.addToQuantity(-1 * toReduce);
        p.release();
        return p;
    }

    /**
     * @param id    the product id
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
     * @param id the item unique number
     * @return the removed product
     */
    public Product removeProduct(int id) throws InnerLogicException {

        Product p = getProduct(id);
        p.acquire();

        DBConnection dbConnection = beginTransaction();

        int oldQuantity = p.getQuantity();



        dbConnection.addFailure(()->p.setQuantityWithOutCheck(oldQuantity));
        p.setQuantity(0);
        dbConnection.updateSession(p);

        Product output = products.remove(id);
        //if fail while commit
        dbConnection.addFailure(()->products.put(output.getId(),output));

        commitTransaction(dbConnection);
        ; // IMPORTANT! so purchase won't be able to buy the product
        p.release();


        //PersistenceManager.update(output);
        return output;
    }

    /**
     * @param id      the item unique number
     * @param newName the new product name
     * @return the product with the new name
     */
    public Product changeProductName(int id, String newName) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        DBConnection dbConnection = beginTransaction();


        String oldName = p.getProductName();

        dbConnection.addFailure(()->p.setProductName(oldName));
        p.setProductName(newName);

        dbConnection.update(p);
        commitTransaction(dbConnection);
        p.release();
        return p;
    }

    /**
     * @param id             the item unique number
     * @param newDescription the new product description
     * @return the product with the new description
     */
    public Product changeProductDescription(int id, String newDescription) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        DBConnection dbConnection = beginTransaction();


        String oldDes = p.getDescription();

        dbConnection.addFailure(()->p.setDescription(oldDes));
        p.setDescription(newDescription);
        dbConnection.update(p);
        commitTransaction(dbConnection);
        p.release();
        return p;
    }

    /**
     * @param id       the item unique number
     * @param newPrice the new product price
     * @return the product with the new price
     */
    public Product changeProductPrice(int id, double newPrice) throws InnerLogicException {
        Product p = getProduct(id);
        p.acquire();
        DBConnection dbConnection = beginTransaction();

        //old info
        double oldPrice = p.getPrice();
        //roll back on failure
        dbConnection.addFailure(()->p.setPrice(oldPrice));
        //new info
        p.setPrice(newPrice);
        dbConnection.update(p);
        commitTransaction(dbConnection);
        p.release();
        return p;
    }

    //**************************assign shop owner*********************

    public AssignAgreement initAssignShopOwner(String usernameToAssign, String assignor) throws InnerLogicException {
        if (!VisitorController.getInstance().isMemberExists(usernameToAssign)){
            throw new InnerLogicException("there is no such member: " + usernameToAssign);
        }

        if (assignAgreementMap.containsKey(usernameToAssign)){
            throw new InnerLogicException("an assignment agreement already exists for " + usernameToAssign);
        }

        AssignAgreement assignAgreement = new AssignAgreement(shopId, usernameToAssign, assignor);

        assignAgreementMap.put(usernameToAssign, assignAgreement);
        //PersistenceManager.persist(assignAgreement);
        //PersistenceManager.update(this);
        notifyShopObservers("New Assign Agreement created for " + usernameToAssign + " in shop: " + shopId);
        approveAssignAgreement(usernameToAssign, assignor);
        return assignAgreement;
    }

    public AssignAgreement approveAssignAgreement(String usernameToApprove, String approver) throws InnerLogicException {
        if (!assignAgreementMap.containsKey(usernameToApprove)){
            throw new InnerLogicException("an assignment agreement doesn't exists for " + usernameToApprove);
        }
        AssignAgreement assignAgreement = assignAgreementMap.get(usernameToApprove);
        assignAgreement.approveAssign(approver);
        InnerLogicException innerLogicException = null;
        if (assignAgreement.isAssignApproved()){
            assignAgreementMap.remove(usernameToApprove);
            try{
                RoleController.getInstance().assignShopOwnerAfterApproves(shopId, usernameToApprove, assignAgreement.getInitiatorAssignor());
            }
            catch (InnerLogicException e){
                innerLogicException = e;
            }

        }

        PersistenceManager.update(this);
        if (innerLogicException != null){
            throw innerLogicException;
        }
        return assignAgreement;

    }

    public void updateApprovingCollections(Set<String> removedOwners) throws InnerLogicException {
        Set<AssignAgreement> toRemoveAgreements = new HashSet<>();
        for (AssignAgreement assignAgreement : assignAgreementMap.values()){
            if (removedOwners.contains(assignAgreement.getInitiatorAssignor())){
                toRemoveAgreements.add(assignAgreement);
            }
            else if (assignAgreement.isAssignApproved()){
                RoleController.getInstance().assignShopOwnerAfterApproves(shopId, assignAgreement.getToAssignMember(), assignAgreement.getInitiatorAssignor());
                toRemoveAgreements.add(assignAgreement);
            }
        }

        for (AssignAgreement assigneeToRemove : toRemoveAgreements){
            assignAgreementMap.remove(assigneeToRemove.getToAssignMember());
        }

        Set<IShopObserver> toRemoveObservers = new HashSet<>();
        for (IShopObserver shopObserver : shopObservers){
            if (removedOwners.contains(shopObserver.getUsername())){
                toRemoveObservers.add(shopObserver);
            }
        }
        for (IShopObserver shopObserver : toRemoveObservers){
            shopObservers.remove(shopObserver);
        }

        PersistenceManager.update(this);


    }

    //**************************bid purchase**************************

    public Bid bidProduct(IShopObserver bidder, int productId, int quantity, double price) throws InnerLogicException {

        Product product = products.get(productId);
        if(product == null){
            throw new InnerLogicException("shop " + shopId + " dont have product with id " + productId);
        }
        if(product.getPrice() < price){
            throw new InnerLogicException("member tried to bid with price that is higher that the normal product price");
        }
        Bid bid = new Bid(bidder, shopId,product , quantity, price);
        PersistenceManager.persist(bid);
        activeBids.put(bid.getId(),bid);
        PersistenceManager.update(this);

        return bid;
    }

    private boolean isBidApprover(Role role) {
        if(role instanceof ShopOwner) return true;
        if(role instanceof ShopOwner){
            ShopManager manager = (ShopManager) role;
            try{
                manager.isPermissionExists(PermissionManagement.BidApprover);
            } catch (InnerLogicException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Bid approveBid(String approverUserName, int bidId) throws InnerLogicException {
        Bid bid = activeBids.get(bidId);
        if(bid == null) throw new InnerLogicException("shop with id " + shopId + "dont have bid with id " + bidId);
        bid.approveBid(approverUserName);
        PersistenceManager.update(bid);
        return bid;

    }

    public Bid incrementBidPrice(int bidId, String approverUserName, double newPrice) throws InnerLogicException {
        Bid bid = activeBids.get(bidId);
        if(bid == null) throw new InnerLogicException("shop with id " + shopId + "dont have bid with id " + bidId);
        bid.incrementBidPrice(approverUserName, newPrice);
        return bid;
    }

    public List<Bid> getAllBid(Member loggedIn) throws InnerLogicException {
        Role role = RoleController.getInstance().getAllShopRoles(shopId).get(loggedIn.getUsername());
        if(role instanceof ShopOwner) return new LinkedList<>(activeBids.values());
        ShopManager shopManager = (ShopManager) role;
        shopManager.isPermissionExists(PermissionManagement.BidApprover);//throws exception if not bid permitted
        return new LinkedList<>(activeBids.values());

    }

    //****************************************************************

    public Map<Integer, Bid> getActiveBids() {
        return activeBids;
    }

    public void setActiveBids(Map<Integer, Bid> activeBids) {
        this.activeBids = activeBids;
    }

    public boolean isRoleAssignLock() {
        return roleAssignLock;
    }

    public void setRoleAssignLock(boolean roleAssignLock) {
        this.roleAssignLock = roleAssignLock;
    }

    /**
     * @param id the item unique number
     * @return the quantity of the product left in the shop
     */
    public int getQuantity(int id) throws InnerLogicException {
        return getProduct(id).getQuantity();
    }

    /**
     * @param id the item unique number
     * @return the product be id, if there is no product for this id exception is thrown
     */
    public Product getProduct(int id) throws InnerLogicException {
        if (!products.containsKey(id)) {
            throw new InnerLogicException("the items with id: " + id + " not exists");
        }
        return products.get(id);
    }

    public void insertPruducts(HashMap<Integer, Product> products) {
        this.products = products;
//        this.nextProductId = new AtomicInteger(products.keySet().stream().max(Integer::compareTo).orElse(0));
    }

    public void clear() {
        this.products = new HashMap<>();
    }

    public boolean containsProduct(int id) {
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
        return AccountToDeposit;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void close() throws InnerLogicException {
        isOpen = false;
        PersistenceManager.update(this);
        notifyShopObservers("shop " + name + " id " + shopId + " is now close");
    }

    public void open() throws InnerLogicException {
        isOpen = true;
        PersistenceManager.update(this);
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
    public String validatePurchasePolicy(ShoppingBasket basket) {
        for (PurchasePolicy policy : purchasePolicy.values()) {
            if (!policy.checkPolicy(basket)){
                return policy.getCondition().getDescription();
            }
        }

        return null;
    }

    public PurchasePolicy addAtMostFromProductPolicy(int productId, int maxQuantity) throws InnerLogicException {
        Product product = getProduct(productId);
        //int purchasePolicyId = nextPurchasePolicyId.getAndIncrement();
        Condition condition = ConditionFactory.buildAtMostProductQuantityCondition(product, maxQuantity);

        PurchasePolicy policy = new PurchasePolicy(condition);
        PersistenceManager.persist(policy);
        purchasePolicy.put(policy.getId(), policy);
        PersistenceManager.update(this);

        return policy;
    }

    public PurchasePolicy addAtLeastFromProductPolicy(int productId, int maxQuantity) throws InnerLogicException {
        Product product = getProduct(productId);
        //int purchasePolicyId = nextPurchasePolicyId.getAndIncrement();
        Condition condition = ConditionFactory.buildAtLeastProductQuantityCondition(product, maxQuantity);

        PurchasePolicy policy = new PurchasePolicy(condition);
        PersistenceManager.persist(policy);
        purchasePolicy.put(policy.getId(), policy);
        PersistenceManager.update(this);

        return policy;
    }

    public PurchasePolicy composePurchasePolicies(CONDITION_COMPOSE_TYPE type, int policyId1, int policyId2) throws InnerLogicException {
        PurchasePolicy policy1 = purchasePolicy.remove(policyId1);
        if (policy1 == null) {
            throw new InnerLogicException("couldn't find purchase policy with id: " + policyId1);
        }
        PurchasePolicy policy2 = purchasePolicy.remove(policyId2);
        if (policy2 == null) {
            purchasePolicy.put(policyId1, policy1); // undo remove
            throw new InnerLogicException("couldn't find purchase policy with id: " + policyId2);
        }

        Condition composedCondition = ConditionFactory.getComposedCondition(type, policy1.getCondition(), policy2.getCondition());
        //int newPolicyId = nextPurchasePolicyId.getAndIncrement();
        PurchasePolicy newPolicy = new PurchasePolicy(composedCondition);



        PersistenceManager.persist(newPolicy);

        purchasePolicy.put(newPolicy.getId(), newPolicy);
        PersistenceManager.update(this);

        return newPolicy;
    }


    //***********discount policy***********
    public void applyDiscounts(ShoppingBasket shoppingBasket) {
        for (Discount discount : discountsPolicy.values()) {
            discount.applyDiscount(shoppingBasket);
        }
    }

    public SingleProductDiscount addProductDiscount(double percentage, Date lastValidDate, int productId) throws InnerLogicException {
        Product product = getProduct(productId);
        //int discountId = nextDiscountId.getAndIncrement();
        SingleProductDiscount singleProductDiscount = new SingleProductDiscount(percentage, lastValidDate, product);
        PersistenceManager.persist(singleProductDiscount);
        discountsPolicy.put(singleProductDiscount.getId(), singleProductDiscount);
        PersistenceManager.update(this);
        return singleProductDiscount;
    }

    public CategoryDiscount addCategoryDiscount(double percentage, Date lastValidDate, String category) throws InnerLogicException {
        //int discountId = nextDiscountId.getAndIncrement();
        CategoryDiscount categoryDiscount = new CategoryDiscount(percentage, lastValidDate, category);
        PersistenceManager.persist(categoryDiscount);
        discountsPolicy.put(categoryDiscount.getId(), categoryDiscount);
        PersistenceManager.update(this);
        return categoryDiscount;
    }

    public TotalShopDiscount addTotalShopDiscount(double percentage, Date lastValidDate) throws InnerLogicException {
        //int discountId = nextDiscountId.getAndIncrement();
        TotalShopDiscount totalShopDiscount = new TotalShopDiscount(percentage, lastValidDate);
        PersistenceManager.persist(totalShopDiscount);
        discountsPolicy.put(totalShopDiscount.getId(), totalShopDiscount);
        PersistenceManager.update(this);
        return totalShopDiscount;
    }

    public XorDiscount addXorDiscount(int discountAId, int discountBId) throws InnerLogicException {
        Discount discountA = getDiscount(discountAId);
        Discount discountB = getDiscount(discountBId);
        //int discountId = nextDiscountId.getAndIncrement();
        XorDiscount xorDiscount = new XorDiscount(discountA, discountB);
        discountsPolicy.remove(discountAId);
        discountsPolicy.remove(discountBId);
        PersistenceManager.persist(xorDiscount);
        discountsPolicy.put(xorDiscount.getId(), xorDiscount);
        PersistenceManager.update(this);

        //TODO there is a bug - after Xor there is still a condtional left
        // checked with product discount and category discount
        // after the xor - product discount was removed
        // category discount wa
        return xorDiscount;
    }

    // TBP = Total Basket Price
    public Discount addTBPCondition(CONDITION_COMPOSE_TYPE type, int discountId, double minBasketPrice) throws InnerLogicException {
        Condition condition = ConditionFactory.buildTotalBasketPriceCondition(minBasketPrice);
        Discount discount = getDiscount(discountId);
        discount.addCompositeCondition(type, condition);
        return discount;
    }

    // PQ = Product Quantity
    public Discount addPQCondition(CONDITION_COMPOSE_TYPE type, int discountId, int productId, int minProductQuantity) throws InnerLogicException {
        Product product = getProduct(productId);
        Condition condition = ConditionFactory.buildAtLeastProductQuantityCondition(product, minProductQuantity);
        Discount discount = getDiscount(discountId);
        discount.addCompositeCondition(type, condition);
        return discount;
    }

    private Discount getDiscount(int discountId) throws InnerLogicException {
        if (!discountsPolicy.containsKey(discountId)) {
            throw new InnerLogicException("no such discount with id: " + discountId);
        }
        return discountsPolicy.get(discountId);
    }


    //***********notifications***********

    public void notifyShopObservers(String message) throws InnerLogicException {
        for (IShopObserver observer : shopObservers) {
            ShopNotification shopNotification = new ShopNotification(shopId, message);
            //PersistenceManager.persist(shopNotification);
            long id = shopNotification.getId();
            observer.sendNotification(shopNotification);
        }
    }

    public void registerToShopOwnerNotification(Member shopObserver) {
        shopObservers.add(shopObserver);
    }
    public void unregisterToShopOwnerNotification(Member shopObserver){
        shopObservers.remove(shopObserver);
    }


    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setFounder(String founder) {
        Founder = founder;
    }

    public void setFounderID(int founderID) {
        FounderID = founderID;
    }

    public String getFounderPhone() {
        return FounderPhone;
    }

    public void setFounderPhone(String founderPhone) {
        FounderPhone = founderPhone;
    }

    public String getAccountToDeposit() {
        return AccountToDeposit;
    }

    public void setAccountToDeposit(String accountToDeposit) {
        AccountToDeposit = accountToDeposit;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public List<IShopObserver> getShopObservers() {
        return shopObservers;
    }

    public List<Discount> getDiscountPolicies() {
        return discountsPolicy.values().stream().toList();
    }

    public List<PurchasePolicy> getPurchasePolicies() {
        return purchasePolicy.values().stream().toList();

    }



    public Map<String, AssignAgreement> getAssignAgreementMap() {
        return assignAgreementMap;
    }

    public void setAssignAgreementMap(Map<String, AssignAgreement> assignAgreementMap) {
        this.assignAgreementMap = assignAgreementMap;
    }

    public Map<Integer, Discount> getDiscountsPolicy() {
        return discountsPolicy;
    }

    public void setDiscountsPolicy(Map<Integer, Discount> discountsPolicy) {
        this.discountsPolicy = discountsPolicy;
    }

    public Map<Integer, PurchasePolicy> getPurchasePolicy() {
        return purchasePolicy;
    }

    public void setPurchasePolicy(Map<Integer, PurchasePolicy> purchasePolicy) {
        this.purchasePolicy = purchasePolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shop shop)) return false;
        return getShopId() == shop.getShopId() && getName().equals(shop.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShopId(), getName());
    }

    public void setShopObservers(List<IShopObserver> shopObservers) {
        this.shopObservers = shopObservers;
    }
    private DBConnection beginTransaction() throws InnerLogicException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.setConnection();
        dbConnection.beginTransaction();
        return dbConnection;
    }
    private void commitTransaction(DBConnection dbConnection) throws InnerLogicException {
        dbConnection.commitTransaction();
        dbConnection.closeConnections();
    }


    public void notifyShopObserversWithConnection(String message,DBConnection dbConnection) throws InnerLogicException {
        for (IShopObserver observer : shopObservers) {
            ShopNotification shopNotification = new ShopNotification(shopId, message);
            PersistenceManager.persist(shopNotification);
            long id = shopNotification.getId();
            observer.sendNotificationWithConnection(shopNotification,dbConnection);
        }
    }


    public List<AssignAgreement> getAllAssignAgreementsList() {
        return new ArrayList<>(assignAgreementMap.values());
    }
}