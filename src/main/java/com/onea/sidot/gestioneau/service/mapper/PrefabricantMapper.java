package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.PrefabricantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Prefabricant} and its DTO {@link PrefabricantDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrefabricantMapper extends EntityMapper<PrefabricantDTO, Prefabricant> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    PrefabricantDTO toDtoLibelle(Prefabricant prefabricant);
}
