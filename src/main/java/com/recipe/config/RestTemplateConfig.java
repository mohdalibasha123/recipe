package com.recipe.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.stream.Collectors;

@Configuration
public class RestTemplateConfig {

    Logger log = LoggerFactory.getLogger(RestTemplateConfig.class);
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .interceptors(requestInterceptor(), responseInterceptor())
                .errorHandler(new CustomResponseErrorHandler())
                .build();
    }

    private ClientHttpRequestInterceptor requestInterceptor() {
        return (request, body, execution) -> {

            log.info("URI: {}", request.getURI());
            log.info("Method: {}", request.getMethod());
            log.info("Request Headers: {}", request.getHeaders());
            log.info("Request Body: {}", new String(body));
            return execution.execute(request, body);
        };
    }

    private ClientHttpRequestInterceptor responseInterceptor() {
        return (request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            log.info("Response Status Code: {}", response.getStatusCode());
            log.info("Response Headers: {}", response.getHeaders());

            String responseBody = new BufferedReader(new InputStreamReader(response.getBody()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            log.info("Response Body: {}", responseBody);

            return response;
        };
    }

    private static class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
        Logger log = LoggerFactory.getLogger(CustomResponseErrorHandler.class);

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            log.info("Response error: {}", response.getStatusCode());
            super.handleError(response);
        }
    }
}
