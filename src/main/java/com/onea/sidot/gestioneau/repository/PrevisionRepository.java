package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Prevision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prevision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrevisionRepository extends JpaRepository<Prevision, Long> {}
