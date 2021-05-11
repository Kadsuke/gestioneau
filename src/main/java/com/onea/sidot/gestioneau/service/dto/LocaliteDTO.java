package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.Localite} entity.
 */
public class LocaliteDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private CommuneDTO commune;

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

    public CommuneDTO getCommune() {
        return commune;
    }

    public void setCommune(CommuneDTO commune) {
        this.commune = commune;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocaliteDTO)) {
            return false;
        }

        LocaliteDTO localiteDTO = (LocaliteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocaliteDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", commune=" + getCommune() +
            "}";
    }
}
