package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.TypeCommuneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeCommune} and its DTO {@link TypeCommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeCommuneMapper extends EntityMapper<TypeCommuneDTO, TypeCommune> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    TypeCommuneDTO toDtoLibelle(TypeCommune typeCommune);
}
