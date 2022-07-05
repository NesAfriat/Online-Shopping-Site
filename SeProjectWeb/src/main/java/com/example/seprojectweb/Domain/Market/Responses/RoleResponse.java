package com.example.seprojectweb.Domain.Market.Responses;

public abstract class RoleResponse {
    //Holds the owner or managers members userNames
    public String assignorUsername;
    public String memberUserName;

    protected RoleResponse(String memberUserName, String assignorUsername) {
        this.memberUserName = memberUserName;
        this.assignorUsername = assignorUsername;
    }

    public String getAssignorUsername() {
        return assignorUsername;
    }
    public String getMemberUserNameUsername() {
        return memberUserName;
    }
}
