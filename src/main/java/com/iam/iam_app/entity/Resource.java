package com.iam.iam_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import org.wakanda.framework.entity.BaseEntity;

@Entity
@Table(name = "resources")
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Resource extends BaseEntity<Integer> {

    @NotBlank(message = "Resource name cannot be blank")
    private String name;

    @NotBlank(message = "Resource type cannot be blank")
    private String type;

    @NotBlank(message = "Resource URL cannot be blank")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false, referencedColumnName = "email")
    @NotNull(message = "Owner is required")
    private User owner;
}
