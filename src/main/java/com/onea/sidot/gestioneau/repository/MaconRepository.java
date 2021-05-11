package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.Macon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Macon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaconRepository extends JpaRepository<Macon, Long> {}
