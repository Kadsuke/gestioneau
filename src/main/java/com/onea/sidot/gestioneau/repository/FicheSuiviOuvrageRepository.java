package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.FicheSuiviOuvrage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FicheSuiviOuvrage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FicheSuiviOuvrageRepository extends JpaRepository<FicheSuiviOuvrage, Long> {}
