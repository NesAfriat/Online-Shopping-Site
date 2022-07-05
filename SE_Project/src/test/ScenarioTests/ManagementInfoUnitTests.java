package ScenarioTests;

import AcceptanceTests.MemberObserverForTests;
import Domain.InnerLogicException;
import Domain.Market.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ManagementInfoUnitTests {
    RoleController rc = RoleController.getInstance();
    private static VisitorController visitorController = VisitorController.getInstance();
    private static Visitor visitor1 = visitorController.visitSystem();
    private static Visitor visitor2 = visitorController.visitSystem();
    private static Visitor visitor3 = visitorController.visitSystem();
    private static Visitor visitor4 = visitorController.visitSystem();
    Map<Integer, Map<String, Role>> shopsMapper;
    HashMap<String,Map<Integer,Role>> membersMapper;
    private static String useName1 = "tsvika";
    private static String password1 = "peek";

    private static String useName2 = "tsvika2";
    private static String password2 = "peek";

    private static String useName3 = "tsvika3";
    private static String password3 = "peek";

    private static String useName4 = "tsvika4";
    private static String password4 = "peek";

    ShopOwner sO1;
    ShopManager sM1;

    ShopOwner sO2;
    ShopManager sM2;



    int shopid1 = 1;
    int shopid2 = 2;




    @BeforeAll
    static void beforeAll() throws InnerLogicException {
        visitorController.register(visitor1.getId(), useName1, password1);
        visitorController.register(visitor2.getId(), useName2, password2);
        visitorController.register(visitor3.getId(), useName3, password3);
        visitorController.register(visitor4.getId(), useName4, password4);
    }

    @BeforeEach
    void setUp() throws InnerLogicException {
        MemberObserverForTests memberObserver1 = new MemberObserverForTests();
        MemberObserverForTests memberObserver2 = new MemberObserverForTests();
        MemberObserverForTests memberObserver3 = new MemberObserverForTests();
        MemberObserverForTests memberObserver4 = new MemberObserverForTests();
        visitorController.login(visitor1.getId(),useName1,password1, memberObserver1);
        visitorController.login(visitor2.getId(),useName2,password2, memberObserver2);
        visitorController.login(visitor3.getId(),useName3,password3, memberObserver3);
        visitorController.login(visitor4.getId(),useName4,password4, memberObserver4);

        sO1 = new ShopOwner(null);
        List<String> assingnees = new LinkedList<>();
        //set assignees of shop owner 1
        assingnees.add(useName2);
        assingnees.add(useName3);
        sO1.setAssigneeUsernames(assingnees);


        sM1 = new ShopManager(useName1);

        sO2 = new ShopOwner(useName1);

        sM2 = new ShopManager(useName3);

        shopsMapper = createManagerMapper(sO1,sM1,shopid1,useName1,useName2);


        membersMapper = createManagerMapper(shopid1,sO1,sM1,useName1,useName2);

        //add the shop owner user name 3 to shop 2
        addRoleToAShop(sO2,shopid2,useName3);
        //add user name 4 to shop assignee is user name 3
        addAssigneeManager(sO2,sM2,shopid2,useName4);




        rc.setShopsMapper(shopsMapper);
        rc.setMembersMapper(membersMapper);

    }

    /***
     *
     * @param r
     * @param shopId
     * @param useName the user name of r
     */
    void addRoleToAShop(Role r, int shopId,String useName){
        if(!membersMapper.containsKey(useName)) {
            membersMapper.put(useName, new HashMap<>());
        }
        if(!shopsMapper.containsKey(shopId)){
            shopsMapper.put(shopId,new HashMap<>());
        }
        membersMapper.get(useName).put(shopId,r);
        shopsMapper.get(shopId).put(useName,r);
    }

    /***
     *
     * @param sO the owner to add the assignee to
     * @param r
     * @param shopId
     * @param asignee the user name to add to shop and list of assignors
     */
    void addAssigneeManager(ShopOwner sO, Role r, int shopId, String asignee){
        List<String> n = sO.getAssigneeUsernames();
        n.add(asignee);
        addRoleToAShop(r,shopId,asignee);

    }
    Map<Integer, Map<String, Role>> createManagerMapper(ShopOwner sO,ShopManager sM,int shopId, String useName1,String useName2){
        Map<String, Role> rolesInShop = new ConcurrentHashMap<>();
        Map<Integer, Map<String, Role>> shopsMapper = new HashMap<>();
        List<String> appointments = new LinkedList<>();
        appointments.add(useName2);
        sO.setAssigneeUsernames(appointments);
        rolesInShop.put(useName1,sO);
        rolesInShop.put(useName2,sM);
        shopsMapper.put(shopId,rolesInShop);
        return shopsMapper;
    }
        HashMap<String,Map<Integer,Role>> createManagerMapper(int shopId, ShopOwner sO, ShopManager sM,String useName1,String useName2){
        HashMap<Integer,Role> roleInShops1 = new HashMap<>();
        HashMap<Integer,Role> roleInShops2 = new HashMap<>();
        roleInShops1.put(shopId,sO);
        roleInShops2.put(shopId,sM);
        HashMap<String,Map<Integer,Role>> membersMapper = new HashMap<>();
        membersMapper.put(useName1,roleInShops1);
        membersMapper.put(useName2,roleInShops2);
        return membersMapper;
    }

    @AfterEach
    void tearDown() throws InnerLogicException {
        visitorController.logout(visitor1.getId());
        visitorController.logout(visitor2.getId());
        visitorController.logout(visitor3.getId());
        visitorController.logout(visitor4.getId());
    }


    @Test
    @DisplayName("set manager permission success scenario")
    void setManagerPermissionSuccess() throws InnerLogicException {
        ShopManager sM = rc.setManagerPermission(visitor1.getId(),shopid1,0,useName2);
        assertTrue(sM.getPermissionsInShop().contains(Permissions.Notification.toString()));
        sM = rc.setManagerPermission(visitor3.getId(),shopid2,1,useName4);
        assertTrue(sM.getPermissionsInShop().contains(Permissions.PurchaseHistory.toString()));
    }
    @Test
    @DisplayName("set manager permission fail scenario => the manager have no rule in the shop")
    void setManagerPermissionFail1() throws InnerLogicException {
        Throwable exception1 = assertThrows(InnerLogicException.class,()-> rc.setManagerPermission(visitor1.getId(),shopid1,0,useName4));
        Throwable exception2 = assertThrows(InnerLogicException.class, ()-> rc.setManagerPermission(visitor3.getId(),shopid2,1,useName2));

    }
    @Test
    @DisplayName("set manager permission fail scenario => not a user assignor")
    void setManagerPermissionFai2() throws InnerLogicException {
        //assigned be owner user name 3
        ShopOwner s = new ShopOwner(useName3);
        //add him to a shop
        addRoleToAShop(s,shopid1,useName3);
        //add user name 4 to shop 1 the assignee is use name 3
        addAssigneeManager(s,new ShopManager(useName4),shopid1,useName4);
        rc.setShopsMapper(shopsMapper);
        rc.setMembersMapper(membersMapper);

        String expected = "Domain.InnerLogicException: cannot set role to if ur not assignor of this user";
        Throwable exception1 = assertThrows(InnerLogicException.class,()-> rc.setManagerPermission(visitor1.getId(),shopid1,0,useName4));
        assertEquals(expected,exception1.toString());
    }
    @Test
    @DisplayName("get a manager info success scenario")
    void getShopManagerInfoSuccess() throws InnerLogicException {
        ShopManager _sM1 = rc.getPermissionsOfMember(visitor1.getId(),useName2,shopid1);
        ShopManager _sM2 = rc.getPermissionsOfMember(visitor3.getId(),useName4,shopid2);
        assertAll(()-> assertEquals(_sM1,sM1),
                ()-> assertEquals(_sM2,sM2));
    }
    @Test
    @DisplayName("get a manager info Fail scenario => try to get shop manager that not have a role in the roles")
    void getShopManagerInfoFail() throws InnerLogicException {
        String expected_error = "Domain.InnerLogicException: ur dont have a role in this shop!";
        Throwable exception1 = assertThrows(InnerLogicException.class,()-> rc.getPermissionsOfMember(visitor1.getId(),useName4,shopid1));
        Throwable exception2 = assertThrows(InnerLogicException.class, ()-> rc.getPermissionsOfMember(visitor3.getId(),useName2,shopid2));
        assertAll(()->assertEquals(expected_error,exception1.toString()),
        ()-> assertEquals(expected_error,exception2.toString()));
    }
}