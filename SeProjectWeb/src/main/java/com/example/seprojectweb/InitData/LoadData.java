package com.example.seprojectweb.InitData;

import com.example.seprojectweb.Communication.WebSocketController;
import com.example.seprojectweb.Domain.InnerLogicException;
import com.example.seprojectweb.Domain.Market.*;
import com.example.seprojectweb.Domain.Market.Responses.*;
import com.example.seprojectweb.Service.MarketHandler.MarketHandler;
import com.example.seprojectweb.Service.SystemHandler.SystemHandler;
import com.example.seprojectweb.Service.VisitorHandler.VisitorHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LoadData {

    private final VisitorHandler visitorHandler;
    private final SystemHandler systemHandler;
    private final MarketHandler marketHandler;
    private final WebSocketController webSocketController;

    public LoadData(VisitorHandler visitorHandler, SystemHandler systemHandler, MarketHandler marketHandler, WebSocketController webSocketController) {
        this.visitorHandler = visitorHandler;
        this.systemHandler = systemHandler;
        this.marketHandler = marketHandler;
        this.webSocketController = webSocketController;
    }

    public boolean loadData(String path) throws IOException, InnerLogicException {
        String data = getData(path);
        String[] categories = data.split("\\r?\\n");
        String[] toRegister = categories[0].split("\t");
        String[] admins = categories[1].split("\t");
        String[] shops = categories[2].split("\t");
        String[] products = categories[3].split("\t");
        String[] managers = categories[4].split("\t");
        String[] owners = categories[5].split("\t");
        String[] permissions = categories[6].split("\t");


        //cleanDB();

        ArrayList<Response<VisitorResponse>> visitorResponses = new ArrayList<>();
        ArrayList<Response<MemberResponse>> memberResponses = new ArrayList<>();
        ArrayList<Response<VisitorResponse>> loginResponses = new ArrayList<>();
        marketHandler.initMarket();
        ArrayList<Response<VisitorResponse>> toLogOutAndLeave = new ArrayList<>();
        Response<VisitorResponse> adminRess = visitorHandler.visitSystem();
        VisitorResponse admin = adminRess.getValue();
        toLogOutAndLeave.add(adminRess);
        Response<VisitorResponse> adminResponse = visitorHandler.login(admin.getId(), MarketRepresentative.DEFAULT_ADMIN_USERNAME, MarketRepresentative.DEFAULT_ADMIN_PASSWORD, webSocketController);
        if (adminResponse.isErrorOccurred()){
            throw new InnerLogicException(adminResponse.getErrorMessage());
        }


        // visit then register
        for (int i = 1; i < toRegister.length; i++){
            String[] userDetails = toRegister[i].split(",");
            Response<VisitorResponse> visitorResponse = visitorHandler.visitSystem();
            toLogOutAndLeave.add(visitorResponse);
            visitorResponses.add(visitorResponse);
            int visitorId = visitorResponse.getValue().getId();
            Response<MemberResponse> memberResponse = visitorHandler.register(visitorId, userDetails[0], userDetails[1], userDetails[2]);
            memberResponses.add(memberResponse);
            Response<VisitorResponse> loginResponse = visitorHandler.login(visitorId, userDetails[0], userDetails[1], webSocketController);
            loginResponses.add(loginResponse);
        }

        validateResponses(visitorResponses);
        validateResponses(loginResponses);
        validateResponses(memberResponses);

        ArrayList<Response<MemberResponse>> adminResponses = new ArrayList<>();
        // add admins
        for (int i = 1; i < admins.length; i++){
            String username = admins[i];
            Response<MemberResponse> adminRes = systemHandler.addAdmin(admin.getId(), username);
            adminResponses.add(adminRes);
        }

        validateResponses(adminResponses);

        // add shops
        ArrayList<Response<ShopResponse>> shopResponses = new ArrayList<>();
        for (int i = 1; i < shops.length; i++){
            String[] shopDetails = shops[i].split(",");
            int shopOwnerVisitorId = visitorResponses.get(Integer.parseInt(shopDetails[0]) - 1).getValue().getId();
            Response<ShopResponse> s1 = marketHandler.openShop(shopOwnerVisitorId, shopDetails[1], shopDetails[2], shopDetails[3], shopDetails[4],shopDetails[5]);
            shopResponses.add(s1);
        }

        validateResponses(shopResponses);
        // add products
        ArrayList<Response<ProductResponse>> productResponses = new ArrayList<>();
        for (int i = 1; i < products.length; i++){
            String[] productDetails = products[i].split(",");
            int shopOwnerVisitorId = visitorResponses.get(Integer.parseInt(productDetails[0]) - 1).getValue().getId();
            int shopId = shopResponses.get(Integer.parseInt(productDetails[1]) -1).getValue().getId();
            Response<ProductResponse> productResponse = marketHandler.addProduct(shopOwnerVisitorId, shopId, productDetails[2], Integer.parseInt(productDetails[3]),Integer.parseInt(productDetails[4]), productDetails[5], productDetails[6]);
            productResponses.add(productResponse);
        }

        validateResponses(productResponses);

        // add managers
        ArrayList<Response<RoleResponse>> assignManagersResponses = new ArrayList<>();
        for (int i = 1; i < managers.length; i++){
            String[] managerDetails = managers[i].split(",");
            int shopOwnerVisitorId = visitorResponses.get(Integer.parseInt(managerDetails[0]) - 1).getValue().getId();
            int shopId = shopResponses.get(Integer.parseInt(managerDetails[2]) -1).getValue().getId();
            Response<RoleResponse> roleResponse = marketHandler.assignShopManager(shopOwnerVisitorId, managerDetails[1], shopId);
            assignManagersResponses.add(roleResponse);
        }

        validateResponses(assignManagersResponses);

        // add owners
        ArrayList<Response<RoleResponse>> assignOwnersResponses = new ArrayList<>();
        for (int i = 1; i < owners.length; i++){
            String[] ownerDetails = owners[i].split(",");
            int shopOwnerVisitorId = visitorResponses.get(Integer.parseInt(ownerDetails[0]) - 1).getValue().getId();
            int shopId = shopResponses.get(Integer.parseInt(ownerDetails[2]) -1).getValue().getId();
            Response<RoleResponse> roleResponse = marketHandler.assignShopOwner(shopOwnerVisitorId, ownerDetails[1], shopId);
            assignOwnersResponses.add(roleResponse);
        }

        validateResponses(assignOwnersResponses);

        // add Permissions
        ArrayList<Response<ShopManagerResponse>> permissionResponses = new ArrayList<>();
        for (int i = 1; i < permissions.length; i++) {
            String[] permissionDetails = permissions[i].split(",");
            int shopOwnerVisitorId = visitorResponses.get(Integer.parseInt(permissionDetails[0]) - 1).getValue().getId();
            int shopId = shopResponses.get(Integer.parseInt(permissionDetails[2]) - 1).getValue().getId();
            Response<ShopManagerResponse> permissionResponse = marketHandler.setPermission(shopOwnerVisitorId, shopId, permissionDetails[1], Integer.parseInt(permissionDetails[3]));
            permissionResponses.add(permissionResponse);
        }

        validateResponses(permissionResponses);

        //logout
        ArrayList<Response<VisitorResponse>> logoutResponses = new ArrayList<>();
        for (int i = 0; i < toLogOutAndLeave.size(); i++){
            int toLogoutVisitorId = toLogOutAndLeave.get(i).getValue().getId();
            Response<VisitorResponse> logoutResponse = visitorHandler.logout(toLogoutVisitorId);
            logoutResponses.add(visitorHandler.leaveSystem(toLogoutVisitorId));
            logoutResponses.add(logoutResponse);
        }

        validateResponses(logoutResponses);

        return true;
    }



    private <T> void validateResponses(ArrayList<Response<T>> Responses) throws InnerLogicException {
        for (Response<T> response : Responses){
            if (response.isErrorOccurred()){
                throw new InnerLogicException(response.getErrorMessage());
            }
        }
    }

    private String getData(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, "UTF-8");
        return str;
    }

}
