package com.collaborations.dependencystack.infrastructure;

import com.collaborations.dependencystack.domain.GithubServices;
import com.collaborations.dependencystack.domain.github.dependencies.GithubDependenciesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.BaseStream;

@Service
public class GithubServicesImpl implements GithubServices {

    private static final Logger logger = LoggerFactory.getLogger(GithubServicesImpl.class);

    private static final String graphqlFilesPrefix = "graphql/github/";
    private static final String getDependenciesQueryPath = graphqlFilesPrefix+"get-dependencies-query.graphql";

    private static final String apiUrl = "https://api.github.com/graphql";

    @Value("${github.auth.token}")
    private String accessToken;

    private static HttpHeaders getHeaders(String accessToken) {
        var headers = new HttpHeaders();
        headers.put(HttpHeaders.ACCEPT, List.of("application/vnd.github.hawkgirl-preview", "application/json"));
        headers.put(HttpHeaders.AUTHORIZATION, List.of("Bearer "+accessToken));
        return headers;
    }

    private HttpHeaders getHeaders() {
        return getHeaders(accessToken);
    }

    private static Mono<String> loadQuery(String queryPath) {
        return Flux.using(() -> Files.lines(Paths.get(new ClassPathResource(queryPath).getURI())),
                        Flux::fromStream,
                        BaseStream::close)
                .reduce("", (file , line) -> file+line);
    }

    @Override
    public Mono<GithubDependenciesResponse> getRepositoryDependencies(String owner, String name) {
        return loadQuery(getDependenciesQueryPath)
            .flatMap(query -> GraphQLClient.builder()
                    .url(apiUrl)
                    .headers(getHeaders())
                    .build()
                    .query(query)
                    .variables(JSON.create()
                            .put("owner", owner)
                            .put("name", name))
                    .fetch(GithubDependenciesResponse.class));
    }



}
