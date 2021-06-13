package com.collaborations.dependencystack.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GraphQLClient {

    private final Logger logger = LoggerFactory.getLogger(GraphQLClient.class);

    private final WebClient webClient;
    private String query;
    private ObjectNode variables;

    @Builder
    private GraphQLClient(String url, HttpHeaders headers) {
        webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(existing -> existing.addAll(headers))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public GraphQLClient query(String query) {
        this.query = query;
        return this;
    }

    public GraphQLClient variables(ObjectNode variables) {
        this.variables = variables;
        return this;
    }

    private String getBody() {
        Assert.notNull(query, "Query cannot be null");
        var json = JSON.create().put("query", query);
        if (variables != null && !variables.isEmpty()) json.put("variables", variables.toString());
        return json.toString();
    }

    public <T> Mono<T> fetch(Class<T> clazz) {
        return retrieve().bodyToMono(clazz);
    }
    public <T> Mono<T> fetch(ParameterizedTypeReference<T> parameterizedTypeReference) {
        return retrieve().bodyToMono(parameterizedTypeReference);
    }

    private WebClient.ResponseSpec retrieve() {
        return webClient.post()
                .bodyValue(getBody())
                .retrieve();
    }

}
