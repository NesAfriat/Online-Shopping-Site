package Domain.Market;

import Domain.InnerLogicException;

import java.util.*;
import java.util.stream.Collectors;

public class ShopManager extends Role {

    private HashMap<Integer,Permissions> rolesInShop;        // key: Permission value: permission string name (??? neccessery??? TODO)

    public ShopManager(String assignorUsername) {
        super(assignorUsername);
        rolesInShop = new HashMap<>();
    }

    public void removePermission(int e, String assignorUN) throws InnerLogicException {
        if (!assignorUN.equals(getAssignorUsername())){
            throw new InnerLogicException("cannot set role to if ur not assignor of this user");
        }
        if(rolesInShop.containsKey(e)){
            rolesInShop.remove(e);
        }else{
            throw new InnerLogicException("this is never permitted");
        }
    }


    public HashMap<Integer, Permissions> getRolesInShop() {
        return rolesInShop;
    }

    public void setRolesInShop(HashMap<Integer, Permissions> rolesInShop) {
        this.rolesInShop = rolesInShop;
    }

    public void setPermission(int e, String assignorUN) throws InnerLogicException {
        if (!assignorUN.equals(getAssignorUsername())){
            throw new InnerLogicException("cannot set role to if ur not assignor of this user");
        }
        if(!rolesInShop.containsKey(e)){
            if (e == Permissions.PurchaseHistory.ordinal()) {
                rolesInShop.put(e, Permissions.PurchaseHistory);
            } else if(e == Permissions.Notification.ordinal()) {
                rolesInShop.put(e, Permissions.Notification);
            }
        }
    }

    public List<String> getPermissionsInShop() {
        return rolesInShop.values().stream().map(Enum::toString).collect(Collectors.toList());
    }

    public void isPermissionExists(Permissions e) throws InnerLogicException {
        if(!rolesInShop.containsKey(e)){
            throw new InnerLogicException("use: "+ getAssignorUsername() + " cannot extract this method");
        }
    }

}
