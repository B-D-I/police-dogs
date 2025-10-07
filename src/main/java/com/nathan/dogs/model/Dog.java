package com.nathan.dogs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor()
public class Dog extends BaseEntity{

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String breed;

    // May get more than 1 dog from the same supplier
    @NotNull
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(unique = true)
    private String badgeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private LeavingReason leavingReason;

    private LocalDate birthDate;

    private LocalDate dateAcquired;

    private LocalDate leavingDate;

    // Store unique characteristics in separate table
    @ElementCollection
    @CollectionTable(
            name = "dog_kennel_characteristics",
            joinColumns = @JoinColumn(name = "dog_id")
    )
    @Column(name = "characteristic")
    private Set<String> kennellingCharacteristics = new HashSet<>();
}
