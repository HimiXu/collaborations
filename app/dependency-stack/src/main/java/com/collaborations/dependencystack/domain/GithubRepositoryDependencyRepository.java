package com.collaborations.dependencystack.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface GithubRepositoryDependencyRepository extends ReactiveCrudRepository<GithubRepositoryDependency, Long> {
}
