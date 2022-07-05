package com.example.seprojectweb.AcceptanceTests;

import com.example.seprojectweb.Domain.Market.Responses.*;
import org.junit.jupiter.api.AfterEach;
import com.example.seprojectweb.Domain.Market.Notifications.Notification;
import com.example.seprojectweb.Domain.Market.Shop;
import com.example.seprojectweb.Domain.Market.ShopOwner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import static org.junit.Assert.*;


public class AssignAgreementTests extends ProjectTests{

    VisitorResponse visitorShopOwner1;
    VisitorResponse visitorShopOwner2;
    VisitorResponse visitorAssignee;
    MemberResponse shopOwner1;
    MemberResponse shopOwner2;
    MemberResponse assignee;
    MemberObserverForTests shopOwner1MailBox;
    MemberObserverForTests shopOwner2MailBox;
    MemberObserverForTests assigneeMailBox;
    ShopResponse shop;

    @BeforeEach
    void setUp() {
        visitorShopOwner1 = visitSystem().getValue();
        visitorShopOwner2 = visitSystem().getValue();
        visitorAssignee = visitSystem().getValue();

        register(visitorShopOwner1.getId(), "ShopOwner1", "pass", "email@email.email");
        register(visitorShopOwner1.getId(), "ShopOwner2", "pass", "email@email.email");
        register(visitorShopOwner1.getId(), "Assignee", "pass", "email@email.email");


        assigneeMailBox = new MemberObserverForTests();
        shopOwner1MailBox = new MemberObserverForTests();
        shopOwner2MailBox = new MemberObserverForTests();
        shopOwner1 = login(visitorShopOwner1.getId(), "ShopOwner1", "pass",shopOwner1MailBox ).getValue().getLoggedIn();
        shopOwner2 = login(visitorShopOwner2.getId(), "ShopOwner2", "pass",shopOwner2MailBox ).getValue().getLoggedIn();
        assignee = login(visitorAssignee.getId(), "Assignee", "pass", assigneeMailBox).getValue().getLoggedIn();
        shop = openShop(visitorShopOwner1.getId(), "0521234567", "1111222233334444", "shop", "shopshop", "over there").getValue();
    }

    @AfterEach
    void tearDown() {

        closeShop(visitorShopOwner1.getId(), shop.getId());

        logout(visitorShopOwner1.getId());
        leaveSystem(visitorShopOwner1.getId());

        logout(visitorShopOwner2.getId());
        leaveSystem(visitorShopOwner2.getId());

        logout(visitorAssignee.getId());
        leaveSystem(visitorAssignee.getId());


    }

    @Test
    void sanityTest()
    {
        Assertions.assertTrue(true);
    }

    @Test
    void assignFirstShopOwnerSuccess()
    {
        Response<AssignAgreementResponse> assignAgreement = initAssignShopOwner(visitorShopOwner1.getId(), shopOwner2.getUsername(), shop.getId());
        if (assignAgreement.isErrorOccurred()){
            Assertions.fail(assignAgreement.getErrorMessage());
        }

        // verify shopowner2 is now shopowner
        Response<List<RoleResponse>> rolesList = getShopRoleInfo(visitorShopOwner1.getId(), shop.getId());
        if (rolesList.isErrorOccurred()){
            Assertions.fail(rolesList.getErrorMessage());
        }

        boolean isShopOwner2 = false;
        for (RoleResponse roleResponse : rolesList.getValue()){
            if (roleResponse instanceof ShopOwnerResponse && roleResponse.memberUserName.equals(shopOwner2.getUsername())){
                isShopOwner2 = true;
            }
        }

        if (!isShopOwner2){
            Assertions.fail("ShopOwner2 is not shop owner");
        }


        // verify assignagreement is deleted
        Response<List<AssignAgreementResponse>> assignAgreements = getAllShopAssignAgreements(visitorShopOwner1.getId(), shop.getId());
        if (assignAgreements.isErrorOccurred()){
            Assertions.fail(assignAgreements.getErrorMessage());
        }

        Assertions.assertTrue(assignAgreements.getValue().isEmpty());

        // verify shopowner1 got notification about need to approve
        List<Notification> notifications = shopOwner1MailBox.getNotifications();
        boolean gotShopOwner1Notificatio = false;
        for (Notification notification : notifications){
            String msg= notification.getMessage();
            if (msg.contains("New Assign Agreement created for") && msg.contains(shopOwner2.getUsername())){
                gotShopOwner1Notificatio =true;
            }
        }
        Assertions.assertTrue(gotShopOwner1Notificatio);

        // verify shopowner2 got notification about got assigneed
        notifications = shopOwner2MailBox.getNotifications();
        boolean gotShopOwner2Notification = false;
        for (Notification notification : notifications){
            String msg= notification.getMessage();
            if (msg.contains("Congratz")){
                gotShopOwner2Notification =true;
            }
        }
        Assertions.assertTrue(gotShopOwner2Notification);
    }

