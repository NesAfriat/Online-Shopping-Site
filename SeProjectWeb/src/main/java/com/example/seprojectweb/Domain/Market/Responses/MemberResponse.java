package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.Member;

public class MemberResponse {

    private final String username;

    public MemberResponse(Member member) {
        this.username = member.getUsername();
    }

    public String getUsername() {
        return username;
    }
}
