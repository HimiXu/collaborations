package com.collaborations.dependencystack.domain.github;

import java.util.List;
import java.util.Set;

public record GithubRepositoryDependencies(Set<GithubRepositoryVertex> dependencies){}
