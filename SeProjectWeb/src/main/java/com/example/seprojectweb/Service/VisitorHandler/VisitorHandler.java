package com.example.seprojectweb.Service.VisitorHandler;

import com.example.seprojectweb.Domain.Market.MarketRepresentative;
import com.example.seprojectweb.Domain.Market.Notifications.IMemberObserver;
import com.example.seprojectweb.Domain.Market.Responses.BidResponse;
import com.example.seprojectweb.Domain.Market.Responses.MemberResponse;
import com.example.seprojectweb.Domain.Market.Responses.Response;
import com.example.seprojectweb.Domain.Market.Responses.VisitorResponse;

import com.example.seprojectweb.Service.ServiceUtils;
import org.springframework.stereotype.Service;

@Service
public class VisitorHandler {
    MarketRepresentative marketRepresentative;

    public VisitorHandler() {
        marketRepresentative = MarketRepresentative.getInstance();
    }

    //returns visitor ID
    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.m91jizn0v7bz
    public Response<VisitorResponse> visitSystem() {
        return marketRepresentative.visitSystem();
    }

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.1y8zdrlvc1y5
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.b0wrdaa47eqx
    public Response<VisitorResponse> leaveSystem(int visitorId) {
        return marketRepresentative.leaveSystem(visitorId);
    }

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.7yavqg1icq0n
    public Response<MemberResponse> register(int visitorId, String username, String password, String email) {
        if (!ServiceUtils.isValidUsername(username) || !ServiceUtils.isValidPassword(password)) {
            return new Response<>("invalid username or password");
        }
        return marketRepresentative.register(visitorId, username, password);
    }

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.pfnewjuuehz
    public Response<VisitorResponse> login(int visitorId, String username, String password, IMemberObserver memberObserver) {
        if (!ServiceUtils.isValidUsername(username) || !ServiceUtils.isValidPassword(password)) {
            return new Response<>("invalid username or password");
        }

        Response<VisitorResponse> ret =  marketRepresentative.login(visitorId, username, password, memberObserver);

        return ret;
    }

    //Use Case doc:
    //https://docs.google.com/document/d/1ym3oqWMYDzjnqnnojm17IJ9Njn7X2d3vOYrVyuYXykU/edit#bookmark=id.lclpkag44rg6
    public Response<VisitorResponse> logout(int visitorId) {
        return marketRepresentative.logout(visitorId);
    }


}
