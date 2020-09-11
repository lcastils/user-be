package com.api.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.api.user.dto.exception.ExceptionDTO;
import com.api.user.exception.BusinessException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        ResponseErrorHandler responseErrorHandler = new DefaultResponseErrorHandler() {
            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                String errorMessage = getErrorMessageFromInputStream(response.getBody());
                log.error(new StringBuilder().append("Error from :").append(url.toURL()).append("Error Message: ")
                        .append(errorMessage).toString());
                throwSimpleException(convertToDTO(errorMessage));
            }

            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return HttpStatus.INTERNAL_SERVER_ERROR.equals(statusCode) || HttpStatus.BAD_REQUEST.equals(statusCode);
            }
        };
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

    private static String getErrorMessageFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder strBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            strBuilder.append(line);
        }
        return strBuilder.toString();
    }

    private static ExceptionDTO convertToDTO(String jsonResponse) throws IOException {
        ExceptionDTO exception;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        exception = mapper.readValue(jsonResponse, ExceptionDTO.class);

        return exception;
    }

    private static void throwSimpleException(ExceptionDTO exception) {
        if (exception != null) {
                throw new BusinessException(exception.getMessage());
        }
    }
}
