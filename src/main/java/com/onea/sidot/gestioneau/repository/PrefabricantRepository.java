package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Prefabricant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prefabricant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrefabricantRepository extends JpaRepository<Prefabricant, Long> {}
