package org.example.webapi.dto.response;

import org.example.domain.Sponsor;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SponsorMapper {
    SponsorDto toSponsorDto(Sponsor sponsor);
}
