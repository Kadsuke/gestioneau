package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Annee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Annee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnneeRepository extends JpaRepository<Annee, Long> {}
