package com.collaborations.dependencystack.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GithubRepositoryRepository extends ReactiveCrudRepository<GithubRepository, Long> {
}