    @Test
    void assignShopOwnerTwoApproversSuccess() {
        // first assign another shop owner except founder
        initAssignShopOwner(visitorShopOwner1.getId(), shopOwner2.getUsername(), shop.getId());
        // now init request for another shop owner
        Response<AssignAgreementResponse> assignAgreement = initAssignShopOwner(visitorShopOwner1.getId(), assignee.getUsername(), shop.getId());
        if (assignAgreement.isErrorOccurred()){
            Assertions.fail(assignAgreement.getErrorMessage());
        }
        // verify the one initiated is listed as aprover
        Set<String> aprovers = assignAgreement.getValue().getApprovers();
        if (!aprovers.contains(shopOwner1.getUsername())){
            Assertions.fail("initiator is not listed as aprover");
        }

        // verify he is not yet shop owner
        Response<List<RoleResponse>> rolesList = getShopRoleInfo(visitorShopOwner1.getId(), shop.getId());
        if (rolesList.isErrorOccurred()){
            Assertions.fail(rolesList.getErrorMessage());
        }

        for (RoleResponse roleResponse : rolesList.getValue()){
            if (roleResponse instanceof ShopOwnerResponse && roleResponse.memberUserName.equals(assignee.getUsername())){
                Assertions.fail("assignee is shop owner but shopowner2 didn't aprove him");
            }
        }

        // check if shopowner2 got notification
        List<Notification> notifications = shopOwner2MailBox.getNotifications();
        boolean gotShopOwner2Notification = false;
        for (Notification notification : notifications){
            String msg= notification.getMessage();
            if (msg.contains("New Assign Agreement created for") && msg.contains(assignee.getUsername())){
                gotShopOwner2Notification =true;
            }
        }
        Assertions.assertTrue(gotShopOwner2Notification);

        // aprove with the other shop owner
        assignAgreement = approveAssignAgreement(visitorShopOwner2.getId(), assignee.getUsername(), shop.getId());
        if (assignAgreement.isErrorOccurred()){
            Assertions.fail(assignAgreement.getErrorMessage());
        }

        aprovers = assignAgreement.getValue().getApprovers();
        if (!aprovers.contains(shopOwner2.getUsername())){
            Assertions.fail("shopowner2 is not listed as aprover");
        }

        // verify he is shop owner now
        boolean isShopOwnerAssignee = false;
        rolesList = getShopRoleInfo(visitorShopOwner1.getId(), shop.getId());
        if (rolesList.isErrorOccurred()){
            Assertions.fail(rolesList.getErrorMessage());
        }

        for (RoleResponse roleResponse : rolesList.getValue()){
            if (roleResponse instanceof ShopOwnerResponse && roleResponse.memberUserName.equals(assignee.getUsername())){
                isShopOwnerAssignee = true;
            }
        }

        Assertions.assertTrue(isShopOwnerAssignee);

        // verify shopowner2 got notification about got assigneed
        notifications = assigneeMailBox.getNotifications();
        boolean gotAssigneeNotification = false;
        for (Notification notification : notifications){
            String msg= notification.getMessage();
            if (msg.contains("Congratz")){
                gotAssigneeNotification =true;
            }
        }
        Assertions.assertTrue(gotAssigneeNotification);

        // verify no more assign agreements
        Response<List<AssignAgreementResponse>> assignAgreemetns = getAllShopAssignAgreements(visitorShopOwner1.getId(), shop.getId());
        if (assignAgreemetns.isErrorOccurred()){
            Assertions.fail(assignAgreemetns.getErrorMessage());
        }

        Assertions.assertTrue(assignAgreemetns.getValue().isEmpty());

    }

