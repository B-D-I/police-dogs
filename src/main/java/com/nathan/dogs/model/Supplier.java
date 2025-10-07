package com.nathan.dogs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor()
public class Supplier extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}