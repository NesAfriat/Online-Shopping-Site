package com.example.seprojectweb.InitData;

import com.example.seprojectweb.Domain.Market.MarketRepresentative;
import com.example.seprojectweb.Domain.Market.PermissionManagement;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import com.example.seprojectweb.Domain.Market.Responses.ShopResponse;
import com.example.seprojectweb.Domain.Market.Responses.VisitorResponse;
import com.example.seprojectweb.Service.MarketHandler.MarketHandler;
import com.example.seprojectweb.Service.SystemHandler.SystemHandler;
import com.example.seprojectweb.Service.VisitorHandler.VisitorHandler;

public class InitDataClass {
    public static void main(String[] args) {
        initData();
    }

    public static void initData(){

        VisitorHandler visitorHandler = new VisitorHandler();
        SystemHandler systemHandler = new SystemHandler();
        MarketHandler marketHandler = new MarketHandler();

        try {
            VisitorResponse admin = visitorHandler.visitSystem().getValue();
            visitorHandler.login(admin.getId(), MarketRepresentative.DEFAULT_ADMIN_USERNAME, MarketRepresentative.DEFAULT_ADMIN_PASSWORD, new InitDataMemberObserver());

            VisitorResponse visitor1 = visitorHandler.visitSystem().getValue();
            visitorHandler.register(visitor1.getId(), "u1", "123", "u1@post.bgu.ac.il");

            systemHandler.addAdmin(admin.getId(), "u1");

            visitorHandler.login(visitor1.getId(), "u1", "123", new InitDataMemberObserver());


            VisitorResponse visitor2 = visitorHandler.visitSystem().getValue();
            visitorHandler.register(visitor2.getId(), "u2", "123", "u2@post.bgu.ac.il");
            visitorHandler.login(visitor2.getId(), "u2", "123", new InitDataMemberObserver());

            VisitorResponse visitor3 = visitorHandler.visitSystem().getValue();
            visitorHandler.register(visitor3.getId(), "u3", "123", "u3@post.bgu.ac.il");
            visitorHandler.login(visitor3.getId(), "u3", "123", new InitDataMemberObserver());

            VisitorResponse visitor4 = visitorHandler.visitSystem().getValue();
            visitorHandler.register(visitor4.getId(), "u4", "123", "u4@post.bgu.ac.il");
            visitorHandler.login(visitor4.getId(), "u4", "123", new InitDataMemberObserver());

            VisitorResponse visitor5 = visitorHandler.visitSystem().getValue();
            visitorHandler.register(visitor5.getId(), "u5", "123", "u5@post.bgu.ac.il");
            visitorHandler.login(visitor5.getId(), "u5", "123", new InitDataMemberObserver());

            VisitorResponse visitor6 = visitorHandler.visitSystem().getValue();
            visitorHandler.register(visitor6.getId(), "u6", "123", "u6@post.bgu.ac.il");
            visitorHandler.login(visitor6.getId(), "u6", "123", new InitDataMemberObserver());

            ShopResponse s1 = marketHandler.openShop(visitor2.getId(), "0521234567", "1234123412341234", "s1", "description about s1","location of s1").getValue();

            marketHandler.addProduct(visitor2.getId(), s1.getId(), "Bamba", 10,30, "Bamba description", "snacks");

            marketHandler.assignShopManager(visitor2.getId(), "u3", s1.getId());

            marketHandler.setPermission(visitor2.getId(), s1.getId(), "u3", PermissionManagement.ManageInventory);

            marketHandler.assignShopOwner(visitor2.getId(), "u4", s1.getId());
            marketHandler.assignShopOwner(visitor2.getId(), "u5", s1.getId());

            visitorHandler.logout(visitor5.getId());
        }
        catch (Exception e){
            System.err.println("Failed to init data :(");
            System.exit(1);
        }

        System.exit(0);
    }
}
