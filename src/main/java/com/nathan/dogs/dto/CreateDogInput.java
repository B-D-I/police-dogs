package com.nathan.dogs.dto;

import com.nathan.dogs.model.CurrentStatus;
import com.nathan.dogs.model.Gender;
import com.nathan.dogs.model.LeavingReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDogInput {

    @NotBlank
    private String name;
    @NotBlank
    private String breed;
    @NotBlank
    private String supplier;
    @NotNull
    private CurrentStatus currentStatus;
    private String badgeId;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate dateAcquired;
    private LocalDate leavingDate;
    private LeavingReason leavingReason;
    private Set<String> kennellingCharacteristics;
}
