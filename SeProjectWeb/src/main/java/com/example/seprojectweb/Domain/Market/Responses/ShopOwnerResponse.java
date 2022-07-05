package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.ShopOwner;

import java.util.LinkedList;
import java.util.List;

public class ShopOwnerResponse extends RoleResponse {

    public final List<String> AssigneeUsernames;

    public ShopOwnerResponse(String memberUserName, ShopOwner assignOwner) {
        super(memberUserName, assignOwner.getAssignorUsername());
        this.AssigneeUsernames = new LinkedList<>();
        AssigneeUsernames.addAll(assignOwner.getAssigneeUsernames());
    }
    public List<String> getAssigneeUsernames() {
        return AssigneeUsernames;
    }
}
