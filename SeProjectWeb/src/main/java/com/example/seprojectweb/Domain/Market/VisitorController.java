package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Responses.BidResponse;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;
import com.example.seprojectweb.Domain.PersistenceManager;
import com.example.seprojectweb.Domain.Security.SecurityRepresentative;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VisitorController {
    //TODO: sync the maps members and visitors
    private final Map<String, Member> members;
    private final Map<Integer, Visitor> visitors;
    private final AtomicInteger visitorIdCount;
    private final Map<String, ShoppingCart> cartsMapper;          // key: memberUserName value: member.ShoppingCart


    public List<Member> getMembersList() {
        List allMembers = PersistenceManager.findAll(Member.class);
        for (Object m: allMembers){
            Member mi = (Member) m;
            members.putIfAbsent(mi.getUsername(),mi);
        }
        return new ArrayList<>(members.values());
    }
    public List<Visitor> getAllVisitors(){
        return new ArrayList<>(visitors.values());
    }


    private VisitorController() {
        members = new HashMap<>();
        visitors = new ConcurrentHashMap<>();
        this.visitorIdCount = new AtomicInteger(0);
        cartsMapper = new ConcurrentHashMap<>();
        //getMembersList();

        //Thread visitorSessionHandler = new Thread(new VisitorSessionHandler(visitors)); // thread in charge of removing not active visitors
        //visitorSessionHandler.start();
    }

    public List<Member> getAllAdmins() {
        List<Member> allMembers = getMembersList();
        List<Member> admins = new LinkedList<>();
        for (Member member : allMembers){
            if (member.isAdmin())
                admins.add(member);
        }
        return admins;
    }

    public void verifyIsMember(String usernameToAssign) {
    }


    private static class VisitorControllerHolder {
        private static final VisitorController instance = new VisitorController();
    }

    public static VisitorController getInstance() {
        return VisitorControllerHolder.instance;
    }

    public Visitor visitSystem() {
        int id = visitorIdCount.getAndIncrement();
        Visitor visitor = new Visitor(id);
        //TODO: sync the map use
        visitors.put(id, visitor);
        return visitor;
    }

    public Visitor leaveSystem(int visitorId) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        if(visitor.isLoggedIn()){
            visitor.logout();
        }
        visitors.remove(visitorId);
        return visitor;
    }
    public boolean isMemberExists(String userName){
        Member m = members.get(userName);
        if(m != null){
            return true;
        }
        if ((m = (Member) PersistenceManager.find(Member.class,userName)) != null) {
            members.put(userName,m);
            return true;
        }
        return false;
    }

    public Member getMember(String username) throws InnerLogicException {
        if (!isMemberExists(username)) {
            throw new InnerLogicException(username + "is not match to any member");
        }
        return members.get(username);
    }

    public Visitor login(int visitorId, String username, String password, IMemberObserver memberObserver) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        Member member = getMember(username);

        if (!member.verifyPassword(password)) {
            throw new InnerLogicException("wrong password");
        }
//        if (cartsMapper.containsKey(username)){
//            member.setShoppingCart(cartsMapper.get(username));
//        }
        visitor.login(member, memberObserver);
        //cartsMapper.putIfAbsent(username, member.getShoppingCart());
        //visitor.setShoppingCart(cartsMapper.getOrDefault(username, new ShoppingCart()));
        return visitor;
    }

    public Visitor logout(int visitorId) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
