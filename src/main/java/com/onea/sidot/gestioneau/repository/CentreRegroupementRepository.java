package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.CentreRegroupement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CentreRegroupement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentreRegroupementRepository extends JpaRepository<CentreRegroupement, Long> {}
