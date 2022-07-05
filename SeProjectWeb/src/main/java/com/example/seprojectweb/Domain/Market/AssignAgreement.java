package com.example.seprojectweb.Domain.Market;

import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.PersistenceManager;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Entity
public class AssignAgreement {

    @ElementCollection
    private Set<String> approvers;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private int shopId;
    private String toAssignMember;
    private String initiatorAssignor;

    public AssignAgreement(int shopId, String toAssignmemberId, String initiatorAssignor) {
        this.shopId = shopId;
        this.toAssignMember = toAssignmemberId;
        this.initiatorAssignor = initiatorAssignor;
        approvers = new HashSet<>();
    }

    public AssignAgreement() {

    }

    public List<ShopOwner> getShopOwners(){
        return RoleController.getInstance().getShopOwners(this.shopId);
    }

    public boolean isAssignApproved(){
        for(ShopOwner shopOwner : getShopOwners()){
            if(!approvers.contains(shopOwner.getMemberID()))
                return false;
        }
        return true;
    }

    public void approveAssign(String approver) throws InnerLogicException {
        approvers.add(approver);
    }


    public Set<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(Set<String> approvers) {
        this.approvers = approvers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getToAssignMember() {
        return toAssignMember;
    }

    public void setToAssignMember(String toAssignMember) {
        this.toAssignMember = toAssignMember;
    }

    public String getInitiatorAssignor() {
        return initiatorAssignor;
    }

    public void setInitiatorAssignor(String initiatorAssignor) {
        this.initiatorAssignor = initiatorAssignor;
    }

}
