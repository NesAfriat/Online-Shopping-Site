package Domain.Market.Responses;

import Domain.Market.ShopManager;

import java.util.List;

public class ShopManagerResponse extends RoleResponse{

    public List<String> permissions;

    public ShopManagerResponse(String memberUserName, ShopManager assignManager) {
        super(memberUserName, assignManager.getAssignorUsername());
        permissions = assignManager.getPermissionsInShop();
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
