package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Centre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Centre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentreRepository extends JpaRepository<Centre, Long> {}
