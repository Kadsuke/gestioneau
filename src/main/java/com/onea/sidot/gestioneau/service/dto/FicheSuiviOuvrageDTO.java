package com.onea.sidot.gestioneau.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.onea.sidot.gestioneau.domain.FicheSuiviOuvrage} entity.
 */
public class FicheSuiviOuvrageDTO implements Serializable {

    private Long id;

    @NotNull
    private String prjAppuis;

    @NotNull
    private String nomBenef;

    @NotNull
    private String prenomBenef;

    @NotNull
    private String professionBenef;

    @NotNull
    private Long nbUsagers;

    @NotNull
    private String contacts;

    @NotNull
    private Float longitude;

    @NotNull
    private Float latitude;

    @NotNull
    private Instant dateRemiseDevis;

    @NotNull
    private Instant dateDebutTravaux;

    @NotNull
    private Instant dateFinTravaux;

    private String rue;

    private String porte;

    @NotNull
    private String coutMenage;

    @NotNull
    private Integer subvOnea;

    @NotNull
    private Integer subvProjet;

    @NotNull
    private Integer autreSubv;

    @NotNull
    private Integer toles;

    @NotNull
    private String animateur;

    @NotNull
    private String superviseur;

    @NotNull
    private String controleur;

    private ParcelleDTO parcelle;

    private PrevisionDTO prevision;

    private NatureOuvrageDTO natureouvrage;

    private TypeHabitationDTO typehabitation;

    private SourceApprovEpDTO sourceapprovep;

    private ModeEvacuationEauUseeDTO modeevacuationeauusee;

    private ModeEvacExcretaDTO modeevacexcreta;

    private MaconDTO macon;

    private PrefabricantDTO prefabricant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrjAppuis() {
        return prjAppuis;
    }

    public void setPrjAppuis(String prjAppuis) {
        this.prjAppuis = prjAppuis;
    }

    public String getNomBenef() {
        return nomBenef;
    }

    public void setNomBenef(String nomBenef) {
        this.nomBenef = nomBenef;
    }

    public String getPrenomBenef() {
        return prenomBenef;
    }

    public void setPrenomBenef(String prenomBenef) {
        this.prenomBenef = prenomBenef;
    }

    public String getProfessionBenef() {
        return professionBenef;
    }

    public void setProfessionBenef(String professionBenef) {
        this.professionBenef = professionBenef;
    }

    public Long getNbUsagers() {
        return nbUsagers;
    }

