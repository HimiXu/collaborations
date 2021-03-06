package com.collaborations.dependencystack.domain;

public record GithubDependenciesResponse(Data data){
    public record Data(Repository repository){}
    public record Repository(Integer stargazerCount, String nameWithOwner, DependencyGraphManifests dependencyGraphManifests){}
    public record DependencyGraphManifests(Node2[] nodes){}
    public record Node2(Dependencies dependencies){}
    public record Dependencies(Node1[] nodes){}
    public record Node1(Repository repository){}
}
