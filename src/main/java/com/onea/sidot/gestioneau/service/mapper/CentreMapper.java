package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.CentreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Centre} and its DTO {@link CentreDTO}.
 */
@Mapper(componentModel = "spring", uses = { CentreRegroupementMapper.class })
public interface CentreMapper extends EntityMapper<CentreDTO, Centre> {
    @Mapping(target = "centreregroupement", source = "centreregroupement", qualifiedByName = "libelle")
    CentreDTO toDto(Centre s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    CentreDTO toDtoLibelle(Centre centre);
}
