package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.TypeHabitation} entity.
 */
public class TypeHabitationDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeHabitationDTO)) {
            return false;
        }

        TypeHabitationDTO typeHabitationDTO = (TypeHabitationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeHabitationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeHabitationDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
