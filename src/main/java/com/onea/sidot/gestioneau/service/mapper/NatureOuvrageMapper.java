package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.NatureOuvrageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NatureOuvrage} and its DTO {@link NatureOuvrageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NatureOuvrageMapper extends EntityMapper<NatureOuvrageDTO, NatureOuvrage> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    NatureOuvrageDTO toDtoLibelle(NatureOuvrage natureOuvrage);
}
