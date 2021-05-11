package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.CommuneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commune} and its DTO {@link CommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProvinceMapper.class, TypeCommuneMapper.class })
public interface CommuneMapper extends EntityMapper<CommuneDTO, Commune> {
    @Mapping(target = "province", source = "province", qualifiedByName = "libelle")
    @Mapping(target = "typecommune", source = "typecommune", qualifiedByName = "libelle")
    CommuneDTO toDto(Commune s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    CommuneDTO toDtoLibelle(Commune commune);
}