//        Member loggedIn = visitor.getLoggedIn();
//        if (loggedIn != null) {
//            cartsMapper.putIfAbsent(loggedIn.getUsername(), visitor.getShoppingCart());
//        }
        visitor.logout();
        visitor.setShoppingCart(new ShoppingCart());
        return visitor;
    }

    public synchronized Member register(int visitorId, String username, String password) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        if (isMemberExists(username)) {
            throw new InnerLogicException("username already used");
        }
        Member newMember = new Member(username, password);
        // TODO: maybe save only when logout
        PersistenceManager.persist(newMember);
        members.put(username, newMember);
        return newMember;
    }
    public synchronized Member adminRegister(int visitorId, String username, String password) throws InnerLogicException {
        Member newMember = new Member(username, password);
        newMember.setAdmin(true);
        members.put(username, newMember);
        PersistenceManager.persist(newMember);
        return newMember;
    }


    // only user can unregister himself for now (not in requirements)
    public Member unRegister(int visitorId) throws InnerLogicException {
        Member loggedIn = getVisitorLoggedIn(visitorId);
        getVisitor(visitorId).logout();
        members.remove(loggedIn.getUsername());
        PersistenceManager.remove(loggedIn);

        return loggedIn;
    }

    /***
     * admin cancel a membership...
     * @param userName
     * @return
     * @throws InnerLogicException
     */
    public Member unRegister(String userName) throws InnerLogicException {
        Member toRemove = getMember(userName);
        //if the user log in logged him out
        if (toRemove.isLoggedIn()) {
            toRemove.logout();
        }

        toRemove = members.remove(toRemove.getUsername());
        if (toRemove == null) {
            throw new InnerLogicException("user: " + userName + " is not a member");
        }
        PersistenceManager.remove(toRemove);

        return toRemove;
    }

    //return the visitor with the given id, throw inner logic exception if non exist
    public Visitor getVisitor(int visitorId) throws InnerLogicException {
        Visitor visitor = visitors.get(visitorId);
        if (visitor == null) {
            throw new InnerLogicException("visit session timed out");
        }
        visitor.updateLastActivityTime();
        return visitor;
    }
    public boolean isVisitorOnCache(String memberUserName){
        return members.get(memberUserName) != null ;
    }

    public Member changePassword(String userName,String newPassword) throws InnerLogicException {
        Member member = getMember(userName);
        member.setHashedPassword(SecurityRepresentative.encryptPassword(newPassword));
        PersistenceManager.update(member);
        return member;
    }

    //return the visitor with the given id, throw inner logic exception if non exist
    public Member getVisitorLoggedIn(int visitorId) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        Member loggedIn = visitor.getLoggedIn();
        if (loggedIn == null) {
            throw new InnerLogicException("visitor is not logged in");
        }
        return loggedIn;
    }

    public ShoppingCart addProductToShoppingCart(int visitorID, int shopID, Product prod, int quantity) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorID);
        return visitor.addProductToShoppingCart(shopID, prod, quantity);

    }

    public ShoppingCart removeProductFromShoppingCart(int visitorID, int shopID, int productID) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorID);
        return visitor.removeProductFromShoppingCart(shopID, productID);
    }

    public Set<Map.Entry<Integer, ShoppingBasket>> getBaskets(int visitorID) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorID);
        return visitor.getBaskets();
    }

    public ShoppingCart getShoppingCart(int visitorID) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorID);
        return visitor.getShoppingCart();
    }


    //********************************Bid*******************************************
    public Bid bidProduct(int visitorId, int shopId, int productId, int quantity, double price) throws InnerLogicException {
        Member loggedIn = getVisitorLoggedIn(visitorId);
        if(quantity <= 0){
            throw new InnerLogicException("quantity of bid must be positive");
        }
        if(price <= 0){
            throw new InnerLogicException("price of bid must be positive");
        }
        return loggedIn.bidProduct(ShopController.getInstance().getShop(shopId), productId, quantity, price);
    }

    public PurchaseHistory purchaseBid(int visitorId, int bidId, String creditCardNumber, String date, String cvs, String country, String city, String street, String zip) throws InnerLogicException {
        return getVisitorLoggedIn(visitorId).purchaseBid(visitorId, bidId, creditCardNumber, date, cvs, country, city, street, zip);
    }

    public List<Bid> getAllMemberBid(int visitorId) throws InnerLogicException {
        return new LinkedList<Bid>(getVisitorLoggedIn(visitorId).getActiveBids().values());
    }
    //******************************************************************************







}
