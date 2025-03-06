package org.example.webapi.dto.request;

import org.example.domain.CarCategory;

public record PatchCarDto(int id, String brand, String model, double engine, int horsepower, int year, CarCategory category) {
}
