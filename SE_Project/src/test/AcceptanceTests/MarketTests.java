package AcceptanceTests;

import Domain.Market.Responses.ProductResponse;
import Domain.Market.Responses.Response;
import Domain.Market.Responses.VisitorResponse;


public class MarketTests extends ProjectTests{
    Response<VisitorResponse> visitResponse2;
    Response<VisitorResponse> visitResponse3;
    Response<VisitorResponse> visitResponse4;
    Response<ProductResponse> newProductResponse2;
    Response<ProductResponse> newProductResponse3;
    ProductResponse productResponse2;
    ProductResponse productResponse3;
    String productName3= "FlyingIPhone";
    String description2= "Don't use at home";
    String category2= "Tech";

    MemberObserverForTests visitor2MemberObserver;
    VisitorResponse visitor2;

    MemberObserverForTests visitor3MemberObserver;
    VisitorResponse visitor3;

    MemberObserverForTests visitor4MemberObserver;
    VisitorResponse visitor4; //not with role
    String memberUsername1= "UserLogged";
    String memberUsername2= "User2";
    String memberUsername3= "User3";
    String memberUsername4= "User4";
    String notAMemberUserName= "tzvika";
    String invalidCreditNumber= "24052352235A34";
    String invalidPhoneNumber= "24010465A1";
    String shortPhoneNumber= "042424130";
    int notShopID= -1000;
    int negativeQuantity=-1;
    String productName2= "Red Hat";
    int illegalquantity= 0;
    double illegalPrice= -1;
    String notProductName= "Banana";
    int notProductID = -1000;
    String date= "05/04/2022";
    String cvs= "123";
    String country= "Israel";
    String city= "Ramat";
    String street= "Pozis";

}






