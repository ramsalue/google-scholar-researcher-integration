package edu.univ.scientometrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.univ.scientometrics.config.ApiConfig;
import edu.univ.scientometrics.exception.ApiException;
import edu.univ.scientometrics.model.ApiResponseRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoogleScholarApiClient implements ApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ApiConfig apiConfig;

    public GoogleScholarApiClient(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ApiResponseRecord get(Map<String, String> parameters) {
        try {
            String url = buildUrl(parameters);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new ApiException(
                        "API request failed with status: " + response.statusCode(),
                        response.statusCode()
                );
            }

            return objectMapper.readValue(
                    response.body(),
                    ApiResponseRecord.class
            );

        } catch (IOException e) {
            throw new ApiException(
                    "Failed to parse API response: " + e.getMessage(),
                    e,
                    500
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(
                    "API request was interrupted: " + e.getMessage(),
                    e,
                    500
            );
        }
    }

    private String buildUrl(Map<String, String> parameters) {
        parameters.put("engine", apiConfig.getEngine());
        parameters.put("api_key", apiConfig.getApiKey());

        String queryString = parameters.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));

        return apiConfig.getBaseUrl() + "?" + queryString;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}