package Domain.Market.Responses;

import Domain.Market.Member;

public class MemberResponse {

    private String username;

    public MemberResponse(Member member) {
        this.username = member.getUsername();
    }

    public String getUsername() {
        return username;
    }
}
