package com.collaborations.dependencystack.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class GithubRepositoryDependency {
    @Id
    private Long id;
    private Long from;
    private Long to;
}