    public void setNbUsagers(Long nbUsagers) {
        this.nbUsagers = nbUsagers;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Instant getDateRemiseDevis() {
        return dateRemiseDevis;
    }

    public void setDateRemiseDevis(Instant dateRemiseDevis) {
        this.dateRemiseDevis = dateRemiseDevis;
    }

    public Instant getDateDebutTravaux() {
        return dateDebutTravaux;
    }

    public void setDateDebutTravaux(Instant dateDebutTravaux) {
        this.dateDebutTravaux = dateDebutTravaux;
    }

    public Instant getDateFinTravaux() {
        return dateFinTravaux;
    }

    public void setDateFinTravaux(Instant dateFinTravaux) {
        this.dateFinTravaux = dateFinTravaux;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getCoutMenage() {
        return coutMenage;
    }

    public void setCoutMenage(String coutMenage) {
        this.coutMenage = coutMenage;
    }

    public Integer getSubvOnea() {
        return subvOnea;
    }

    public void setSubvOnea(Integer subvOnea) {
        this.subvOnea = subvOnea;
    }

    public Integer getSubvProjet() {
        return subvProjet;
    }

    public void setSubvProjet(Integer subvProjet) {
        this.subvProjet = subvProjet;
    }

    public Integer getAutreSubv() {
        return autreSubv;
    }

    public void setAutreSubv(Integer autreSubv) {
        this.autreSubv = autreSubv;
    }

    public Integer getToles() {
        return toles;
    }

    public void setToles(Integer toles) {
        this.toles = toles;
    }

    public String getAnimateur() {
        return animateur;
    }

    public void setAnimateur(String animateur) {
        this.animateur = animateur;
    }

    public String getSuperviseur() {
        return superviseur;
    }

    public void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }

    public String getControleur() {
        return controleur;
    }

    public void setControleur(String controleur) {
        this.controleur = controleur;
    }

    public ParcelleDTO getParcelle() {
        return parcelle;
    }

    public void setParcelle(ParcelleDTO parcelle) {
        this.parcelle = parcelle;
    }

    public PrevisionDTO getPrevision() {
        return prevision;
    }

    public void setPrevision(PrevisionDTO prevision) {
        this.prevision = prevision;
    }

    public NatureOuvrageDTO getNatureouvrage() {
        return natureouvrage;
    }

    public void setNatureouvrage(NatureOuvrageDTO natureouvrage) {
        this.natureouvrage = natureouvrage;
    }

    public TypeHabitationDTO getTypehabitation() {
        return typehabitation;
    }

    public void setTypehabitation(TypeHabitationDTO typehabitation) {
        this.typehabitation = typehabitation;
    }

    public SourceApprovEpDTO getSourceapprovep() {
        return sourceapprovep;
    }

    public void setSourceapprovep(SourceApprovEpDTO sourceapprovep) {
        this.sourceapprovep = sourceapprovep;
    }

    public ModeEvacuationEauUseeDTO getModeevacuationeauusee() {
        return modeevacuationeauusee;
    }

    public void setModeevacuationeauusee(ModeEvacuationEauUseeDTO modeevacuationeauusee) {
        this.modeevacuationeauusee = modeevacuationeauusee;
    }

    public ModeEvacExcretaDTO getModeevacexcreta() {
        return modeevacexcreta;
    }

    public void setModeevacexcreta(ModeEvacExcretaDTO modeevacexcreta) {
        this.modeevacexcreta = modeevacexcreta;
    }

    public MaconDTO getMacon() {
        return macon;
    }

    public void setMacon(MaconDTO macon) {
        this.macon = macon;
    }

    public PrefabricantDTO getPrefabricant() {
        return prefabricant;
    }

    public void setPrefabricant(PrefabricantDTO prefabricant) {
        this.prefabricant = prefabricant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FicheSuiviOuvrageDTO)) {
            return false;
        }

        FicheSuiviOuvrageDTO ficheSuiviOuvrageDTO = (FicheSuiviOuvrageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ficheSuiviOuvrageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FicheSuiviOuvrageDTO{" +
            "id=" + getId() +
            ", prjAppuis='" + getPrjAppuis() + "'" +
            ", nomBenef='" + getNomBenef() + "'" +
            ", prenomBenef='" + getPrenomBenef() + "'" +
            ", professionBenef='" + getProfessionBenef() + "'" +
            ", nbUsagers=" + getNbUsagers() +
            ", contacts='" + getContacts() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", dateRemiseDevis='" + getDateRemiseDevis() + "'" +
            ", dateDebutTravaux='" + getDateDebutTravaux() + "'" +
            ", dateFinTravaux='" + getDateFinTravaux() + "'" +
            ", rue='" + getRue() + "'" +
            ", porte='" + getPorte() + "'" +
            ", coutMenage='" + getCoutMenage() + "'" +
            ", subvOnea=" + getSubvOnea() +
            ", subvProjet=" + getSubvProjet() +
            ", autreSubv=" + getAutreSubv() +
            ", toles=" + getToles() +
            ", animateur='" + getAnimateur() + "'" +
            ", superviseur='" + getSuperviseur() + "'" +
            ", controleur='" + getControleur() + "'" +
            ", parcelle=" + getParcelle() +
            ", prevision=" + getPrevision() +
            ", natureouvrage=" + getNatureouvrage() +
            ", typehabitation=" + getTypehabitation() +
            ", sourceapprovep=" + getSourceapprovep() +
            ", modeevacuationeauusee=" + getModeevacuationeauusee() +
            ", modeevacexcreta=" + getModeevacexcreta() +
            ", macon=" + getMacon() +
            ", prefabricant=" + getPrefabricant() +
            "}";
    }
}
