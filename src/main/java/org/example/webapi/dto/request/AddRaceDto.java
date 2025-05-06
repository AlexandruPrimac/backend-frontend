package org.example.webapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddRaceDto(
        @NotBlank @Size(min = 1, max = 50) String name,
        @NotNull LocalDate date,
        @NotNull String track,
        @NotNull String location,
        @NotNull Double distance
) {

}
