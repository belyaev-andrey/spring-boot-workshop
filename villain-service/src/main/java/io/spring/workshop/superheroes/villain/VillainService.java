package io.spring.workshop.superheroes.villain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class VillainService {

    @Value("${level.multiplier}")
    double levelMultiplier;
    private final VillainRepository villainRepository;

    public VillainService(VillainRepository villainRepository) {
        this.villainRepository = villainRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Villain findRandom() {
        long countVillains = villainRepository.count();
        Random random = new Random();
        int randomVillain = random.nextInt((int) countVillains);
        return villainRepository.findAll().get(randomVillain);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Villain persistVillain(Villain villain) {
        villain.level = (int) Math.round(villain.level * levelMultiplier);
        return villainRepository.save(villain);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteVillain(Long id) {
        villainRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Villain> findAllVillains() {
        return villainRepository.findAll();

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Optional<Villain> findVillainById(Long id) {
        return villainRepository.findById(id);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Villain updateVillain(@Valid Villain villain) {
        Villain entity = villainRepository.findById(villain.getId())
                                          .orElseThrow(EntityNotFoundException::new);
        entity.setName(villain.getName());
        entity.setOtherName(villain.getOtherName());
        entity.setLevel(villain.getLevel());
        entity.setPicture(villain.getPicture());
        entity.setPowers(villain.getPowers());
        return villainRepository.save(entity);
    }
}
