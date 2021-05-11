package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Secteur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Secteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecteurRepository extends JpaRepository<Secteur, Long> {}
