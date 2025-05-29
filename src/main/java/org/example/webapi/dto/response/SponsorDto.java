package org.example.webapi.dto.response;

public record SponsorDto(int id,
                         String name,
                         String industry,
                         int foundingYear,
                         String image) {
}
