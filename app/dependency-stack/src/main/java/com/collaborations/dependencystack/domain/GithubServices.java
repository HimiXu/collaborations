package com.collaborations.dependencystack.domain;

import com.collaborations.dependencystack.domain.github.GithubRepositoryDependencies;
import reactor.core.publisher.Mono;

public interface GithubServices {
    Mono<GithubRepositoryDependencies> getRepositoryDependencies(String owner, String name);
}
