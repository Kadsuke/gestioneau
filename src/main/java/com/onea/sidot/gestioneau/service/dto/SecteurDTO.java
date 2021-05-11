package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.Secteur} entity.
 */
public class SecteurDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private LocaliteDTO localite;

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

    public LocaliteDTO getLocalite() {
        return localite;
    }

    public void setLocalite(LocaliteDTO localite) {
        this.localite = localite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecteurDTO)) {
            return false;
        }

        SecteurDTO secteurDTO = (SecteurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, secteurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecteurDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", localite=" + getLocalite() +
            "}";
    }
}
