package com.example.seprojectweb.Domain.Market;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RolePK2 implements Serializable {
    @Column(name = "member_user_name")
    private String memberUserName;
    @Column(name = "shop_id")
    private int shopID;

    public RolePK2(String memberUserName, int shopID) {
        this.memberUserName = memberUserName;
        this.shopID = shopID;
    }

    public RolePK2() {

    }

    public String getMemberUserName() {
        return memberUserName;
    }

    public void setMemberUserName(String memberUserName) {
        this.memberUserName = memberUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePK2)) return false;
        RolePK2 rolePK2 = (RolePK2) o;
        return getShopID() == rolePK2.getShopID() && Objects.equals(getMemberUserName(), rolePK2.getMemberUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberUserName(), getShopID());
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }


}
