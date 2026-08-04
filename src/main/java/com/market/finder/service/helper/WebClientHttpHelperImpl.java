package com.market.finder.service.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientHttpHelperImpl implements HttpHelperService {

    private static final Logger logger = LoggerFactory.getLogger(WebClientHttpHelperImpl.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public WebClientHttpHelperImpl(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String get(String url) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            logger.error("HTTP GET request failed for URL {}: {}", url, e.getMessage());
            throw e;
        }
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType) {
        String json = get(url);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, responseType);
        } catch (Exception e) {
            logger.error("Failed to deserialize HTTP response from URL {}: {}", url, e.getMessage());
            throw new IllegalArgumentException("JSON deserialization error", e);
        }
    }
}
