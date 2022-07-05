package com.example.seprojectweb.Service.SystemHandler;

import com.example.seprojectweb.Domain.Market.MarketRepresentative;
import com.example.seprojectweb.Domain.Market.Responses.DailySystemDataResponse;
import com.example.seprojectweb.Domain.Market.Responses.MemberResponse;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemHandler {
    MarketRepresentative marketRepresentative;

    public SystemHandler() {
        marketRepresentative = MarketRepresentative.getInstance();
    }

    public boolean initMarket() {
        return false;
    }

    public Response<MemberResponse> addAdmin(int visitorId, String userName){
        return marketRepresentative.addAdmin(visitorId, userName);
    }

    public Response<List<DailySystemDataResponse>> getSystemData(int visitorId, String startDate, String endDate){
        return marketRepresentative.getSystemData(visitorId, startDate, endDate);
    }


    //TODO: payment and delivery init


}
