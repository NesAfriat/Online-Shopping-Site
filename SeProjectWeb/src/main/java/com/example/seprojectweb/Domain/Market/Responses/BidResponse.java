package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.Market.SpecialPurchase.Bid;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BidResponse {
    public int id;
    public String bidderUserName;
    public ProductResponse product;
    public int quantity;
    public List<String> approvers;

    public List<String> notApprovedYet;
    public double price;
    public BidResponse(Bid bid, int shopId) throws InnerLogicException {
        this.id = bid.getId();
        Member member = (Member) bid.getBidder();
        this.bidderUserName =  member.getUsername();
        this.product = new ProductResponse(shopId, bid.getProduct());
        this.approvers = new LinkedList<>(bid.getApprovers());
        this.notApprovedYet = new LinkedList<>();
        for(Map.Entry<String, Role> entry: bid.getRoleInShop().entrySet()){
            Role role = entry.getValue();
            if(role instanceof ShopOwner){
                if(!approvers.contains(entry.getKey())){
                    notApprovedYet.add(entry.getKey());
                }
            }
            else {
                ShopManager shopManager = (ShopManager) role;
                try {
                    shopManager.isPermissionExists(PermissionManagement.BidApprover);
                    if(!approvers.contains(entry.getKey())){
                        notApprovedYet.add(entry.getKey());
                    }
                }catch (InnerLogicException ignored){}

            }
        }
        this.price = bid.getPrice();
        this.quantity = bid.getQuantity();
    }

    public int getId() {
        return id;
    }

    public String getBidderUserName() {
        return bidderUserName;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<String> getApprovers() {
        return approvers;
    }

    public double getPrice() {
        return price;
    }

}
