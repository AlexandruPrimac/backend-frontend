package org.example.webapi.dto.response;

import org.example.domain.Race;

public record RaceDto(int id, String name, String location, String image, String track) {
    public static RaceDto fromRace(final Race race){
        return new RaceDto(race.getId(), race.getName(), race.getLocation(), race.getImage(), race.getTrack());
    }
}
