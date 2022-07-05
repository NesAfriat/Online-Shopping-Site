package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.AssignAgreement;

import java.util.HashSet;
import java.util.Set;

public class AssignAgreementResponse {
    private final Set<String> approvers;
    private final int id;
    private final int shopId;
    private final String toAssignMember;
    private final String initiatorAssignor;

    public AssignAgreementResponse(AssignAgreement assignAgreement) {
        approvers = new HashSet<>();
        this.id = assignAgreement.getId();
        this.shopId = assignAgreement.getShopId();
        this.toAssignMember = assignAgreement.getToAssignMember();
        this.initiatorAssignor = assignAgreement.getInitiatorAssignor();
        approvers.addAll(assignAgreement.getApprovers());
    }

    public Set<String> getApprovers() {
        return approvers;
    }

    public int getId() {
        return id;
    }

    public int getShopId() {
        return shopId;
    }

    public String getToAssignMember() {
        return toAssignMember;
    }

    public String getInitiatorAssignor() {
        return initiatorAssignor;
    }
}
