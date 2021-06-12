package com.collaborations.dependencystack.domain;


import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
@Table("repositories")
public class SourceCodeRepository {
    @Id
    private Long id;
    @NotEmpty
    private String owner;
    @NotEmpty
    private String name;
    @PositiveOrZero
    private Integer stars;
    @URL
    private String url;
    private RepositoryHosting hosting;
}
