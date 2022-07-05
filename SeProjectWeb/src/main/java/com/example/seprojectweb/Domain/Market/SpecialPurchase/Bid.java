package com.example.seprojectweb.Domain.Market.SpecialPurchase;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.DeliveryAdapter.IDelivey;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.Market.Notifications.BidNotification;
import com.example.seprojectweb.Domain.Market.Notifications.IShopObserver;
import com.example.seprojectweb.Domain.PaymentAdapter.IPayment;
import com.example.seprojectweb.Domain.PersistenceManager;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


@Entity
public class Bid {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Transient
    private IShopObserver bidder;
    private int shopId;


//    @ManyToOne(targetEntity = Member.class)
//    private IShopObserver bidder;
    private String bidderUserName;
    @ManyToOne
    private Product product;
    private int quantity;

    @ElementCollection
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
    private  Set<String> approvers;
    private double price;



//    AtomicInteger counter;

    public Bid(IShopObserver bidder, int shopId, Product product, int quantity, double price) {
        this.bidder = bidder;
        this.bidderUserName = bidder.getUsername();
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.shopId = shopId;
        this.approvers = new ConcurrentSkipListSet<>();

    }

    public Bid(){

    }


    public Map<String, Role> getRoleInShop(){
        return RoleController.getInstance().getAllShopRoles(this.shopId);
    }


    private boolean isUserNameApprover(String userName) {
        Role role = getRoleInShop().get(userName);
        if(role == null) return false;
        if(role instanceof ShopOwner) return true;
        ShopManager shopManager = (ShopManager) role;
        try {
            shopManager.isPermissionExists(PermissionManagement.BidApprover);
            return true;
        }catch (InnerLogicException e) {
            return false;
        }
    }

    private boolean isBidApproved(){
        for(Map.Entry<String,Role> entry: getRoleInShop().entrySet()){
            if(isUserNameApprover(entry.getKey()) && !approvers.contains(entry.getKey())) return false;
        }
        return true;
    }

    public void approveBid(String approverUserName) throws InnerLogicException {
        if(!isUserNameApprover(approverUserName) || approvers.contains(approverUserName)){
            throw new InnerLogicException(approverUserName + "tried to approve bid that he is not exepted to approve");
        }
        approvers.add(approverUserName);
        if(isBidApproved()) bidder.sendNotification(new BidNotification(id, "your bid is approved"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return getId() == bid.getId() && getShopId() == bid.getShopId() && getQuantity() == bid.getQuantity() && Double.compare(bid.getPrice(), getPrice()) == 0 && getBidder().equals(bid.getBidder()) && getBidderUserName().equals(bid.getBidderUserName()) && getProduct().equals(bid.getProduct()) && getApprovers().equals(bid.getApprovers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBidder(), getShopId(), getBidderUserName(), getProduct(), getQuantity(), getApprovers(), getPrice());
    }

    public synchronized void incrementBidPrice(String approverUserName, double newPrice) throws InnerLogicException {
        if(!isUserNameApprover(approverUserName) || approvers.contains(approverUserName)){
            throw new InnerLogicException(approverUserName + "tried to change bid that he is not exepted to change");
        }
        if(newPrice < price){
            throw new InnerLogicException(approverUserName + "tried to change bid price to lower price");
        }
        if(newPrice > product.getPrice()){
            throw new InnerLogicException(approverUserName + "tried to change bid price to price that is higher than the product price");
        }
        double oldPrice = price;
        price = newPrice;
        DBConnection dbConnection = PersistenceManager.beginTransaction();
        dbConnection.addFailure(()->price = oldPrice);
        dbConnection.update(this);
        PersistenceManager.commitTransaction(dbConnection);
        bidder.sendNotification(new BidNotification(id, "the shop offered new price on your bid"));
    }

    public synchronized PurchaseHistory purchaseBid(String userName, int visitorId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip, IDelivey delivery, IPayment payment, DBConnection dbConnection) throws InnerLogicException {
        if(!isBidApproved()){
            throw  new InnerLogicException(bidder + "tried to buy unapproved bid");
        }
        Shop shop = ShopController.getInstance().getShop(visitorId, shopId);



        product.acquire();

        if (product.getQuantity() < quantity) {
            product.release();
            return new PurchaseHistory("purchase bid failed: Required quantity is bigger than can be supplied. product: " + product.getId(), visitorId, shopId);
        }
        int deliveryId = delivery.bookDelivery(userName,country, city, street,zip);
        if (deliveryId < 0) {
            product.release();
            return new PurchaseHistory("purchase bid failed: Failed to get confirmation for delivery for basket.", visitorId, shopId);
        }
        dbConnection.addFailure(()->delivery.cancelDelivery(deliveryId));


        int chargedReceipt = payment.chargeCreditCard(creditCardNumber, date,userName, cvs, price);
        if (chargedReceipt < 0) {
//            ProxyDelivery.getInstance().cancelDelivery(deliveryId);
            delivery.cancelDelivery(deliveryId);
            product.release();

            return new PurchaseHistory("Failed to get confirmation for payment for basket.", visitorId, shopId);
        }
        dbConnection.addFailure(()->payment.cancelPayment(chargedReceipt));
        try {
            int oldQuantity = product.getQuantity();
            dbConnection.addFailure(()->product.setQuantityWithOutCheck(oldQuantity));
            product.setQuantity(product.getQuantity() - quantity);
            dbConnection.update(product);
        } catch (InnerLogicException e) {
            delivery.cancelDelivery(deliveryId);
            payment.cancelPayment(chargedReceipt);
        }
        product.release();

        PurchaseHistory purchaseHistory = new PurchaseHistory(visitorId, bidder.getUsername() , shopId, product.getId(),quantity, price, deliveryId, chargedReceipt);

        PurchaseCashier.getInstance().saveHistory(purchaseHistory, bidder.getUsername(), shopId);


        shop.notifyShopObserversWithConnection(purchaseHistory.toString(),dbConnection);
        return purchaseHistory;
    }

    public IShopObserver getBidder() {
        return bidder;
    }

    public void setBidder(IShopObserver bidder) {
        this.bidder = bidder;
    }

    public String getBidderUserName() {
        return bidderUserName;
    }

    public void setBidderUserName(String bidderUserName) throws InnerLogicException {
        this.bidderUserName = bidderUserName;
        bidder = VisitorController.getInstance().getMember(bidderUserName);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public Set<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(Set<String> approvers) {
        this.approvers = approvers;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}
