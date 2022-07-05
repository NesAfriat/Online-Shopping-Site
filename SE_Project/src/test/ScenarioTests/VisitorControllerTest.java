package ScenarioTests;

import AcceptanceTests.MemberObserverForTests;
import Domain.InnerLogicException;
import Domain.Market.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitorControllerTest {

    private static VisitorController visitorController;

    @BeforeAll
    static void registerUsers(){
        visitorController = VisitorController.getInstance();
        Visitor visitor = visitorController.visitSystem();
        try {
            visitorController.register(visitor.getId(), "tsvika", "peek");
        } catch (InnerLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMember() {
        try{
            Member member = visitorController.getMember("tsvika");
            assertEquals("tsvika", member.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMember_not_exist() {
        try{
            Member member = visitorController.getMember("notTsvika");
            fail();
        } catch (InnerLogicException e) {
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void visitSystem() {
        Visitor visitor = visitorController.visitSystem();
        try{
            assertEquals(visitorController.getVisitor(visitor.getId()).getId(), visitor.getId());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void leaveSystem() {
        Visitor visitor = visitorController.visitSystem();
        try{
            visitorController.leaveSystem(visitor.getId());
            try {
                visitorController.getVisitor(visitor.getId());
                fail();
            }catch (InnerLogicException e){
                assertTrue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void leaveSystem_session_already_ended() {
        Visitor visitor = visitorController.visitSystem();
        try{
            visitorController.leaveSystem(visitor.getId());
            try {
                visitorController.leaveSystem(visitor.getId());
                fail();
            }catch (InnerLogicException e){
                assertTrue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void login() {
        // setup
        int visitorId = visitorController.visitSystem().getId();
        try {
            MemberObserverForTests memberObserver = new MemberObserverForTests();
            Visitor visitor = visitorController.login(visitorId, "tsvika", "peek", memberObserver);
            Member loggedInMember = visitor.getLoggedIn();
            assertEquals("tsvika", loggedInMember.getUsername());
            visitorController.logout(visitorId);
        }catch (Exception e) {
            fail();
        }
    }

    @Test
    void login_bad_username() {
        // setup
        int visitorId = visitorController.visitSystem().getId();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        try {
            Visitor visitor = visitorController.login(visitorId, "notTsvika", "peek", memberObserver);
            fail();
        }catch (InnerLogicException e) {
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void login_bad_password() {
        // setup
        int visitorId = visitorController.visitSystem().getId();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        try {
            Visitor visitor = visitorController.login(visitorId, "tsvika", "notPeek", memberObserver);
            fail();
        }catch (InnerLogicException e) {
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void login_already_logged_in() {
        int visitor1Id = visitorController.visitSystem().getId();
        int visitor2Id = visitorController.visitSystem().getId();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        MemberObserverForTests memberObserver2 = new MemberObserverForTests();
        try {
                visitorController.login(visitor1Id, "tsvika", "peek", memberObserver);
            try {
                visitorController.login(visitor2Id, "tsvika", "peek", memberObserver2);
            }catch (InnerLogicException e){
                visitorController.logout(visitor1Id);
                assertTrue(true);
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        fail();
    }

    @Test
    void logout() {
        int visitorId = visitorController.visitSystem().getId();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        try {
            Visitor visitor = visitorController.login(visitorId, "tsvika", "peek", memberObserver);
            assertNotNull(visitor.getLoggedIn());
            assertNull(visitorController.logout(visitorId).getLoggedIn());
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    void logout_bad_not_logged_in() {
        int visitorId = visitorController.visitSystem().getId();
        try {
            visitorController.logout(visitorId);
            fail();
        }catch (InnerLogicException e) {
            assertTrue(true);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void registerNewMember() throws InnerLogicException {
        // setup
        int visitorId = visitorController.visitSystem().getId();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        String uniqueUsername = "abu_yoyo";
        String password = "sak_kemah";
        try {
            Member myMember = visitorController.register(visitorId, uniqueUsername, password);
            assertEquals(visitorController.getMember(uniqueUsername).getUsername(), myMember.getUsername());
            // teardown
            visitorController.login(visitorId, uniqueUsername, password, memberObserver);
            visitorController.unRegister(visitorId);

        }catch (Exception e){
            fail(e.getMessage());
        }


    }

    @Test
    void registerExistingMember() {
        // setup
        int visitorId = visitorController.visitSystem().getId();
        String existUsername = "tsvika";
        String password = "sak_kemah";
        try {
            visitorController.register(visitorId, existUsername, password);
            fail();
        }
        catch(InnerLogicException e){
            assertTrue(true);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }



    @Test
    void getVisitor() {
        Visitor visitor = visitorController.visitSystem();
        try {
            assertEquals(visitor,visitorController.getVisitor(visitor.getId()));
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getVisitor_bad_visitorId() {
        Visitor visitor = visitorController.visitSystem();
        try {
            visitorController.getVisitor(visitor.getId() + 10);
            fail();
        }catch (InnerLogicException e) {
            assertTrue(true);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getVisitorLoggedIn() {
        Visitor visitor = visitorController.visitSystem();
        MemberObserverForTests memberObserver = new MemberObserverForTests();
        try {
            visitorController.login(visitor.getId(),"tsvika", "peek", memberObserver);
            assertEquals(visitorController.getMember("tsvika"), visitor.getLoggedIn());
            visitorController.logout(visitor.getId());
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getVisitorLoggedIn_not_logger_in() {
        Visitor visitor = visitorController.visitSystem();
        try {
            visitorController.getVisitorLoggedIn(visitor.getId());
            fail();
        }catch (InnerLogicException e) {
            assertTrue(true);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void cartSaveForReLoginMember() {

        Visitor visitor = visitorController.visitSystem();
        try {
            int visitorId = visitor.getId();
            MemberObserverForTests memberObserver = new MemberObserverForTests();
            visitorController.login(visitor.getId(),"tsvika", "peek", memberObserver);
            Shop shop = ShopController.getInstance().openShop(visitorId,"0521234567","1234123412341234",
                    "hakolBeSheker","hakol po Yakar", "here");
            int shopId = shop.getShopId();
            Product product = shop.addProduct("bike",123,"bike",1,"bike");
            visitorController.addProductToShoppingCart(visitorId,shopId, product,1);
            visitorController.logout(visitorId);
            boolean isCartReset=visitor.getShoppingCart().getBaskets().isEmpty();
            assertTrue(isCartReset);
            MemberObserverForTests memberObserver2 = new MemberObserverForTests();
            visitorController.login(visitor.getId(),"tsvika", "peek", memberObserver2);
            boolean isReloaded = visitor.getShoppingCart().getBaskets().size() == 1;
            visitorController.leaveSystem(visitorId);
            assertTrue(isReloaded);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}