package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.MaconDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Macon} and its DTO {@link MaconDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MaconMapper extends EntityMapper<MaconDTO, Macon> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    MaconDTO toDtoLibelle(Macon macon);
}
