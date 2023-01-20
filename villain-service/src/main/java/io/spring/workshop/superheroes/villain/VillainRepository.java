package io.spring.workshop.superheroes.villain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VillainRepository extends JpaRepository<Villain, Long> {
}