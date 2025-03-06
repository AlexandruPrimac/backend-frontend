package org.example.webapi.dto.response;

import org.example.domain.Car;
import org.example.domain.CarRaces;
import org.example.domain.Race;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CarMapper {

    @Mapping(target = "races", source = "races", qualifiedByName = "toRaceDtoList")
    CarDto toCarDto(Car car);

    Car toCar(CarDto carDto);

    @Named("toRaceDtoList")
    default List<RaceDto> toRaceDtoList(List<CarRaces> carRaces) {
        if (carRaces == null) {
            return Collections.emptyList();
        }

        return carRaces.stream()
                .map(carRace -> {
                    Race race = carRace.getRace();
                    return new RaceDto(
                            race.getId(),
                            race.getName(),
                            race.getDate(),
                            race.getLocation(),
                            race.getImage(),
                            race.getTrack()
                    );
                })
                .collect(Collectors.toList());
    }

}
