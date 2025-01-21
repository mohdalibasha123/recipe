package com.recipe.service.client;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class SpoonacularRestService {

//    private final RestTemplate restTemplate;

    @Value("${spoonacular.API_BASE_URL}")
    private String API_BASE_URL;

    @Value("${spoonacular.api_key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public SpoonacularRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }




    private String getAPI_BASE_URL(){
        return API_BASE_URL;
    }

}
