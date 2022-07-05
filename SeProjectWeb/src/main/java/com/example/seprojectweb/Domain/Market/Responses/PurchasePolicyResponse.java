package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.PurchasePolicies.PurchasePolicy;

public class PurchasePolicyResponse {
    private final int purchasePolicyId;
    private final String conditionDescription;

    public PurchasePolicyResponse(PurchasePolicy purchasePolicy) {
        this.purchasePolicyId = purchasePolicy.getId();
        this.conditionDescription = purchasePolicy.getCondition().getDescription();
    }

    public int getPurchasePolicyId() {
        return purchasePolicyId;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }
}
