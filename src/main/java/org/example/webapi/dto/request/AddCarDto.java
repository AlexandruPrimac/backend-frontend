package org.example.webapi.dto.request;

import jakarta.validation.constraints.*;
import org.example.domain.CarCategory;

public record AddCarDto(
        @NotBlank @Size(min = 1, max = 50) String brand,

        @NotNull @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Model must be alphanumeric") String model,

        @NotNull @DecimalMin(value = "0.5", message = "Engine size must be greater than 0.5L") Double engine,

        @NotNull @Min(value = 50, message = "Horsepower must be at least 50") @Max(value = 2000, message = "Horsepower cannot exceed 2000") Integer horsePower,

        @NotNull @Min(value = 1886, message = "Year must be after 1885") @Max(value = 2025, message = "Year cannot be later than 2025") Integer year,

        @NotNull CarCategory category
) {
}
