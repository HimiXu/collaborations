package com.collaborations.dependencystack.domain;

import com.collaborations.dependencystack.domain.github.dependencies.GithubDependenciesResponse;
import reactor.core.publisher.Mono;

public interface GithubServices {
    Mono<GithubDependenciesResponse> getRepositoryDependencies(String owner, String name);
}
