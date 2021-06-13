package com.collaborations.dependencystack.api.rest;

import com.collaborations.dependencystack.domain.GithubServices;
import com.collaborations.dependencystack.domain.SourceCodeRepository;
import com.collaborations.dependencystack.domain.SourceCodeRepositoryDao;
import com.collaborations.dependencystack.domain.github.dependencies.GithubDependenciesResponse;
import com.collaborations.dependencystack.infrastructure.GithubServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final SourceCodeRepositoryDao repositoryRepository;
    private final GithubServices githubServices;
    private final DatabaseClient client;

    @GetMapping("/{owner}/{name}")
    public Mono<GithubDependenciesResponse> test(@PathVariable String owner, @PathVariable String name) {
        return githubServices.getRepositoryDependencies(owner, name);
    }

//    @GetMapping("/repositories")
//    public Flux<SourceCodeRepository> find() {
//        return repositoryRepository.findAll();
//    }
//
//    @GetMapping("/{owner}/{name}/dependencies")
//    public Flux<SourceCodeRepository> findEdges(@PathVariable String owner, @PathVariable String name) {
//        return client.sql("""
//SELECT github_repository.*
//FROM github_repository gr
//JOIN github_repository_dependency grd
//ON gr.id = grd.from
//JOIN github_repository
//ON grd.to = gr.id
//WHERE gr.owner = $1
//AND gr.name = $2
//""")
//                .bind("$1", owner)
//                .bind("$2",name)
//                .fetch()
//                .all()
//                .cast(SourceCodeRepository.class);
//    }
}
