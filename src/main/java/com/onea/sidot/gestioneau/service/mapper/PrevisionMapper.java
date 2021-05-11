package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.PrevisionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Prevision} and its DTO {@link PrevisionDTO}.
 */
@Mapper(componentModel = "spring", uses = { CentreMapper.class, AnneeMapper.class })
public interface PrevisionMapper extends EntityMapper<PrevisionDTO, Prevision> {
    @Mapping(target = "centre", source = "centre", qualifiedByName = "libelle")
    @Mapping(target = "refannee", source = "refannee", qualifiedByName = "libelle")
    PrevisionDTO toDto(Prevision s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrevisionDTO toDtoId(Prevision prevision);
}
