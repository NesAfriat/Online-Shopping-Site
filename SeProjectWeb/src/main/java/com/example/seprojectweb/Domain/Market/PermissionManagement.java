package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.PersistenceManager;

import javax.persistence.*;
import java.util.*;

@Entity
public class PermissionManagement {

    //Who ever touch this need to change in ShopManager set/remove permission
    public static int Notification = 0;   // 0
    public static int PurchaseHistory = 1; // 1

    public static int ManageInventory = 2; // 2

    public static int BidApprover = 3; // 3



    public void setPermissionsInShop(Set<Integer> permissionsInShop) {
        this.permissionsInShop = permissionsInShop;
    }

    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> permissionsInShop;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionManagement(){
        permissionsInShop = new HashSet<>();
    }
    public String permissionToString(int s){
        if (s == PermissionManagement.Notification) {
            return "Notifications";
        } else if (s == PermissionManagement.PurchaseHistory) {
            return "PurchaseHistory";
        }
        else if (s == PermissionManagement.ManageInventory) {
            return "ManageInventory";
        }
        else if (s == PermissionManagement.BidApprover) {
            return "BidApprover";
        }
        return null;
    }
    public void isPermissionExists(int e, String assignorUserName) throws InnerLogicException {
        if (! permissionsInShop.contains(e)) {
            throw new InnerLogicException("user: " + assignorUserName + " cannot extract this method");
        }
    }

    public static int getNotification() {
        return Notification;
    }

    public static void setNotification(int notification) {
        Notification = notification;
    }

    public static int getPurchaseHistory() {
        return PurchaseHistory;
    }

    public static void setPurchaseHistory(int purchaseHistory) {
        PurchaseHistory = purchaseHistory;
    }

    public static int getManageInventory() {
        return ManageInventory;
    }

    public static void setManageInventory(int manageInventory) {
        ManageInventory = manageInventory;
    }

    public static int getBidApprover() {
        return BidApprover;
    }

    public static void setBidApprover(int bidApprover) {
        BidApprover = bidApprover;
    }


    public Set<Integer> getRolesInShop() {
        return  permissionsInShop;
    }

    public void setRolesInShop(Set<Integer> rolesInShop) {
        this. permissionsInShop = rolesInShop;
    }


    public List<String> getPermissionsInShop() {
        List<String> outPut = new LinkedList<>();
        for (int s:  permissionsInShop){
            permissionToString(s);
        }
        return outPut;
    }public void removePermission(int e) throws InnerLogicException {
        if ( permissionsInShop.contains(e)) {
            permissionsInShop.remove(e);
            PersistenceManager.update(this);
        } else {
            throw new InnerLogicException("this is never permitted");
        }
    }
    public void setPermission(int e){
        permissionsInShop.add(e);
        PersistenceManager.update(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionManagement)) return false;
        PermissionManagement that = (PermissionManagement) o;
        return getId().equals(that.getId()) && getRolesInShop().equals(that.getRolesInShop());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRolesInShop());
    }
}
