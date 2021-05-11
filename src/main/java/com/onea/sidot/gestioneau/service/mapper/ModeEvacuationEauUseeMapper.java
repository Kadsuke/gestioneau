package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.ModeEvacuationEauUseeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModeEvacuationEauUsee} and its DTO {@link ModeEvacuationEauUseeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModeEvacuationEauUseeMapper extends EntityMapper<ModeEvacuationEauUseeDTO, ModeEvacuationEauUsee> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ModeEvacuationEauUseeDTO toDtoLibelle(ModeEvacuationEauUsee modeEvacuationEauUsee);
}
