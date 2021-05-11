package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.LotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lot} and its DTO {@link LotDTO}.
 */
@Mapper(componentModel = "spring", uses = { SectionMapper.class })
public interface LotMapper extends EntityMapper<LotDTO, Lot> {
    @Mapping(target = "section", source = "section", qualifiedByName = "libelle")
    LotDTO toDto(Lot s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    LotDTO toDtoLibelle(Lot lot);
}
