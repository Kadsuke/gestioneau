package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.AnneeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Annee} and its DTO {@link AnneeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnneeMapper extends EntityMapper<AnneeDTO, Annee> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    AnneeDTO toDtoLibelle(Annee annee);
}
