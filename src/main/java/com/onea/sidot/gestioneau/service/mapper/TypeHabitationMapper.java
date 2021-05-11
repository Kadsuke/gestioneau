package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.TypeHabitationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeHabitation} and its DTO {@link TypeHabitationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeHabitationMapper extends EntityMapper<TypeHabitationDTO, TypeHabitation> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    TypeHabitationDTO toDtoLibelle(TypeHabitation typeHabitation);
}
