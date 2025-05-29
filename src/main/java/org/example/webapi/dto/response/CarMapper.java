package org.example.webapi.dto.response;

import org.example.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CarMapper {

    CarDto toCarDto(Car car);

    default CarWithRacesDto toCarWithRacesDto(Car car) {
        return new CarWithRacesDto(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getImage(),
                toRaceDtoList(car.getRaces())
        );
    }

    @Named("toRaceDtoList")
    default List<RaceDto> toRaceDtoList(List<CarRaces> carRaces) {
        if (carRaces == null) {
            return Collections.emptyList();
        }
        return carRaces.stream()
                .filter(carRace -> carRace.getRace() != null)
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

    default CarWithSponsorsDto toCarWithSponsorsDto(Car car) {
        return new CarWithSponsorsDto(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getImage(),
                toSponsorDtoList(car.getSponsors())
        );
    }

    @Named("toRaceDtoList")
    default List<SponsorDto> toSponsorDtoList(List<CarSponsors> carSponsors) {
        if (carSponsors == null) {
            return Collections.emptyList();
        }
        return carSponsors.stream()
                .filter(carSponsor -> carSponsor.getSponsor() != null)
                .map(carSponsor -> {
                    Sponsor sponsor = carSponsor.getSponsor();
                    return new SponsorDto(
                            sponsor.getId(),
                            sponsor.getName(),
                            sponsor.getIndustry(),
                            sponsor.getFoundingYear(),
                            sponsor.getImage()
                    );
                })
                .collect(Collectors.toList());
    }
}