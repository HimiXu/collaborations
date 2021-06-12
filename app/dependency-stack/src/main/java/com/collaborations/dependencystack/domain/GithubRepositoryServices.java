package com.collaborations.dependencystack.domain;

import com.collaborations.dependencystack.domain.github.dependencies.GithubDependenciesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GithubRepositoryServices {

    @Value("${github.graphql.url}")
    private String baseUrl;
    @Value("${github.auth.token}")
    private String token;
    private static final String accept = "application/vnd.github.hawkgirl-preview+json";

    private String getQuery(String owner, String name) {
        var json = new ObjectMapper();
        return json.createObjectNode()
                .put("query","""
    query getDependencies($owner: String! , $name: String!) {
        repository(owner: $owner, name: $name){
            nameWithOwner
            stargazerCount
            dependencyGraphManifests {
                nodes {
                    dependencies {
                        nodes {
                            repository {
                                nameWithOwner
                                stargazerCount
                            }
                        }
                    }
                }
            }
        }
    }
                """)
                .put("variables", json.createObjectNode()
                        .put("owner", owner)
                        .put("name", name)
                        .toString()
                )
                .toString();
    }
    public Mono<GithubDependenciesResponse> getDependencies(String owner, String name){
        var webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, accept)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .build();
        var query = getQuery(owner, name);
        return webClient.post()
                .bodyValue(query)
                .retrieve()
                .bodyToMono(GithubDependenciesResponse.class);
    }
}
