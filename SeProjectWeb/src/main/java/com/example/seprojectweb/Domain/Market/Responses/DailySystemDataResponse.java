package com.example.seprojectweb.Domain.Market.Responses;

import com.example.seprojectweb.Domain.Market.SystemData.DailySystemData;

public class DailySystemDataResponse {

    public final String date;
    public final int visitorCount;
    public final int roleLessMembersCount;
    public final int shopManagersCount;
    public final int shopOwnerCount;
    public final int AdminCount;

    public DailySystemDataResponse(DailySystemData data) {
        this.date = data.getDate();
        this.visitorCount = data.getVisitorCount();
        this.roleLessMembersCount = data.getRoleLessMembersCount();
        this.shopManagersCount = data.getShopManagersCount();
        this.shopOwnerCount = data.getShopOwnerCount();
        AdminCount = data.getAdminCount();
    }


    public String getDate() {
        return date;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public int getRoleLessMembersCount() {
        return roleLessMembersCount;
    }

    public int getShopManagersCount() {
        return shopManagersCount;
    }

    public int getShopOwnerCount() {
        return shopOwnerCount;
    }

    public int getAdminCount() {
        return AdminCount;
    }
}
