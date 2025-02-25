package org.example.webapi.dto.response;

import org.example.domain.Car;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CarMapper {
    CarDto toCarDto(Car car);
}
