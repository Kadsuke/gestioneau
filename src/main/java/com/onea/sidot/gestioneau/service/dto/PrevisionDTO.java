package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.Prevision} entity.
 */
public class PrevisionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer nbLatrine;

    @NotNull
    private Integer nbPuisard;

    @NotNull
    private Integer nbPublic;

    @NotNull
    private Integer nbScolaire;

    private CentreDTO centre;

    private AnneeDTO refannee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbLatrine() {
        return nbLatrine;
    }

    public void setNbLatrine(Integer nbLatrine) {
        this.nbLatrine = nbLatrine;
    }

    public Integer getNbPuisard() {
        return nbPuisard;
    }

    public void setNbPuisard(Integer nbPuisard) {
        this.nbPuisard = nbPuisard;
    }

    public Integer getNbPublic() {
        return nbPublic;
    }

    public void setNbPublic(Integer nbPublic) {
        this.nbPublic = nbPublic;
    }

    public Integer getNbScolaire() {
        return nbScolaire;
    }

    public void setNbScolaire(Integer nbScolaire) {
        this.nbScolaire = nbScolaire;
    }

    public CentreDTO getCentre() {
        return centre;
    }

    public void setCentre(CentreDTO centre) {
        this.centre = centre;
    }

    public AnneeDTO getRefannee() {
        return refannee;
    }

    public void setRefannee(AnneeDTO refannee) {
        this.refannee = refannee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrevisionDTO)) {
            return false;
        }

        PrevisionDTO previsionDTO = (PrevisionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, previsionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrevisionDTO{" +
            "id=" + getId() +
            ", nbLatrine=" + getNbLatrine() +
            ", nbPuisard=" + getNbPuisard() +
            ", nbPublic=" + getNbPublic() +
            ", nbScolaire=" + getNbScolaire() +
            ", centre=" + getCentre() +
            ", refannee=" + getRefannee() +
            "}";
    }
}
