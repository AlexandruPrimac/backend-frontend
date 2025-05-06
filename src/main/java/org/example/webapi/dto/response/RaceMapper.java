package org.example.webapi.dto.response;

import org.example.domain.Race;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface RaceMapper {
    RaceDto toRaceDto(Race race);
}
