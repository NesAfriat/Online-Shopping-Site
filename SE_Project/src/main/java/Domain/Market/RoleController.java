package Domain.Market;

import Domain.InnerLogicException;
import Domain.Market.Notifications.ShopNotification;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoleController {
    // This class is for saving the shared data in a single object
    // the main issue was updating the shop managers
    // and collecting information in an easy and fast way
    private Map<Integer, Map<String, Role>> shopsMapper;     //key: shopId, value: Map of key: memberUserName, value: Role
    private Map<String, Map<Integer, Role>> membersMapper;   //key: memberUserName, value: Map of key: shopID, value: Role


    //TODO: add concurrency
    public Map<Integer, Map<String, Role>> getShopsMapper() {
        return shopsMapper;
    }

    public void setMembersMapper(Map<String, Map<Integer, Role>> membersMapper) {
        this.membersMapper = membersMapper;
    }

    public void setShopsMapper(Map<Integer, Map<String, Role>> shopsMapper) {
        this.shopsMapper = shopsMapper;
    }

    private RoleController() {
        shopsMapper = new ConcurrentHashMap<>();
        membersMapper = new ConcurrentHashMap<>();


    }
    private Role removeRoleFromShop(String userName, int shopId){
        Role r =  shopsMapper.get(shopId).remove(userName);
        membersMapper.get(userName).remove(shopId);
        return r;
    }

    public void verifyIsShopOwner(int visitorId, int shopId) throws InnerLogicException {
        String userName = getUserName(visitorId);
        if (!verifyIsShopOwner(shopId, userName)) {
            throw new InnerLogicException("visitor is not a shop owner");
        }
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

    /***
     *
     * @param visitorId visitor to check on
     * @param shopId the sote to check the permission on
     * @param e the permission
     * @throws InnerLogicException
     */
    public void isPermissionExists(int visitorId, int shopId, Permissions e) throws InnerLogicException {
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
     * @param visitorId
     * @param shopId
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
     * @param visitorId
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

    private static class RoleControllerHolder {
        private static final RoleController instance = new RoleController();
    }

    public static RoleController getInstance() {
        return RoleController.RoleControllerHolder.instance;
    }


    // adding a manager to a shop version
    public ShopManager assignShopManager(int visitorId, String usernameToAssign, int shopID) throws InnerLogicException {
        try {
            if(!shopsMapper.containsKey(shopID)){
                throw new InnerLogicException("shop not found");
            }
            ShopController.getInstance().acquireShopRoleAssign(shopID);
            VisitorController visitorController = VisitorController.getInstance();
            Member assignor = visitorController.getVisitorLoggedIn(visitorId);
            Member assignee = visitorController.getMember(usernameToAssign);

            isLegalAssign(shopID, assignor.getUsername(), usernameToAssign);

            ShopManager manager = new ShopManager(assignor.getUsername());

            initMapsInMaps(shopID, usernameToAssign);

            shopsMapper.get(shopID).put(usernameToAssign, manager);
            membersMapper.get(usernameToAssign).put(shopID, manager);
            ShopOwner assignorOwnerRole = (ShopOwner) getMemberRoleInShop(assignor.getUsername(), shopID);
            if (assignorOwnerRole != null) {
                assignorOwnerRole.addAssignee(assignee.getUsername());
            }
            return manager;
        } catch (InnerLogicException e) {
            throw new InnerLogicException("error not a valid assignment");
        } catch (Exception e) {
            throw e;
        } finally {
            ShopController.getInstance().releaseShopRoleAssign(shopID);
        }
    }

    public ShopOwner assignShopOwner(int visitorId, String usernameToAssign, int shopID) throws InnerLogicException {
        try {
            if(!shopsMapper.containsKey(shopID)){
                throw new InnerLogicException("shop not found");
            }
            ShopController.getInstance().acquireShopRoleAssign(shopID);
            VisitorController visitorController = VisitorController.getInstance();
            Member assignor = visitorController.getVisitorLoggedIn(visitorId);
            Member assignee = visitorController.getMember(usernameToAssign);

            isLegalAssign(shopID, assignor.getUsername(), usernameToAssign);

            ShopOwner owner = new ShopOwner(assignor.getUsername());

            initMapsInMaps(shopID, usernameToAssign);

            shopsMapper.get(shopID).put(usernameToAssign, owner);
            membersMapper.get(usernameToAssign).put(shopID, owner);

            ShopOwner assignorOwnerRole = (ShopOwner) getMemberRoleInShop(assignor.getUsername(), shopID);
            if (assignorOwnerRole != null) {
                assignorOwnerRole.addAssignee(assignee.getUsername());
            }

            ShopController.getInstance().getShop(visitorId, shopID).registerToShopOwnerNotification(assignee);

            return owner;
        } catch (InnerLogicException e) {
            throw new InnerLogicException("error not a valid assignment");
        } catch (Exception e) {
            throw e;
        } finally {
            ShopController.getInstance().releaseShopRoleAssign(shopID);
        }
    }

    public ShopOwner addFirstShopOwner(int visitorId, int shopID) throws InnerLogicException {
        VisitorController visitorController = VisitorController.getInstance();
        String username = visitorController.getVisitorLoggedIn(visitorId).getUsername();

        initMapsInMaps(shopID, username);

        Map<String, Role> shopRoles = shopsMapper.get(shopID);
        if (!shopRoles.isEmpty()) // shop already has roles
            throw new InnerLogicException("shop already have roles in it. can't add founder");

        Map<Integer, Role> memberRoles = membersMapper.get(username);
        if (memberRoles.containsKey(shopID))
            throw new InnerLogicException("fatal! user already have role in this shop");

        ShopOwner shopOwner = new ShopOwner(null); // founder doesn't have assignor
        shopRoles.put(username, shopOwner);
        memberRoles.put(shopID, shopOwner);

        return shopOwner;
    }

    private Role getMemberRoleInShop(String username, int shopID) throws InnerLogicException {
        if (shopsMapper.get(shopID) == null)
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        return shopsMapper.get(shopID).get(username);
    }

    private void isLegalAssign(int shopID, String assignorUsername, String assigneeUsername) throws InnerLogicException {
        if(!shopsMapper.containsKey(shopID)){
            throw new InnerLogicException("not a valid shop id");
        }
        if (!verifyIsShopOwner(shopID, assignorUsername)) {
            throw new InnerLogicException("member tried to add role to a shop he is not own");
        }
        if (verifyIsShopOwner(shopID, assigneeUsername)) {
            throw new InnerLogicException("tried to assign " + assigneeUsername + " as role to shop " + shopID + "but the member is already owner");
        }
        if (isShopManager(shopID, assigneeUsername)) {
            throw new InnerLogicException("tried to assign " + assigneeUsername + " as role to shop " + shopID + "but the member is already manager");
        }
    }

    private void initMapsInMaps(int shopID, String username) {
        if (!shopsMapper.containsKey(shopID)) {
            shopsMapper.put(shopID, new ConcurrentHashMap<>());
        }
        if (!membersMapper.containsKey(username)) {
            membersMapper.put(username, new ConcurrentHashMap<>());
        }
    }

    // delete a member management from a shop
    public void removeRole(int shopID, String memberUserName) throws InnerLogicException {

        try {
            ShopController.getInstance().acquireShopRoleAssign(shopID);
            if (shopsMapper.get(shopID) == null)
                throw new InnerLogicException("tried to remove manager: " + memberUserName + " from shop: " + shopID + "but the shop does not exist");
            if (shopsMapper.get(shopID).get(memberUserName) == null)
                throw new InnerLogicException("tried to remove manager: " + memberUserName + " from shop: " + shopID + "but he isn't a manager");
            shopsMapper.get(shopID).remove(memberUserName);
            membersMapper.get(memberUserName).remove(shopID);
            VisitorController.getInstance().getMember(memberUserName).sendNotification(
                    new ShopNotification(shopID, "your role in shop " + shopID + " has canceled."));
        } catch (Exception e) {
            throw e;
        } finally {
            ShopController.getInstance().releaseShopRoleAssign(shopID);
        }

    }

    // return if the member is a shop owner at the shop
    public boolean verifyIsShopOwner(int shopID, String memberUserName) throws InnerLogicException {
        if (shopsMapper.get(shopID) == null)
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        if (shopsMapper.get(shopID).get(memberUserName) == null)
            return false;
        return shopsMapper.get(shopID).get(memberUserName) instanceof ShopOwner;
    }

    public ShopOwner getShopOwner(int shopID, String memberUserName) throws InnerLogicException {
        checkRole(shopID, memberUserName);
        if (!(shopsMapper.get(shopID).get(memberUserName) instanceof ShopOwner)) {
            throw new InnerLogicException("user name: " + memberUserName + " is not a shop owner");
        }
        return (ShopOwner) shopsMapper.get(shopID).get(memberUserName);
    }

    public ShopManager getShopManager(int shopID, String memberUserName) throws InnerLogicException {
        checkRole(shopID, memberUserName);
        if (!(shopsMapper.get(shopID).get(memberUserName) instanceof ShopManager)) {
            throw new InnerLogicException("user name: " + memberUserName + " is not a shop manager");
        }
        return (ShopManager) shopsMapper.get(shopID).get(memberUserName);
    }

    private void checkRole(int shopID, String memberUserName) throws InnerLogicException {
        if (shopsMapper.get(shopID) == null)
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        if (shopsMapper.get(shopID).get(memberUserName) == null)
            throw new InnerLogicException("ur dont have a role in this shop!");
    }


    // return if the member is a shop manager at the shop
    public boolean isShopManager(int shopID, String memberUserName) throws InnerLogicException {
        if (shopsMapper.get(shopID) == null)
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        if (shopsMapper.get(shopID).get(memberUserName) == null)
            return false;
        return shopsMapper.get(shopID).get(memberUserName) instanceof ShopManager;
    }

    public LinkedList<String> getManagementInfo(int shopID) throws InnerLogicException {
        if (shopsMapper.get(shopID) == null)
            throw new InnerLogicException("tried to get shopID: " + shopID + "but the shop does not exist");
        return new LinkedList<>(shopsMapper.get(shopID).keySet());
    }

    public LinkedList<Integer> getShopsInfo(String memberUserName) {
        if (membersMapper.get(memberUserName) == null)
            return new LinkedList<>();
        return new LinkedList<>(membersMapper.get(memberUserName).keySet());
    }

    // this is after we make sure there no shop exist
    public String shopOpened(int visitorID, int shopID) throws InnerLogicException {
        VisitorController visitorController = VisitorController.getInstance();
        Member founder = visitorController.getVisitorLoggedIn(visitorID);

        Role role = new ShopOwner(null);

        //check if the member does not have a shop already
        if (!membersMapper.containsKey(founder.getUsername())) {
            membersMapper.put(founder.getUsername(), new ConcurrentHashMap<>());
        }
        membersMapper.get(founder.getUsername()).put(shopID, role);

        //because its a new shop we dont have to check if exist
        if (shopsMapper.containsKey(shopID)) {
            throw new InnerLogicException("tried to open shopID: " + shopID + "but the shop already exist");
        }
        shopsMapper.put(shopID, new ConcurrentHashMap<>());
        shopsMapper.get(shopID).put(founder.getUsername(), role);

        return founder.getUsername();
    }
    public boolean isRoleExists(String userName){
        return membersMapper.containsKey(userName);
    }
    public HashMap<String,ShopOwner> removeShopOwner(int visitorId, String userName, int shopId) throws InnerLogicException {
        String shopOwnerUserName = getUserName(visitorId);
        ShopOwner shopOwner = getShopOwner(shopId,shopOwnerUserName);

        //get the shop owner to remove
        ShopOwner removedShopOwner = getShopOwner(shopId,userName);

        // it will remove the assignee if exists else it will throw exception
        shopOwner.removeAssignee(userName);

        //remove him from shop
        removeRoleFromShop(userName,shopId);

        //remove all assignees of the shop owner
        return removeShopOwnerHelper(userName,removedShopOwner,shopId);

    }
    private HashMap<String,ShopOwner> removeShopOwnerHelper(String userName,ShopOwner shopOwner, int shopId) throws InnerLogicException {
        HashMap<String,ShopOwner> outPut = new HashMap<>();
        //add himself to the list
        outPut.put(userName,shopOwner);
        List<String> assignees = shopOwner.getAssigneeUsernames();
        for (String a: assignees){
            //remove the current from shop
            Role r = removeRoleFromShop(a,shopId);
            //if the role is shop owner remove hes assignee's
            if(r instanceof ShopOwner) {
                ShopOwner shopOwner1 = (ShopOwner)r;
                //call recursive to the function
                outPut =  appendHash(outPut,removeShopOwnerHelper(a,shopOwner1,shopId));
            }
        }
        return outPut;
    }
    private HashMap<String,ShopOwner> appendHash(HashMap<String,ShopOwner> h1, HashMap<String,ShopOwner> h2){
        for (Map.Entry<String,ShopOwner> entry: h1.entrySet()){
            h2.put(entry.getKey(),entry.getValue());
        }
        return h2;
    }

}
