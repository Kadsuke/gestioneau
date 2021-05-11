package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.ParcelleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parcelle} and its DTO {@link ParcelleDTO}.
 */
@Mapper(componentModel = "spring", uses = { LotMapper.class })
public interface ParcelleMapper extends EntityMapper<ParcelleDTO, Parcelle> {
    @Mapping(target = "lot", source = "lot", qualifiedByName = "libelle")
    ParcelleDTO toDto(Parcelle s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ParcelleDTO toDtoLibelle(Parcelle parcelle);
}
