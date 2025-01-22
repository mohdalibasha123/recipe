package com.recipe.service.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.model.dto.recipe.RecipeSearchReq;
import com.recipe.model.dto.recipe.RecipeSearchRes;
import com.recipe.util.QueryParamSerializerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpoonacularRestService {

    private final String RECIPE_COMPLEX_SEARCH = "/recipes/complexSearch";

    @Value("${spoonacular.API_BASE_URL}")
    private String API_BASE_URL;

    @Value("${spoonacular.api_key}")
    private String API_KEY;


    private final RestTemplate restTemplate;

    public SpoonacularRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public RecipeSearchRes recipesComplexSearch(RecipeSearchReq req) {

        UriComponentsBuilder uriComponentsBuilder = getBaseUrl()
                .path("/recipes/complexSearch")
                .queryParam("apiKey", API_KEY);

//        uriComponentsBuilder
//                .queryParam("query", req.getRecipeName())
//                .queryParam("addRecipeInformation", req.isAddRecipeInformation())
//                .queryParam("minServings", req.getMinServings())
//                .queryParam("sort", req.getSort())
//                .queryParam("sortDirection", req.getSortDirection())
//                .queryParam("apiKey", API_KEY);
//
//        String intolerances = req.getIntolerances()
//                .stream()
//                .map(Enum::name)
//                .collect(Collectors.joining(","));
//
//        uriComponentsBuilder.queryParam("intolerances", intolerances);
        uriComponentsBuilder.query(QueryParamSerializerUtil.toQueryParams(req));

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        HashMap requestValues = objectMapper.convertValue(req, HashMap.class);

//        requestValues.forEach((k, v) -> {
//            uriComponentsBuilder.queryParam((String) k, v);
//        });


        try {
            ResponseEntity<RecipeSearchRes> responseEntity =
                    restTemplate.getForEntity(uriComponentsBuilder.build().toUri(), RecipeSearchRes.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Search recipes successful");
                return responseEntity.getBody();
            }

        } catch (Exception e) {
            log.error("Search recipes Throws Exception", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }



    private String getApiBaseUrl() {
        return API_BASE_URL;
    }

    private UriComponentsBuilder getBaseUrl() {
        return UriComponentsBuilder.fromUriString(getApiBaseUrl());
    }

}
