package com.collaborations.dependencystack.domain.github;

public record GithubRepositoryVertex(
        String name,
        String owner,
        Integer stars
){}
