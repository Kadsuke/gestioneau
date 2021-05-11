package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.SecteurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Secteur} and its DTO {@link SecteurDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocaliteMapper.class })
public interface SecteurMapper extends EntityMapper<SecteurDTO, Secteur> {
    @Mapping(target = "localite", source = "localite", qualifiedByName = "libelle")
    SecteurDTO toDto(Secteur s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    SecteurDTO toDtoLibelle(Secteur secteur);
}
