package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.Lot} entity.
 */
public class LotDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private SectionDTO section;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public SectionDTO getSection() {
        return section;
    }

    public void setSection(SectionDTO section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LotDTO)) {
            return false;
        }

        LotDTO lotDTO = (LotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LotDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", section=" + getSection() +
            "}";
    }
}
