package com.onea.sidot.gestioneau.repository;

import com.onea.sidot.gestioneau.domain.SourceApprovEp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SourceApprovEp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceApprovEpRepository extends JpaRepository<SourceApprovEp, Long> {}
