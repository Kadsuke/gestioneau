package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.SectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Section} and its DTO {@link SectionDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecteurMapper.class })
public interface SectionMapper extends EntityMapper<SectionDTO, Section> {
    @Mapping(target = "secteur", source = "secteur", qualifiedByName = "libelle")
    SectionDTO toDto(Section s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    SectionDTO toDtoLibelle(Section section);
}
