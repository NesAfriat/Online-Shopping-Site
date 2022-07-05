package Service.SystemHandler;

import Domain.Market.MarketRepresentative;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SystemHandler {
    MarketRepresentative marketRepresentative;
    public SystemHandler()
    {
        marketRepresentative = MarketRepresentative.getInstance();
    }
    public boolean initMarket(){
        throw new NotImplementedException();
    }


    //TODO: payment and delivery init



}
