package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Comarca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComarcaRepository extends JpaRepository<Comarca, Integer> {
  Optional<Comarca> findByName(String name);
}
