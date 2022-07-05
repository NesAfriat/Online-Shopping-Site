package com.example.seprojectweb.Domain.DeliveryAdapter;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HTTPDelivery implements IDelivey{
    public final String url = "https://cs-bgu-wsep.herokuapp.com/";
    public RestTemplate restTemplate;
    public HTTPDelivery(){
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
    public int bookDelivery(String name, String country, String city, String address, String zip) {
        restTemplate=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action_type", "supply");
        map.add("name", name);
        map.add("address", address);
        map.add("city", city);
        map.add("country", country);
        map.add("zip", zip);

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
    public void cancelDelivery(int deliveryId) {
        restTemplate=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action_type", "cancel_supply");
        map.add("transaction_id", ""+deliveryId);

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
