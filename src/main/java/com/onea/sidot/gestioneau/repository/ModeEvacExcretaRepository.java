package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.ModeEvacExcreta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ModeEvacExcreta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModeEvacExcretaRepository extends JpaRepository<ModeEvacExcreta, Long> {}
