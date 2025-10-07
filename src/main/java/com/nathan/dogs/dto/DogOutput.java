package com.nathan.dogs.dto;

import com.nathan.dogs.model.CurrentStatus;
import com.nathan.dogs.model.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DogOutput {

    private String name;
    private String breed;
    private String badgeId;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate dateAcquired;
    private CurrentStatus currentStatus;
    private Set<String> kennellingCharacteristics;
}
