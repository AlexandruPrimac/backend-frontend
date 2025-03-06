package org.example.webapi.dto.response;

import java.util.List;

public record CarDto(int id, String brand, String model, String image, List<RaceDto> races) {

}
