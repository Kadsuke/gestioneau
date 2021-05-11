package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.SourceApprovEpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SourceApprovEp} and its DTO {@link SourceApprovEpDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SourceApprovEpMapper extends EntityMapper<SourceApprovEpDTO, SourceApprovEp> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    SourceApprovEpDTO toDtoLibelle(SourceApprovEp sourceApprovEp);
}
