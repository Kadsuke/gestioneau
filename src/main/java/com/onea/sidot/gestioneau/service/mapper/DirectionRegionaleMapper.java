package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.DirectionRegionaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DirectionRegionale} and its DTO {@link DirectionRegionaleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DirectionRegionaleMapper extends EntityMapper<DirectionRegionaleDTO, DirectionRegionale> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    DirectionRegionaleDTO toDtoLibelle(DirectionRegionale directionRegionale);
}
