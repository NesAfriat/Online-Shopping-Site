package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Notifications.IMemberObserver;

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
        return new ArrayList<>(members.values());
    }


    private static class VisitorControllerHolder {
        private static final VisitorController instance = new VisitorController();
    }

    public static VisitorController getInstance(){
        return VisitorControllerHolder.instance;
    }

    private VisitorController() {
        members = new HashMap<>();
        visitors = new ConcurrentHashMap<>();
        this.visitorIdCount = new AtomicInteger(0);
        cartsMapper = new ConcurrentHashMap<>();
        Thread visitorSessionHandler = new Thread(new VisitorSessionHandler(visitors)); // thread in charge of removing not active visitors
        visitorSessionHandler.start();
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
        if (visitor.isLoggedIn()){
            visitor.logout();
        }
        visitors.remove(visitorId);
        return visitor;
    }


    public Visitor login(int visitorId, String username, String password, IMemberObserver memberObserver) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        Member member = members.get(username);
        if(member == null){
            throw new InnerLogicException("username does not exist");
        }
        if(!member.verifyPassword(password)){
            throw new InnerLogicException("wrong password");
        }
        visitor.login(member, memberObserver);
        visitor.setShoppingCart(cartsMapper.getOrDefault(username, new ShoppingCart()));
        return visitor;
    }


    public Visitor logout(int visitorId) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        Member loggedIn = visitor.getLoggedIn();
        if(loggedIn != null){
            cartsMapper.put(loggedIn.getUsername(), visitor.getShoppingCart());
        }
        visitor.logout();
        visitor.setShoppingCart(new ShoppingCart());
        return visitor;
    }


    public synchronized Member register(int visitorId, String username, String password) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        if(members.get(username) != null){
            throw new InnerLogicException("username already used");
        }
        Member newMember = new Member(username, password);
        members.put(username, newMember);
        return newMember;
    }

    // TODO: remove from DB in next versions
    // only user can unregister himself for now (not in requirements)
    public Member unRegister(int visitorId) throws InnerLogicException {
        Member loggedIn = getVisitorLoggedIn(visitorId);
        members.remove(loggedIn.getUsername());
        getVisitor(visitorId).logout();
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
        if(toRemove.isLoggedIn()){
            toRemove.logout();
        }
        toRemove =  members.remove(toRemove.getUsername());
        if(toRemove == null){
            throw new InnerLogicException("user: "+ userName + " is not a member");
        }
        return toRemove;
    }

    //return the visitor with the given id, throw inner logic exception if non exist
    public Visitor getVisitor(int visitorId) throws InnerLogicException {
        Visitor visitor = visitors.get(visitorId);
        if(visitor == null) {
            throw new InnerLogicException("visit session timed out");
        }
        visitor.updateLastActivityTime();
        return visitor;
    }


    public Member getMember(String username) throws InnerLogicException {
        Member member = members.get(username);
        if(member == null){
            throw new InnerLogicException(username + "is not match to any member");
        }
        return member;
    }



    //return the visitor with the given id, throw inner logic exception if non exist
    public Member getVisitorLoggedIn(int visitorId) throws InnerLogicException {
        Visitor visitor = getVisitor(visitorId);
        Member loggedIn = visitor.getLoggedIn();
        if(loggedIn == null){
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


}
