package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.Section} entity.
 */
public class SectionDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private SecteurDTO secteur;

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

    public SecteurDTO getSecteur() {
        return secteur;
    }

    public void setSecteur(SecteurDTO secteur) {
        this.secteur = secteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SectionDTO)) {
            return false;
        }

        SectionDTO sectionDTO = (SectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SectionDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", secteur=" + getSecteur() +
            "}";
    }
}