    // first there is 2 shop owners who need to approve
    // only one of them approved, then the other one get removed
    // so now the assign should be approved because all of shop owners approved the request
    @Test
    void assignShopOwnerAfterRemoval() {
        // first assign another shop owner except founder
        initAssignShopOwner(visitorShopOwner1.getId(), shopOwner2.getUsername(), shop.getId());
        // now init request for another shop owner
        Response<AssignAgreementResponse> assignAgreement = initAssignShopOwner(visitorShopOwner1.getId(), assignee.getUsername(), shop.getId());
        if (assignAgreement.isErrorOccurred()){
            Assertions.fail(assignAgreement.getErrorMessage());
        }
        // verify the one initiated is listed as aprover
        Set<String> aprovers = assignAgreement.getValue().getApprovers();
        if (!aprovers.contains(shopOwner1.getUsername())){
            Assertions.fail("initiator is not listed as aprover");
        }

        // verify he is not yet shop owner
        Response<List<RoleResponse>> rolesList = getShopRoleInfo(visitorShopOwner1.getId(), shop.getId());
        if (rolesList.isErrorOccurred()){
            Assertions.fail(rolesList.getErrorMessage());
        }

        for (RoleResponse roleResponse : rolesList.getValue()){
            if (roleResponse instanceof ShopOwnerResponse && roleResponse.memberUserName.equals(assignee.getUsername())){
                Assertions.fail("assignee is shop owner but shopowner2 didn't aprove him");
            }
        }

        // check if shopowner2 got notification
        List<Notification> notifications = shopOwner2MailBox.getNotifications();
        boolean gotShopOwner2Notification = false;
        for (Notification notification : notifications){
            String msg= notification.getMessage();
            if (msg.contains("New Assign Agreement created for") && msg.contains(assignee.getUsername())){
                gotShopOwner2Notification =true;
            }
        }
        Assertions.assertTrue(gotShopOwner2Notification);

        // now remove shop owner 2
        Response<List<ShopOwnerResponse>> shopOwnersRemovedRes = removeShopOwner(visitorShopOwner1.getId(), shopOwner2.getUsername(), shop.getId());
        if (shopOwnersRemovedRes.isErrorOccurred()){
            Assertions.fail(assignAgreement.getErrorMessage());
        }

        List<ShopOwnerResponse> shopOwnersRemoved = shopOwnersRemovedRes.getValue();
        Assertions.assertTrue(shopOwnersRemoved.size() == 1);
        ShopOwnerResponse shopOwnerResponse = shopOwnersRemoved.get(0);
        Assertions.assertTrue(shopOwnerResponse.memberUserName.equals(shopOwner2.getUsername()));
        Assertions.assertTrue(shopOwnerResponse.assignorUsername.equals(shopOwner1.getUsername()));


        // now verify assignee is shop owner now
        boolean isShopOwnerAssignee = false;
        rolesList = getShopRoleInfo(visitorShopOwner1.getId(), shop.getId());
        if (rolesList.isErrorOccurred()){
            Assertions.fail(rolesList.getErrorMessage());
        }

        for (RoleResponse roleResponse : rolesList.getValue()){
            if (roleResponse instanceof ShopOwnerResponse && roleResponse.memberUserName.equals(assignee.getUsername())){
                isShopOwnerAssignee = true;
            }
        }

        Assertions.assertTrue(isShopOwnerAssignee);

        // verify assignee got notification about got assigneed
        notifications = assigneeMailBox.getNotifications();
        boolean gotAssigneeNotification = false;
        for (Notification notification : notifications){
            String msg= notification.getMessage();
            if (msg.contains("Congratz")){
                gotAssigneeNotification =true;
            }
        }
        Assertions.assertTrue(gotAssigneeNotification);

        // verify no more assign agreements
        Response<List<AssignAgreementResponse>> assignAgreemetns = getAllShopAssignAgreements(visitorShopOwner1.getId(), shop.getId());
        if (assignAgreemetns.isErrorOccurred()){
            Assertions.fail(assignAgreemetns.getErrorMessage());
        }

        Assertions.assertTrue(assignAgreemetns.getValue().isEmpty());

    }

}
