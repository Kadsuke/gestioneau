package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.Parcelle} entity.
 */
public class ParcelleDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private LotDTO lot;

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

    public LotDTO getLot() {
        return lot;
    }

    public void setLot(LotDTO lot) {
        this.lot = lot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParcelleDTO)) {
            return false;
        }

        ParcelleDTO parcelleDTO = (ParcelleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parcelleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParcelleDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", lot=" + getLot() +
            "}";
    }
}
