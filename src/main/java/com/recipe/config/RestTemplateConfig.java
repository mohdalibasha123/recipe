package com.recipe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    Logger log = LoggerFactory.getLogger(RestTemplateConfig.class);
    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate template = new RestTemplate(factory);
        template.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return template;
    }
}
