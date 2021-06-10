package com.collaborations.dependencystack.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
public class GithubRepository {
    @Id
    private Long id;
    @NotEmpty
    private String owner;
    @NotEmpty
    private String name;
    @PositiveOrZero
    private Integer stars;
}
