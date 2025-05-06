package org.example.webapi.dto.request;

import java.time.LocalDate;

public record PatchRaceDto(int id, String name, LocalDate date, String track, String location, double distance) {
}
