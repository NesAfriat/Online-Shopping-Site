package com.example.seprojectweb.UnitTests;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.PersistenceManager;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class removeShopOwnerTest {
    private Map<Integer, Map<String, Role>> shopsMapper= new HashMap<>();
    private Map<String, Map<Integer, Role>> membersMapper = new HashMap<>();
    RoleController rc = RoleController.getInstance();
    String userName1 = "riki1";
    String userName2 = "riki2";
    int shop1 = 1;
    int shop2 = 2;

    String userName3 = "riki3";
    String userName4 = "riki4";
    String userName5 = "riki5";
    String userName6 = "riki6";
    String userName7 = "riki7";



    @BeforeAll
    static void beforeAll() {
        PersistenceManager.setDBConnection("test");
    }

    @BeforeEach
    void setUp() throws InnerLogicException {
        setShop(userName1,shop1);
        setShopOwner(userName1,userName2,shop1);
        setShop(userName2,shop2);
        setShopOwner(userName2,userName3,shop2);
        setShopOwner(userName3,userName4,shop2);
        setShopOwner(userName4,userName5,shop2);
        setShopOwner(userName5,userName6,shop2);
        setShopOwner(userName4,userName7,shop2);
        rc.setShopsMapper(shopsMapper);
        rc.setMembersMapper(membersMapper);
    }

    @AfterEach
    void tearDown() {
        rc.setShopsMapper(new HashMap<>());
        rc.setShopsMapper(new HashMap<>());
    }

    @Test
    @DisplayName("remove owner Easy => Success")
    void removeShopOwner1() throws InnerLogicException {
        List<String> removed =  new ArrayList<>(rc.removeShopOwnerHelper(userName1,userName2,shop1).keySet());
        Assertions.assertEquals(((ShopOwner)(shopsMapper.get(shop1).get(userName1))).getAssigneeUsernames().size(),0);
        assertEquals(1, removed.size());
        assertEquals(removed.get(0),userName2);
    }
    @Test
    @DisplayName("remove owner Easy => hard")
    void removeShopOwner2() throws InnerLogicException {
        List <String> expected = List.of(userName3,userName4,userName5,userName6,userName7);

        List<String> removed = new ArrayList<>(rc.removeShopOwnerHelper(userName2, userName3, shop2).keySet());
        assertEquals(shopsMapper.get(shop2).size(),1);
        assertFalse(membersMapper.get(userName3).containsKey(shop2));
        assertFalse(membersMapper.get(userName4).containsKey(shop2));
        assertFalse(membersMapper.get(userName5).containsKey(shop2));
        assertFalse(membersMapper.get(userName6).containsKey(shop2));
        assertFalse(membersMapper.get(userName7).containsKey(shop2));
        assertEquals(expected.size(), removed.size());
        assertTrue(expected.containsAll(removed));
    }
    @Test
    @DisplayName("remove owner => fail the remover is not an owner assignee")
    void removeOwner3(){
        assertThrows(InnerLogicException.class,()-> rc.removeShopOwnerHelper(userName3, userName2, shop2));
    }

    public void setShopOwner(String username,String usernameToAssign, int shopID) throws InnerLogicException {

        if (!shopsMapper.containsKey(shopID)) {
            shopsMapper.put(shopID, new ConcurrentHashMap<>());
        }
        if (!membersMapper.containsKey(usernameToAssign)) {
            membersMapper.put(usernameToAssign, new ConcurrentHashMap<>());
        }
        ShopOwner owner = rc.createShopOwner(username,new Shop(2),new Member());
        shopsMapper.get(shopID).put(usernameToAssign, owner);
        membersMapper.get(usernameToAssign).put(shopID, owner);

        ShopOwner assignorOwnerRole = (ShopOwner)shopsMapper.get(shopID).get(username);
        if (assignorOwnerRole != null) {
            assignorOwnerRole.addAssignee(usernameToAssign);
            PersistenceManager.updateSession(assignorOwnerRole);
        }
    }

    public void setShop (String username, int shopID){
        ShopOwner owner = rc.createShopOwner(null,new Shop(1),new Member());
        HashMap <String, Role> t1 = new HashMap<>();
        t1.put(username, owner);
        shopsMapper.put(shopID,t1);
        HashMap <Integer, Role> t2 = new HashMap<>();
        t2.put(shopID,owner);
        membersMapper.put(username,t2);
    }
}