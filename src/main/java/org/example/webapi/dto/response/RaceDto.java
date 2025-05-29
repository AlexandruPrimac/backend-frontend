package org.example.webapi.dto.response;

import java.time.LocalDate;

public record RaceDto(int id,
                      String name,
                      LocalDate date,
                      String location,
                      String image,
                      String track) {
}
