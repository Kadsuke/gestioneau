package com.onea.sidot.gestioneau.service.mapper;

import com.onea.sidot.gestioneau.domain.*;
import com.onea.sidot.gestioneau.service.dto.FicheSuiviOuvrageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FicheSuiviOuvrage} and its DTO {@link FicheSuiviOuvrageDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ParcelleMapper.class,
        PrevisionMapper.class,
        NatureOuvrageMapper.class,
        TypeHabitationMapper.class,
        SourceApprovEpMapper.class,
        ModeEvacuationEauUseeMapper.class,
        ModeEvacExcretaMapper.class,
        MaconMapper.class,
        PrefabricantMapper.class,
    }
)
public interface FicheSuiviOuvrageMapper extends EntityMapper<FicheSuiviOuvrageDTO, FicheSuiviOuvrage> {
    @Mapping(target = "parcelle", source = "parcelle", qualifiedByName = "libelle")
    @Mapping(target = "prevision", source = "prevision", qualifiedByName = "id")
    @Mapping(target = "natureouvrage", source = "natureouvrage", qualifiedByName = "libelle")
    @Mapping(target = "typehabitation", source = "typehabitation", qualifiedByName = "libelle")
    @Mapping(target = "sourceapprovep", source = "sourceapprovep", qualifiedByName = "libelle")
    @Mapping(target = "modeevacuationeauusee", source = "modeevacuationeauusee", qualifiedByName = "libelle")
    @Mapping(target = "modeevacexcreta", source = "modeevacexcreta", qualifiedByName = "libelle")
    @Mapping(target = "macon", source = "macon", qualifiedByName = "libelle")
    @Mapping(target = "prefabricant", source = "prefabricant", qualifiedByName = "libelle")
    FicheSuiviOuvrageDTO toDto(FicheSuiviOuvrage s);
}
