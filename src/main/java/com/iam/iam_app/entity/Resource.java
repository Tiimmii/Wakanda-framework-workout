package com.iam.iam_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "resources")
@Getter
@Setter
@SuperBuilder
public class Resource extends BaseEntity {

    @NotBlank(message = "Resource name cannot be blank")
    private String name;

    @NotBlank(message = "Resource type cannot be blank")
    private String type;

    @NotBlank(message = "Resource URL cannot be blank")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false, referencedColumnName = "email")
    @NotNull(message = "Owner is required")
    private User owner;

    public Resource() {
        super();
    }

    @Override
    protected String generateCustomGuid() {
        return "_res_" + java.util.UUID.randomUUID();
    }
}
