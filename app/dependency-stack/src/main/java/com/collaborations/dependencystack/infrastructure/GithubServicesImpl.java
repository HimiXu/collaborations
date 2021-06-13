package com.collaborations.dependencystack.infrastructure;

import com.collaborations.dependencystack.domain.GithubServices;
import com.collaborations.dependencystack.domain.GithubDependenciesResponse;
import com.collaborations.dependencystack.domain.github.GithubRepositoryDependencies;
import com.collaborations.dependencystack.domain.github.GithubRepositoryVertex;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

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
    public Mono<GithubRepositoryDependencies> getRepositoryDependencies(String owner, String name) {
        return loadQuery(getDependenciesQueryPath)
            .flatMap(query -> GraphQLClient.builder()
                    .url(apiUrl)
                    .headers(getHeaders())
                    .build()
                    .query(query)
                    .variables(JSON.create()
                            .put("owner", owner)
                            .put("name", name))
                    .fetch(GithubDependenciesResponse.class))
                .map(this::map);
    }

    private GithubRepositoryDependencies map(GithubDependenciesResponse response) {
        return new GithubRepositoryDependencies(
                Arrays.stream(response.data()
                .repository()
                .dependencyGraphManifests()
                .nodes())
                .flatMap(node2 -> Arrays.stream(node2
                        .dependencies()
                        .nodes())
                        .map(GithubDependenciesResponse.Node1::repository)
                        .filter(Objects::nonNull)
                        .map(this::map))
                        .collect(Collectors.toSet()));
    }

    private GithubRepositoryVertex map(GithubDependenciesResponse.Repository repository) {
        var owner = repository.nameWithOwner().split("/")[0];
        var name = repository.nameWithOwner().split("/")[1];
        return new GithubRepositoryVertex(owner, name, repository.stargazerCount());
    }



}
