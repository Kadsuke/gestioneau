package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.TypeCommune;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeCommune entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeCommuneRepository extends JpaRepository<TypeCommune, Long> {}
