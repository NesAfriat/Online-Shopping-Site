package com.example.seprojectweb.Domain.Market;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Role{

    private String assignorUsername;

    private int shopID;

    private String memberID;

    @EmbeddedId
    private RolePK2 pk;


    public Role() {

    }

    public void setAssignorUsername(String assignorUsername) {
        this.assignorUsername = assignorUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return getShopID() == role.getShopID() && Objects.equals(getAssignorUsername(), role.getAssignorUsername()) && Objects.equals(getMemberID(), role.getMemberID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssignorUsername(), getShopID(), getMemberID());
    }

    public Role(String assignorUsername, int shopID, String memberID) {
        this.assignorUsername = assignorUsername;
        this.shopID = shopID;
        this.memberID = memberID;
        pk = new RolePK2();
        pk.setMemberUserName(memberID);
        pk.setShopID(shopID);
    }

    public int getShopID() {
        return shopID;
    }




    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getAssignorUsername() {
        return assignorUsername;
    }


}

