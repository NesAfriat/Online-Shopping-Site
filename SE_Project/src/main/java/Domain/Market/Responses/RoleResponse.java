package Domain.Market.Responses;

import Domain.Market.Member;

public abstract class RoleResponse {
    //Holds the owner or managers members userNames
    public String assignorUsername;
    public String memberUserName;

    protected RoleResponse(String memberUserName, String assignorUsername){
        this.memberUserName = memberUserName;
        this.assignorUsername = assignorUsername;
    }

    public String getAssignorUsername() {
        return assignorUsername;
    }
}
