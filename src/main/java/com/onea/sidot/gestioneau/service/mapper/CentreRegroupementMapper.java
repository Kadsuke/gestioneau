package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.CentreRegroupementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CentreRegroupement} and its DTO {@link CentreRegroupementDTO}.
 */
@Mapper(componentModel = "spring", uses = { DirectionRegionaleMapper.class })
public interface CentreRegroupementMapper extends EntityMapper<CentreRegroupementDTO, CentreRegroupement> {
    @Mapping(target = "directionregionale", source = "directionregionale", qualifiedByName = "libelle")
    CentreRegroupementDTO toDto(CentreRegroupement s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    CentreRegroupementDTO toDtoLibelle(CentreRegroupement centreRegroupement);
}
