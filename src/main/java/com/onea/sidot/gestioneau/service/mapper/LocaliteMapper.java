package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.LocaliteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Localite} and its DTO {@link LocaliteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommuneMapper.class })
public interface LocaliteMapper extends EntityMapper<LocaliteDTO, Localite> {
    @Mapping(target = "commune", source = "commune", qualifiedByName = "libelle")
    LocaliteDTO toDto(Localite s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    LocaliteDTO toDtoLibelle(Localite localite);
}
