package com.moretorque.service;

import com.moretorque.limit.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NhtsaService {
    private static final String NHTSA_API_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/decodevin/";
    private final RestTemplate restTemplate;

    @Autowired
    public NhtsaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RateLimit(5) // 5 requests per minute
    public String decodeVin(String vin) {
        String url = NHTSA_API_URL + vin + "?format=json";
        return restTemplate.getForObject(url, String.class);
    }
}
