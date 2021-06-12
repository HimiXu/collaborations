package com.collaborations.dependencystack.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SourceCodeRepositoryDao extends ReactiveCrudRepository<SourceCodeRepository, Long> {
}
