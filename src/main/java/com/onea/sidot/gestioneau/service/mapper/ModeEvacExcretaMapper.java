package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.ModeEvacExcretaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModeEvacExcreta} and its DTO {@link ModeEvacExcretaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModeEvacExcretaMapper extends EntityMapper<ModeEvacExcretaDTO, ModeEvacExcreta> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ModeEvacExcretaDTO toDtoLibelle(ModeEvacExcreta modeEvacExcreta);
}
