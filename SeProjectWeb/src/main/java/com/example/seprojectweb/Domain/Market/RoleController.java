package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.DBConnection;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.Market.Notifications.ShopNotification;
import com.example.seprojectweb.Domain.PersistenceManager;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoleController {
    // This class is for saving the shared data in a single object
    // the main issue was updating the shop managers
    // and collecting information in an easy and fast way
    private Map<Integer, Map<String, Role>> shopsMapper;     //key: shopId, value: Map of key: memberUserName, value: Role
    private Map<String, Map<Integer, Role>> membersMapper;   //key: memberUserName, value: Map of key: shopID, value: Role

    private RoleController() {
        shopsMapper = new ConcurrentHashMap<>();
        membersMapper = new ConcurrentHashMap<>();
        List<ShopOwner> shopOwners = PersistenceManager.findAll(ShopOwner.class);
        List<ShopManager> shopManagers = PersistenceManager.findAll(ShopManager.class);
        loadShopRoles(shopOwners);
        loadShopRoles(shopManagers);
    }

    private void loadShopRoles(List<? extends Role> roles) {
        for (Role role : roles){
            int shopId = role.getShopID();
            String memberId = role.getMemberID();

            if (!shopsMapper.containsKey(role.getShopID())){
                shopsMapper.put(shopId,new ConcurrentHashMap<>());
            }
            Map<String, Role> shopRoles = shopsMapper.get(shopId);
            shopRoles.put(memberId, role);

            if (!membersMapper.containsKey(memberId)){
                membersMapper.put(memberId, new ConcurrentHashMap<>());
            }
            Map<Integer, Role> memberRoles = membersMapper.get(memberId);
            memberRoles.put(shopId, role);
        }
    }

    public Set<Integer> getListOfShops(Integer visitorId) throws InnerLogicException {
        String userName = getUserName(visitorId);
        return membersMapper.get(userName).keySet();
    }

    public List<Shop> getMemberShops(Member member) throws InnerLogicException {
        Map<Integer, Role> shopsToRole = membersMapper.get(member.getUsername());
        List<Shop> memberShops = new ArrayList<>();
        if (shopsToRole == null){
            return memberShops;
        }
        ShopController shopController = ShopController.getInstance();
        for (Integer shopId : shopsToRole.keySet()){
            Shop shop = shopController.getShop(shopId);
            memberShops.add(shop);
        }
        return memberShops;
    }

    public void sendMessagesToShopOwners(int shopId, Notification notification) throws InnerLogicException {
        Map<String,Role> managers = shopsMapper.get(shopId);
        for(String manager: managers.keySet()){
            VisitorController visitorController = VisitorController.getInstance();
            Member m = visitorController.getMember(manager);
            m.sendNotification(notification);
        }
    }
    public void sendMessagesToShopOwnersWithConnection(int shopId, Notification notification, DBConnection dbConnection) throws InnerLogicException {
        Map<String,Role> managers = shopsMapper.get(shopId);
        for(String manager: managers.keySet()){
            VisitorController visitorController = VisitorController.getInstance();
            Member m = visitorController.getMember(manager);
            m.sendNotificationWithConnection(notification,dbConnection);
        }
    }




    private static class RoleControllerHolder {
        private static final RoleController instance = new RoleController();
    }

    public static RoleController getInstance() {
        return RoleControllerHolder.instance;
    }

    //TODO: add concurrency
    public Map<Integer, Map<String, Role>> getShopsMapper() {
        return shopsMapper;
    }

    public void setShopsMapper(Map<Integer, Map<String, Role>> shopsMapper) {
        this.shopsMapper = shopsMapper;
    }

    public void setMembersMapper(Map<String, Map<Integer, Role>> membersMapper) {
        this.membersMapper = membersMapper;
    }

    public Role removeRoleFromShop(Role r) {
        String userName = r.getMemberID();
        int shopId = r.getShopID();

        shopsMapper.get(shopId).remove(userName);
        membersMapper.get(userName).remove(shopId);

        return r;
    }
    public void removeAllShop(Role r){
        shopsMapper.remove(r.getShopID());
        membersMapper.remove(r.getMemberID());
    }

    public void verifyIsShopOwner(int visitorId, int shopId) throws InnerLogicException {
        String userName = getUserName(visitorId);
        if (!verifyIsShopOwner(shopId, userName)) {
            throw new InnerLogicException("visitor is not a shop owner");
        }
    }

    public void verifyManageInventoryPermission(int visitorId, int shopId) throws InnerLogicException {
        String userName = getUserName(visitorId);

        if (shopsMapper.get(shopId) == null)
            throw new InnerLogicException("tried to get shopID: " + shopId + "but the shop does not exist");
        Role role = shopsMapper.get(shopId).get(userName);
        if (role == null) throw new InnerLogicException(userName + "is dont have permission to manage inventory");
        if ((role instanceof ShopOwner)) return;
        ShopManager shopManager =  (ShopManager)role;
        shopManager.isPermissionExists(PermissionManagement.ManageInventory);
    }

    public ShopManager setManagerPermission(int visitorId, int shopId, int e, String useName) throws InnerLogicException {
        String userNameAssignor = getUserName(visitorId);
        if (!verifyIsShopOwner(shopId, userNameAssignor)) {
            throw new InnerLogicException(userNameAssignor + "not a shop Owner of this shop");
        } else {
            ShopManager sM = getShopManager(shopId, useName);
            sM.setPermission(e, userNameAssignor);
            return sM;
        }
    }

    public ShopManager removeManagerPermission(int visitorId, int shopId, int e, String useName) throws InnerLogicException {
        String userNameAssignor = getUserName(visitorId);
        if (!verifyIsShopOwner(shopId, userNameAssignor)) {
            throw new InnerLogicException(userNameAssignor + "not a shop Owner of this shop");
        } else {
            ShopManager sM = getShopManager(shopId, useName);
            sM.removePermission(e, userNameAssignor);
            return sM;
        }
    }

    public Map<String, Role> getAllShopRoles(int shopId){
        return shopsMapper.get(shopId);
    }


    public List<ShopOwner> getShopOwners(int shopId) {
        Map<String, Role> rolesInShop = shopsMapper.get(shopId);
        List<ShopOwner> shopOwners = new ArrayList<>();
        for (Role role : rolesInShop.values()){
            if (role instanceof ShopOwner){
                shopOwners.add((ShopOwner) role);
            }
        }
        return shopOwners;
    }

    /***
     *
     * @param visitorId visitor to check on
     * @param shopId the sote to check the permission on
     * @param e the permission
     */
    public void isPermissionExists(int visitorId, int shopId, int e) throws InnerLogicException {
        String useName = getUserName(visitorId);
        // if shop owner so there is a permission
        if (verifyIsShopOwner(shopId, useName)) {
            return;
        }
        //check for shop if hes a shop owner
        getShopManager(shopId, useName).isPermissionExists(e);
    }

    /***
     *
     * @return return the overview list of the shop management info
     * @throws InnerLogicException
     */
    public Set<Map.Entry<String, Role>> getShopManagementInfo(int visitorId, int shopId) throws InnerLogicException {
        String userName = getUserName(visitorId);
        if (verifyIsShopOwner(shopId, userName)) {
            return shopsMapper.get(shopId).entrySet();
        }
        throw new InnerLogicException("");
    }

    /***
     *
     * @param memberUserName
     * @param shopId
     * @return return the permissions of a specific member
     * @throws InnerLogicException
     */
    public ShopManager getPermissionsOfMember(int visitorId, String memberUserName, int shopId) throws InnerLogicException {
        String userName = getUserName(visitorId);
        ShopOwner shopOwner = getShopOwner(shopId, userName);
        return getShopManager(shopId, memberUserName);
    }

    /***
     *
     * @param visitorId the vitor id who ask
     * @param shopId the shop od ask to
     * @return pair in first: shop managers appointments, in second is owners appointment
     * @throws InnerLogicException
     */
    private Pair<List<ShopManager>, List<ShopOwner>> getOwnersAndManagers(int visitorId, int shopId) throws InnerLogicException {
        String memberUserName = getUserName(visitorId);
        ShopOwner shopOwner = getShopOwner(shopId, memberUserName);
        List<String> useNames = shopOwner.getAssigneeUsernames();

        //gets appointments in shop
        List<Pair<String, Role>> roles = getAppointedOfOwner(useNames, shopId);


        List<ShopManager> outPutManagers = new LinkedList<>();
        List<ShopOwner> outPutOwners = new LinkedList<>();

        for (Pair<String, Role> r : roles) {
            if (r.getValue() instanceof ShopManager) {
                outPutManagers.add((ShopManager) r.getValue());
            } else if (r.getValue() instanceof ShopOwner) {
                outPutOwners.add((ShopOwner) r.getValue());
            } else {
                throw new InnerLogicException("this is fatal in getOwners");
            }
        }
        return new Pair<>(outPutManagers, outPutOwners);
    }
    public List<ShopOwner> getALLOwners() throws InnerLogicException {
        List<ShopOwner> outPut = new LinkedList<>();
        List owners = PersistenceManager.findAll(ShopOwner.class);
        for (Object shopOwner: owners){
            ShopOwner shopOwneri = (ShopOwner) shopOwner;
            addToMapsIfAbsent(shopOwneri);
            outPut.add(shopOwneri);
        }
        return outPut;
    }
    private void addToMapsIfAbsent(Role r){
        ConcurrentHashMap<String,Role> h1 = new ConcurrentHashMap<>();
        h1.put(r.getMemberID(),r);
        shopsMapper.putIfAbsent(r.getShopID(),h1);

        ConcurrentHashMap<Integer,Role> h2 = new ConcurrentHashMap<>();
        h2.put(r.getShopID(),r);
        membersMapper.putIfAbsent(r.getMemberID(),h2);
    }

    /***
     *
     * @param userName the use names that owner has appointed
     * @param shopId the shop id
     * @return the roles in this shop that the use name has appointed
     * @throws InnerLogicException
     */
    private List<Pair<String, Role>> getAppointedOfOwner(List<String> userName, int shopId) throws InnerLogicException {
        List<Pair<String, Role>> outPut = new LinkedList<>();
        for (String user : userName) {
            Role r = membersMapper.get(user).get(shopId);
            if (r == null) {
                throw new InnerLogicException("something goes wrong this is fatal!!");
            }
            outPut.add(new Pair<>(user, r));
        }

        return outPut;
    }

    /***
     *
     * @param visitorId
     * @return use name of the logged in person
     * @throws InnerLogicException
     */
    public String getUserName(int visitorId) throws InnerLogicException {
        VisitorController visitorController = VisitorController.getInstance();
        Member assignor = visitorController.getVisitorLoggedIn(visitorId);
        return assignor.getUsername();
    }

    public void verifyShopOwner(int shopId, String memberUsername) throws InnerLogicException {
        if (!verifyIsShopOwner(shopId, memberUsername)) {
            throw new InnerLogicException("username: " + memberUsername + " is not shop owner of " + shopId);
        }
    }

    public ShopOwner assignFounder(Member founder, Shop shop) throws InnerLogicException {
        int shopID = shop.getShopId();


        ShopOwner shopOwner = createShopOwner(null,shop,founder);


        //check if the member does not have a shop already
        if (!membersMapper.containsKey(founder.getUsername())) {
            membersMapper.put(founder.getUsername(), new ConcurrentHashMap<>());
        }
        membersMapper.get(founder.getUsername()).put(shopID, shopOwner);

        //because its a new shop we dont have to check if exist
        if (shopsMapper.containsKey(shopID)) {
            throw new InnerLogicException("tried to open shopID: " + shopID + "but the shop already exist");
        }

        shopsMapper.put(shopID, new ConcurrentHashMap<>());
        shopsMapper.get(shopID).put(founder.getUsername(), shopOwner);


        return shopOwner;
    }


    // adding a manager to a shop version
    public ShopManager assignShopManager(Shop shop,int visitorId, String usernameToAssign) throws InnerLogicException {
        int shopID = shop.getShopId();
        try {
            ShopController.getInstance().acquireShopRoleAssign(shopID);
            VisitorController visitorController = VisitorController.getInstance();
            Member assignor = visitorController.getVisitorLoggedIn(visitorId);
            Member assignee = visitorController.getMember(usernameToAssign);

            //this is must be the first check !!!!!
            ShopOwner assignorOwnerRole = getShopOwner(shopID,assignor.getUsername());

            if(getAndPutRolePersistence(shopID, usernameToAssign)){
                throw new InnerLogicException("member user name " + usernameToAssign + " have role in this shop");
            }
            DBConnection dbConnection = PersistenceManager.beginTransaction();

            ShopManager manager = createShopManager(assignor.getUsername(),shop,assignee);
            dbConnection.persist(manager);

            initMapsInMaps(shopID, usernameToAssign);

            shopsMapper.get(shopID).put(usernameToAssign, manager);
            membersMapper.get(usernameToAssign).put(shopID, manager);

            dbConnection.addFailure(()->removeRoleFromShop(manager));


            assignorOwnerRole.addAssignee(assignee.getUsername());
            dbConnection.addFailure(()->assignorOwnerRole.getAssigneeUsernames().remove(assignee.getUsername()));

            dbConnection.update(assignorOwnerRole);

            PersistenceManager.commitTransaction(dbConnection);
            return manager;
        } finally {
            ShopController.getInstance().releaseShopRoleAssign(shopID);

        }
    }


    public ShopOwner assignShopOwnerAfterApproves(int shopID, String usernameToAssign, String assignorMember) throws InnerLogicException {
        try {
            ShopController.getInstance().acquireShopRoleAssign(shopID);
            Shop shop = ShopController.getInstance().getShop(shopID);
            VisitorController visitorController = VisitorController.getInstance();
            Member assignor = visitorController.getMember(assignorMember);
            Member assignee = visitorController.getMember(usernameToAssign);


            //this is must be the first check !!!!!
            ShopOwner assignorOwnerRole = getShopOwner(shopID,assignor.getUsername());


            if(getAndPutRolePersistence(shopID, usernameToAssign)){
                throw new InnerLogicException("member user name " + usernameToAssign + " have role in this shop");
            }
            DBConnection dbConnection = PersistenceManager.beginTransaction();

            ShopOwner owner = createShopOwner(assignor.getUsername(),shop,assignee);

            initMapsInMaps(shopID, usernameToAssign);
            shopsMapper.get(shopID).put(usernameToAssign, owner);
            membersMapper.get(usernameToAssign).put(shopID, owner);
            //if fail in db remove him
            dbConnection.addFailure(()->removeRoleFromShop(owner));
            dbConnection.persist(owner);

            assignorOwnerRole.addAssignee(assignee.getUsername());
            dbConnection.addFailure(()->assignorOwnerRole.getAssigneeUsernames().remove(assignee.getUsername()));
            dbConnection.update(assignorOwnerRole);


            shop.registerToShopOwnerNotification(assignee);
            //on failure
            dbConnection.addFailure(()->shop.unregisterToShopOwnerNotification(assignee));
            //dbConnection.update(shop);

            PersistenceManager.commitTransaction(dbConnection);
            assignee.sendNotification(new ShopNotification(shopID, "Congratz you have assigned to be shop owner"));
            return owner;
        } finally {
            ShopController.getInstance().releaseShopRoleAssign(shopID);

        }
    }


    public ShopOwner assignShopOwner(Shop shop, int visitorId, String usernameToAssign ) throws InnerLogicException {
        int shopID = shop.getShopId();
        try {
            ShopController.getInstance().acquireShopRoleAssign(shopID);
            VisitorController visitorController = VisitorController.getInstance();
            Member assignor = visitorController.getVisitorLoggedIn(visitorId);
            Member assignee = visitorController.getMember(usernameToAssign);



            //this is must be the first check !!!!!
            ShopOwner assignorOwnerRole = getShopOwner(shopID,assignor.getUsername());


            if(getAndPutRolePersistence(shopID, usernameToAssign)){
                throw new InnerLogicException("member user name " + usernameToAssign + " have role in this shop");
            }
            DBConnection dbConnection = PersistenceManager.beginTransaction();

            ShopOwner owner = createShopOwner(assignor.getUsername(),shop,assignee);

            initMapsInMaps(shopID, usernameToAssign);
            shopsMapper.get(shopID).put(usernameToAssign, owner);
            membersMapper.get(usernameToAssign).put(shopID, owner);
            //if fail in db remove him
            dbConnection.addFailure(()->removeRoleFromShop(owner));
            dbConnection.persist(owner);

            assignorOwnerRole.addAssignee(assignee.getUsername());
            dbConnection.addFailure(()->assignorOwnerRole.getAssigneeUsernames().remove(assignee.getUsername()));
            dbConnection.update(assignorOwnerRole);



            Shop s = ShopController.getInstance().getShop(visitorId, shopID);
            s.registerToShopOwnerNotification(assignee);
            //on failure
            dbConnection.addFailure(()->s.unregisterToShopOwnerNotification(assignee));
            dbConnection.update(s);


            PersistenceManager.commitTransaction(dbConnection);

            return owner;
        } finally {
            ShopController.getInstance().releaseShopRoleAssign(shopID);
        }
    }

    public ShopOwner getShopOwner(int shopID, String memberUserName) throws InnerLogicException {
        Role r = checkRole(shopID, memberUserName);
        if (!(r instanceof ShopOwner)) {
            throw new InnerLogicException("user name: " + memberUserName + " is not a shop owner");
        }
        return (ShopOwner) r;
    }
    public ShopManager getShopManager(int shopID, String memberUserName) throws InnerLogicException {
        Role r = checkRole(shopID, memberUserName);
        if (!(r instanceof ShopManager)) {
            throw new InnerLogicException("user name: " + memberUserName + " is not a shop manager");
        }
        return (ShopManager) r;
    }
    private Role checkRole(int shopID, String memberUserName) throws InnerLogicException {
        Role outPut;
        if (shopsMapper.get(shopID) == null && !getAndPutRolePersistence(shopID,memberUserName))
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        if ((outPut = (shopsMapper.get(shopID).get(memberUserName))) == null)
            throw new InnerLogicException("ur dont have a role in this shop!");
        return outPut;
    }



    private void initMapsInMaps(int shopID, String username) {
        if (!shopsMapper.containsKey(shopID)) {
            shopsMapper.put(shopID, new ConcurrentHashMap<>());
        }
        if (!membersMapper.containsKey(username)) {
            membersMapper.put(username, new ConcurrentHashMap<>());
        }
    }




    // return if the member is a shop owner at the shop
    public boolean verifyIsShopOwner(int shopID, String memberUserName) throws InnerLogicException {
        if (shopsMapper.get(shopID) == null && !getAndPutRolePersistence(shopID,memberUserName))
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        if (shopsMapper.get(shopID).get(memberUserName) == null)
            return false;
        return shopsMapper.get(shopID).get(memberUserName) instanceof ShopOwner;
    }







    //check if the
    private boolean getAndPutRolePersistence(int shopID, String memberUserName){
        Role r = shopsMapper.get(shopID).get(memberUserName);
        if(r == null){
            r = (ShopOwner)PersistenceManager.find(ShopOwner.class, new RolePK2(memberUserName,shopID));
        }
        if (r == null){
            return false;
        }
        ConcurrentHashMap<String,Role> h1 = new ConcurrentHashMap<>();
        h1.put(memberUserName,r);

        ConcurrentHashMap<Integer,Role> h2 = new ConcurrentHashMap<>();
        h2.put(shopID,r);

        shopsMapper.put(shopID,h1);
        membersMapper.put(memberUserName,h2);
        return true;

    }


    // this is after we make sure there no shop exist


    public boolean isRoleExists(String userName) {
        return membersMapper.containsKey(userName);
    }


    public HashMap<String, ShopOwner> removeShopOwner(int visitorId, String userName, int shopId) throws InnerLogicException {
        String shopOwnerUserName = getUserName(visitorId);
        HashMap<String, ShopOwner> removed = removeShopOwnerHelper(shopOwnerUserName,userName,shopId);
        ShopController.getInstance().updateOnShopOwnerRemovals(visitorId, shopId, removed.keySet());
        return removed;

    }

    public HashMap<String, ShopOwner> removeShopOwnerHelper(String shopOwnerUserName, String userName, int shopId) throws InnerLogicException {

        ShopOwner shopOwner = getShopOwner(shopId, shopOwnerUserName);

        //get the shop owner to remove
        ShopOwner removedShopOwner = getShopOwner(shopId, userName);

        if(!shopOwner.getAssigneeUsernames().contains(userName)){
            throw new InnerLogicException("Assignee doesnt exist in Assignor");
        }
        DBConnection dbConnection = new DBConnection();

        dbConnection.setConnection();
        dbConnection.beginTransaction();

        dbConnection.remove(removedShopOwner);

        ShopNotification shopNotification = new ShopNotification(shopId, "your role in shop " + shopId + " has canceled.");
        Member mi = VisitorController.getInstance().getMember(userName);
        mi.sendNotificationWithConnection(shopNotification, dbConnection);



        //remove him from shop
        Role r = checkRole(shopId, userName);

        List<Role> toRemove =new LinkedList<>();
        toRemove.add(r);
        //remove all assignees of the shop owner
        HashMap<String,ShopOwner> outPut = removeShopOwnerHelperRec(userName, removedShopOwner, shopId,toRemove,dbConnection);

        // it will remove the assignee if exists else it will throw exception
        shopOwner.removeAssignee(userName);
        dbConnection.addFailure(()->shopOwner.addAssignee(userName));
        dbConnection.update(shopOwner);

        dbConnection.commitTransaction();
        dbConnection.closeConnections();

        return outPut;

    }

    private HashMap<String, ShopOwner> removeShopOwnerHelperRec(String userName, ShopOwner shopOwner, int shopId,List<Role> rolesToRemove,DBConnection dbConnection) throws InnerLogicException {
        HashMap<String, ShopOwner> outPut = new HashMap<>();
        //add himself to the list
        outPut.put(userName, shopOwner);
        List<String> assignees = shopOwner.getAssigneeUsernames();
        for (String a : assignees) {
            //remove the current from shop
            Role r = checkRole(shopId,a);
            rolesToRemove.add(r);
            //remove from DB and send notification
            ShopNotification shopNotification = new ShopNotification(shopId, "your role in shop " + shopId + " has canceled.");
            Member mi = VisitorController.getInstance().getMember(userName);
            if (mi.getMemberObserver() != null) {
                mi.getMemberObserver().sendRealTimeNotification(shopNotification, userName);
            }else {
                mi.getNotifications().add(shopNotification);
                //remove the notification if fail
                dbConnection.addFailure(()->mi.getNotifications().remove(shopNotification));
                dbConnection.update(mi);
            }
            dbConnection.remove(r);
            //if the role is shop owner remove hes assignee's
            if (r instanceof ShopOwner shopOwner1) {
                //call recursive to the function
                outPut = appendHash(outPut, removeShopOwnerHelperRec(a, shopOwner1, shopId,rolesToRemove,dbConnection));
            }
        }
        //remove all of them from list:
        for (Role r: rolesToRemove){
            removeRoleFromShop(r);
        }
        return outPut;
    }

    private HashMap<String, ShopOwner> appendHash(HashMap<String, ShopOwner> h1, HashMap<String, ShopOwner> h2) {
        h2.putAll(h1);
        return h2;
    }
    public ShopManager createShopManager(String assignor,Shop shop, Member m){
        return new ShopManager(assignor,shop.getShopId(),m.getUsername());
    }
    public ShopOwner createShopOwner(String assignor,Shop shop, Member m){
        return new ShopOwner(assignor,shop.getShopId(),m.getUsername());
    }

    public List<Role> getMemberRoles(String userName){
        List<Role> output = new LinkedList<Role>();
        for(Map.Entry<Integer, Map<String, Role>> entry: shopsMapper.entrySet()){
            Role role = entry.getValue().get(userName);
            if(role != null) output.add(role);
        }
        return output;
    }

}
