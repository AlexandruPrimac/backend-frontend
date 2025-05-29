package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.domain.Sponsor;
import org.example.exception.CustomApplicationException;
import org.example.repository.SponsorJpaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SponsorServiceClass implements org.example.service.Interfaces.SponsorService {
    /// Logger
    private final static Logger logger = LoggerFactory.getLogger(SponsorServiceClass.class);

    /// Repositories
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
    public List<Sponsor> filterSponsorsDynamically(String name) {
        return sponsorRepository.filterSponsorsByName(name);
    }

    @Override
    public void deleteSponsor(int id) {

        if (!sponsorRepository.existsById(id)) {
            throw new CustomApplicationException("Sponsor not found with ID: " + id);
        }

        Sponsor sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new CustomApplicationException("Sponsor not found with ID: " + id));


        entityManager.createQuery("DELETE FROM CarSponsors cs WHERE cs.sponsor = :sponsor")
                .setParameter("sponsor", sponsor)
                .executeUpdate();

        sponsorRepository.deleteById(id);
    }
}

