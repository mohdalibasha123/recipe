package com.recipe.service.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.model.dto.recipe.RecipeSearchReq;
import com.recipe.model.dto.recipe.RecipeSearchRes;
import com.recipe.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
public class SpoonacularRestService {

    @Value("${spoonacular.API_BASE_URL}")
    private String API_BASE_URL;

    @Value("${spoonacular.api_key}")
    private String API_KEY;

    private final RestTemplate restTemplate;


    public SpoonacularRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public RecipeSearchRes recipesComplexSearch(RecipeSearchReq req) {

        UriComponentsBuilder uriComponentsBuilder = buildUriWithParams(req);

        try {
            ResponseEntity<RecipeSearchRes> responseEntity =
                    restTemplate.getForEntity(uriComponentsBuilder.build().toUri(), RecipeSearchRes.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Search recipes rest call completed successfully");
                return responseEntity.getBody();
            }

        } catch (HttpClientErrorException e) {
            log.error("Search recipes rest call failed with HttpClientErrorException", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("Search recipes rest call failed with exception", e);
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    private UriComponentsBuilder buildUriWithParams(RecipeSearchReq req) {

        UriComponentsBuilder uriComponentsBuilder = getBaseUrl()
                .path("/recipes/complexSearch")
                .queryParam("apiKey", API_KEY);

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map fieldMap = objectMapper.convertValue(req, Map.class);

        fieldMap.forEach((key, value) ->
                uriComponentsBuilder.queryParam((String) key, ConvertUtil.convertValueToString(value))
        );
        return uriComponentsBuilder;
    }

    private String getApiBaseUrl() {
        return API_BASE_URL;
    }

    private UriComponentsBuilder getBaseUrl() {
        return UriComponentsBuilder.fromUriString(getApiBaseUrl());
    }
}
