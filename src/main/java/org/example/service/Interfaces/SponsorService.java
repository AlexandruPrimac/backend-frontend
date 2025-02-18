package org.example.service.Interfaces;


import org.example.domain.Sponsor;

import java.util.List;

public interface SponsorService {

    List<Sponsor> getAllSponsors();

    List<Sponsor> filterSponsorsDynamically(String name);

    void deleteSponsor(int id);
}
