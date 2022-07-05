package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.DeliveryAdapter.HTTPDelivery;
import com.example.seprojectweb.Domain.DeliveryAdapter.ProxyDelivery;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Notifications.IShopObserver;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;
import com.example.seprojectweb.Domain.PaymentAdapter.HTTPPayment;
import com.example.seprojectweb.Domain.PaymentAdapter.ProxyPayment;
import com.example.seprojectweb.Domain.PersistenceManager;
import com.example.seprojectweb.Domain.Security.SecurityRepresentative;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
@Entity
@NamedQuery(name="Member.findAll", query="SELECT m FROM Member m")
public class Member implements IShopObserver {
    @Id
    @Column(name = "id", nullable = false)
    private String username;
    private  String hashedPassword;
    private String Email;
    private boolean isAdmin;
    private String phoneNumber;
    @Transient
    private AtomicBoolean isLoggedIn;
    @Transient
    private IMemberObserver memberObserver;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Notification> notifications;

    public IMemberObserver getMemberObserver() {
        return memberObserver;
    }

    public void setMemberObserver(IMemberObserver memberObserver) {
        this.memberObserver = memberObserver;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ShoppingCart shoppingCart;

//    @ManyToMany(targetEntity = Bid.class, fetch = FetchType.EAGER)

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    private Map<Integer,Bid> activeBids;

    public Member(String username, String password) {
        this.username = username;
        this.hashedPassword = SecurityRepresentative.encryptPassword(password);
        this.isLoggedIn = new AtomicBoolean(false);
        this.notifications = new ConcurrentLinkedQueue<>();
        this.shoppingCart = new ShoppingCart();
        this.isAdmin = false;
        this.activeBids = new ConcurrentHashMap<>();
        //PersistenceManager.save(shoppingCart);
    }

    public Member() {
        isLoggedIn = new AtomicBoolean(false);
    }

    public String getUsername() {
        return username;
    }



    public boolean verifyPassword(String plainPassword) {
        return SecurityRepresentative.validatePass(plainPassword, hashedPassword);
    }

    public void login(IMemberObserver observer) throws InnerLogicException {
        if (isLoggedIn.getAndSet(true)) {
            throw new InnerLogicException("tried to login to " + username + " but the member is already logged in");
        }
        memberObserver = observer;
        if(!(notifications == null || notifications.isEmpty())){
            for (Notification notification : notifications) {
                memberObserver.sendRealTimeNotification(notification, this.username);
            }
            notifications.clear();
            PersistenceManager.update(this);
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn.get();
    }

    public void logout() throws InnerLogicException {
        if (!isLoggedIn.getAndSet(false)) {
            throw new InnerLogicException("tried to logout from " + username + " but the member was not logged in");
        }

        memberObserver = null;
    }

    //**************************bid purchase**************************

    public Bid bidProduct(Shop shop, int productId, int quantity, double price) throws InnerLogicException {
        Bid bid = shop.bidProduct(this, productId, quantity, price);
        activeBids.put(bid.getId(),bid);
        PersistenceManager.update(this);
        return bid;
    }

    public PurchaseHistory purchaseBid(int visitorId, int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) throws InnerLogicException {
        Bid chosenBid = activeBids.get(bidId);
        if(chosenBid != null){
            ProxyDelivery proxyDelivery = ProxyDelivery.getInstance();
            proxyDelivery.setDeliveryService(new HTTPDelivery());
            ProxyPayment proxyPayment = ProxyPayment.getInstance();
            proxyPayment.setPaymentService(new HTTPPayment());
            DBConnection dbConnection = PersistenceManager.beginTransaction();
            PurchaseHistory output = chosenBid.purchaseBid(username, visitorId, creditCardNumber, date, cvs, country, city, street, zip, proxyDelivery, proxyPayment,dbConnection);
            if(!output.isErrorOccurred()){
                activeBids.remove(bidId);
                Shop shop =  ShopController.getInstance().getShop(chosenBid.getShopId());

                shop.getActiveBids().remove(bidId);
                dbConnection.update(shop);
                dbConnection.update(this);
                dbConnection.remove(chosenBid);

            }
            PersistenceManager.commitTransaction(dbConnection);


            return output;
        }

        throw new InnerLogicException(username + " dont have any bid with id " + bidId);
    }


    //****************************************************************


    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public void sendNotification(Notification shopNotification) {
        if (memberObserver != null) {
            memberObserver.sendRealTimeNotification(shopNotification, this.username);
        }else {
            this.notifications.add(shopNotification);
            PersistenceManager.update(this);
        }
    }
    @Override
    public void sendNotificationWithConnection(Notification notification, DBConnection dbConnection) throws InnerLogicException {
        if (memberObserver != null) {
            memberObserver.sendRealTimeNotification(notification, this.username);
        }else {
            this.notifications.add(notification);
            //remove if fails.
            dbConnection.addFailure(()->notifications.remove(notification));
            dbConnection.update(this);
        }
    }



    public Collection<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Collection<Notification> notifications) {
        this.notifications =  new ConcurrentLinkedQueue<>();
        this.notifications.addAll(notifications);
    }

    public void sendDataUpdateNotice(){
        if(isAdmin && memberObserver != null){
            memberObserver.sendDataUpdateNotice(this.username);
        }
    }

    public ShoppingCart getShoppingCart() {
        return this.shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Member object){
            return object.getUsername().equals(this.username);
        }
        return false;
    }

    public void clearCart(List<Map.Entry<Integer, ShoppingBasket>> basketsToDelete,DBConnection dbConnection) throws InnerLogicException {
        Set<Map.Entry<Integer, ShoppingBasket>> shopIdToBasket = shoppingCart.getBaskets();
        for (Map.Entry<Integer, ShoppingBasket> shopIdBasketEntry : basketsToDelete) {
            shopIdToBasket.remove(shopIdBasketEntry);
            //PersistenceManager.remove(shopIdBasketEntry.getValue());
        }
        dbConnection.update(shoppingCart);
        for (Map.Entry<Integer, ShoppingBasket> shopIdBasketEntry : basketsToDelete) {
            dbConnection.remove(shopIdBasketEntry.getValue());
        }
    }




    public Map<Integer, Bid> getActiveBids() {
        return activeBids;
    }

    public void setActiveBids(Map<Integer, Bid> activeBids) {
        this.activeBids = activeBids;
    }
}
