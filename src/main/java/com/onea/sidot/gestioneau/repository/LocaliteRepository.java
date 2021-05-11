package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Localite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Localite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocaliteRepository extends JpaRepository<Localite, Long> {}
