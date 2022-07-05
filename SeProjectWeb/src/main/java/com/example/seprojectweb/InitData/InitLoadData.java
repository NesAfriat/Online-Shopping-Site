package com.example.seprojectweb.InitData;
import com.example.seprojectweb.Communication.WebSocketController;
import com.example.seprojectweb.Domain.Market.Responses.*;
import com.example.seprojectweb.Service.MarketHandler.MarketHandler;
import com.example.seprojectweb.Service.VisitorHandler.VisitorHandler;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitLoadData {

    private VisitorHandler visitorHandler;
    private MarketHandler marketHandler;
    private WebSocketController webSocketController;
    private VisitorResponse[] visitors= new VisitorResponse[200];
    private Integer[] visitorsId= new Integer[100];
    private Integer[] shopsId= new Integer[100];
    private List<Integer[]> visitorshopsId = new ArrayList<>();
    private ShopResponse[] shops= new ShopResponse[100];
    private ProductResponse[] products= new ProductResponse[10];
    // private ShoppingCartResponse[] carts= new ShoppingCartResponse[1000];

    public InitLoadData(VisitorHandler visitorHandler, MarketHandler marketHandler, WebSocketController webSocketController){
        this.visitorHandler = visitorHandler;
        this.marketHandler = marketHandler;
        this.webSocketController = webSocketController;
    }

    public void writeDataOwnerMemebers(String filePath) throws IOException {
        String outputFile = filePath;
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        for (int i=0; i<100; i++) {
            bw.write(Integer.toString(visitorshopsId.get(i)[0]) + "," + Integer.toString(visitorshopsId.get(i)[1]));
            bw.newLine();
        }
        bw.close();
        fileWriter.close();
    }
    public void writeDataShopToBuy(String filePath) throws IOException {
        String outputFile = filePath;
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        for (int i=0; i<100; i++) {
            bw.write(Integer.toString(visitorsId[i]) + "," + Integer.toString(shopsId[0]) + "," + Integer.toString(products[0].getId()));
            bw.newLine();
        }
        bw.close();
        fileWriter.close();
    }
    public void writeDataloggedVisitors(String filePath) throws IOException {
        String outputFile = filePath;
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        for (int i=0; i<100; i++) {
            bw.write(Integer.toString(visitorsId[i]));
            bw.newLine();
        }
        bw.close();
        fileWriter.close();
    }
    public void writeDataUnLoggedVisitors(String filePath) throws IOException {
        String outputFile = filePath;
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        for (int i=100; i<200; i++) {
            bw.write(Integer.toString(visitors[i].getId()));
            bw.newLine();
        }
        bw.close();
        fileWriter.close();
    }

    public void initData(String saveCSVPath){
        int usersCount=0;

        try {
            //visit with 1000 users
            for(int i = 0; i<200; i++)
            {
                visitors[i] = visitorHandler.visitSystem().getValue();

            }
            //register 10 members with each visitor - total of 10K members
            for(int i =0; i<200; i++) {
                for (int j = 0; j < 1; j++) {
                    visitorHandler.register(visitors[i].getId(), "us".concat(Integer.toString(usersCount)), "123", Integer.toString(usersCount).concat("@post.bgu.ac.il"));
                    usersCount++;
                }
            }

            //login to the first 1k members
            for(int i = 0; i<100; i++)
            {
                visitorHandler.login(visitors[i].getId(),  "us".concat(Integer.toString(i)), "123",webSocketController);
            }

            //open shop with each logged member
            for(int i = 0; i<100; i++) {
                Response<ShopResponse> res = marketHandler.openShop(visitors[i].getId(), "0521234567", "1234123412341234", "s".concat(Integer.toString(i)), "description about shop", "location of shop");
                if (res.isErrorOccurred()) {
                    System.out.println(res.getErrorMessage());
                } else {
                    shops[i] = res.getValue();
                    if (shops[i] != null && visitors[i] != null) {
                        shopsId[i] = shops[i].getId();
                        visitorsId[i] = visitors[i].getId();


                    }
                }
            }
            //add 400 products to the first shop
            products[0]= marketHandler.addProduct(visitors[0].getId(), shops[0].getId(), "prod", 400,30, "product description", "some category").getValue();
//            boolean firstShopProds= true;
//            //add 1000 products for each shop
//            for(int i=0; i<100;i++)
//            {
//                for(int j=0; j<1; j++)
//                {
//                    if(firstShopProds)
//                        products[j]= marketHandler.addProduct(visitors[i].getId(), shops[i].getId(), "p".concat(Integer.toString(j)), 10,30, "product description", "some category").getValue();
//                    else
//                        marketHandler.addProduct(visitors[i].getId(), shops[i].getId(), "p".concat(Integer.toString(j)), 10,30, "product description", "some category");
//                }
//                firstShopProds= false;
//            }
//            //increase 1 of the products quantity by 1M.
//            marketHandler.addProductAmount(visitors[0].getId(),shops[0].getId(),products[0].getId(),100);
//
//            //Add to quantity of 1 of the product from each user
//            //And purchase cart for 1m times
//            for(int i=0; i<100;i++)
//            {
//                for(int j=0; j<1;j++)
//                {
//                    carts[i]= marketHandler.addProductToShoppingCart(visitors[i].getId(),shops[0].getId(),products[0].getId(),1).getValue();
//                    marketHandler.purchaseShoppingCart(visitors[i].getId(),"3453452452524534","22/06/2022","111","israel","bs","street");
//                }
//            }
//            //logout all of the members
//            for(int i=0; i<100;i++)
//            {
//                visitorHandler.logout(visitors[i].getId());
//            }


            //create csv files
            for(int i=0; i<100;i++)
            {
                Integer[] tmp =new Integer[2];
                tmp[0]= visitorsId[i];
                tmp[1]= shopsId[i];
                visitorshopsId.add(tmp);
            }

            writeDataOwnerMemebers(saveCSVPath);
            writeDataShopToBuy("shopToBuy.csv");
            writeDataloggedVisitors("loggedVisitors.csv");
            writeDataUnLoggedVisitors("unLoggedVisitors.csv");
        }
        catch (Exception e){
            System.err.println("Failed to init data for load tests: " + e.getMessage());
            System.exit(1);
        }

    }
}
