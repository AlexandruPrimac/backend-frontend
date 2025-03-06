package org.example.webapi.dto.request;

import org.example.webapi.dto.response.RaceDto;

import java.util.List;

public record PatchCarRaceDto(int id, List<RaceDto>races) {
}
