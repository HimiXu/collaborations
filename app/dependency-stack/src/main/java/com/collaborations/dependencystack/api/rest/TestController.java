package com.collaborations.dependencystack.api.rest;

import com.collaborations.dependencystack.domain.GithubRepository;
import com.collaborations.dependencystack.domain.GithubRepositoryDependency;
import com.collaborations.dependencystack.domain.GithubRepositoryDependencyRepository;
import com.collaborations.dependencystack.domain.GithubRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final GithubRepositoryRepository repositoryRepository;
    private final DatabaseClient client;
    private final GithubRepositoryDependencyRepository dependencyRepository;

    @GetMapping("/repositories")
    public Flux<GithubRepository> find() {
        return repositoryRepository.findAll();
    }

    @GetMapping("/{owner}/{name}/dependencies")
    public Flux<GithubRepository> findEdges(@PathVariable String owner, @PathVariable String name) {
        return client.sql("""
SELECT github_repository.*
FROM github_repository gr
JOIN github_repository_dependency grd 
ON gr.id = grd.from
JOIN github_repository
ON grd.to = gr.id
WHERE gr.owner = $1
AND gr.name = $2
""")
                .bind("$1", owner)
                .bind("$2",name)
                .fetch()
                .all()
                .cast(GithubRepository.class);
    }
}
