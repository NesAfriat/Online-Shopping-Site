package com.example.seprojectweb.Domain.PaymentAdapter;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HTTPPayment implements IPayment{
    public final String url = "https://cs-bgu-wsep.herokuapp.com/";
    public RestTemplate restTemplate;

    public HTTPPayment(){
        restTemplate=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action_type", "handshake");

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(map, headers);

        ResponseEntity<Void> res;
        res = restTemplate.exchange(
                this.url,
                HttpMethod.POST,
                requestEntity,
                Void.class);

    }
    @Override
    public int chargeCreditCard(String creditCardNumber, String date, String holder, String cvs, double cost) {
        restTemplate=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        String[] dateArr = date.split("-");
        String month = dateArr[0];
        String year = dateArr[1];
        map.add("action_type", "pay");
        map.add("card_number", creditCardNumber);
        map.add("month", month);
        map.add("year", year);
        map.add("holder", holder);
        map.add("ccv", cvs);
        map.add("id", "208420737");

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(map, headers);

        ResponseEntity<String> res;
        res = restTemplate.exchange(
                this.url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        return Integer.parseInt(Objects.requireNonNull(res.getBody()));


    }

    @Override
    public void cancelPayment(int paymentId) {
        restTemplate=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action_type", "cancel_pay");
        map.add("transaction_id", ""+paymentId);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(map, headers);

        ResponseEntity<Void> res;
        res = restTemplate.exchange(
                this.url,
                HttpMethod.POST,
                requestEntity,
                Void.class);

    }
}
