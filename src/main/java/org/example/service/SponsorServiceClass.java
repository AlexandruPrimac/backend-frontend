package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.domain.Sponsor;
import org.example.repository.SponsorJpaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SponsorServiceClass implements org.example.service.Interfaces.SponsorService {
    private final static Logger logger = LoggerFactory.getLogger(SponsorServiceClass.class);

    private final SponsorJpaRepo sponsorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SponsorServiceClass(SponsorJpaRepo sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    @Override
    public List<Sponsor> getAllSponsors() {
        List<Sponsor> sponsors = sponsorRepository.findAll();
        logger.info("Found {} sponsors", sponsors.size());
        return sponsors;
    }

    @Override
    public List<Sponsor> filterSponsors(String name) {
        List<Sponsor> sponsors = sponsorRepository.findAll().stream()
                .filter(sponsor -> sponsor.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        logger.info("Found {} sponsors with name: {}", sponsors.size(), name);
        return sponsors;
    }

    @Override
    public List<Sponsor> filterSponsorsDynamically(String name) {
        return sponsorRepository.filterSponsorsByName(name);
    }

    @Override
    public void deleteSponsor(int id) {
        if (sponsorRepository.existsById(id)) {
            // Remove associations in the join table
            Sponsor sponsor = sponsorRepository.findById(id).orElse(null);

            // Remove car associations using JPQL
            entityManager.createQuery("DELETE FROM CarSponsors cs WHERE cs.sponsor = :sponsor")
                    .setParameter("sponsor", sponsor)
                    .executeUpdate();

            entityManager.flush();

            // Delete the sponsor
            sponsorRepository.deleteById(id);
        }
    }

}
