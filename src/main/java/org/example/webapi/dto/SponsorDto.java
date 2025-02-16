package org.example.webapi.dto;

import org.example.domain.Race;
import org.example.domain.Sponsor;

public record SponsorDto(int id, String name, String industry, int foundingYear, String image) {
    public static SponsorDto fromSponsor(final Sponsor sponsor){
        return new SponsorDto(sponsor.getId(), sponsor.getName(), sponsor.getIndustry(), sponsor.getFoundingYear(), sponsor.getImage());
    }
}
