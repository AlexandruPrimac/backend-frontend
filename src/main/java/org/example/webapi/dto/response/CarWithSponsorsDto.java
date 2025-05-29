package org.example.webapi.dto.response;

import java.util.List;

public record CarWithSponsorsDto(int id,
                              String brand,
                              String model,
                              String image,
                              List<SponsorDto> sponsors) {
}
