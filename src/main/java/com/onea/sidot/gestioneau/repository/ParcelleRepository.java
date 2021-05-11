package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Parcelle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Parcelle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcelleRepository extends JpaRepository<Parcelle, Long> {}
