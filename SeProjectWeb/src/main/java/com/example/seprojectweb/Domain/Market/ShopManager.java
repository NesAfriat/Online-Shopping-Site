package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;


import javax.persistence.*;
import java.util.*;


@Entity
@NamedQuery(name="ShopManager.findAll", query="SELECT s FROM ShopManager s")
public class ShopManager extends Role {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PermissionManagement permissionManagement;



    public ShopManager(String assignorUsername, int shopID, String memberID) {
        super(assignorUsername, shopID, memberID);
        this.permissionManagement = new PermissionManagement();
    }

    public ShopManager() {

    }

    public void removePermission(int e, String assignorUN) throws InnerLogicException {
        if (!assignorUN.equals(getAssignorUsername())) {
            throw new InnerLogicException("cannot set role to if ur not assignor of this user");
        }
        permissionManagement.removePermission(e);
    }



    public void setPermission(int e, String assignorUN) throws InnerLogicException {
        if (!assignorUN.equals(getAssignorUsername())){
            throw new InnerLogicException("cannot set permission to this user");
        }
        permissionManagement.setPermission(e);
    }

    public List<String> getPermissionsInShop() {
       return permissionManagement.getPermissionsInShop();
    }


    public void isPermissionExists(int e) throws InnerLogicException {
        permissionManagement.isPermissionExists(e,getAssignorUsername());
    }

}
