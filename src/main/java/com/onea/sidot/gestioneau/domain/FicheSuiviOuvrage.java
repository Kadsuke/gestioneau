package com.onea.sidot.gestioneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A FicheSuiviOuvrage.
 */
@Entity
@Table(name = "fiche_suivi_ouvrage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fichesuiviouvrage")
public class FicheSuiviOuvrage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "prj_appuis", nullable = false)
    private String prjAppuis;

    @NotNull
    @Column(name = "nom_benef", nullable = false)
    private String nomBenef;

    @NotNull
    @Column(name = "prenom_benef", nullable = false)
    private String prenomBenef;

    @NotNull
    @Column(name = "profession_benef", nullable = false)
    private String professionBenef;

    @NotNull
    @Column(name = "nb_usagers", nullable = false)
    private Long nbUsagers;

    @NotNull
    @Column(name = "contacts", nullable = false)
    private String contacts;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @NotNull
    @Column(name = "date_remise_devis", nullable = false)
    private Instant dateRemiseDevis;

    @NotNull
    @Column(name = "date_debut_travaux", nullable = false)
    private Instant dateDebutTravaux;

    @NotNull
    @Column(name = "date_fin_travaux", nullable = false)
    private Instant dateFinTravaux;

    @Column(name = "rue")
    private String rue;

    @Column(name = "porte")
    private String porte;

    @NotNull
    @Column(name = "cout_menage", nullable = false)
    private String coutMenage;

    @NotNull
    @Column(name = "subv_onea", nullable = false)
    private Integer subvOnea;

    @NotNull
    @Column(name = "subv_projet", nullable = false)
    private Integer subvProjet;

    @NotNull
    @Column(name = "autre_subv", nullable = false)
    private Integer autreSubv;

    @NotNull
    @Column(name = "toles", nullable = false)
    private Integer toles;

    @NotNull
    @Column(name = "animateur", nullable = false)
    private String animateur;

    @NotNull
    @Column(name = "superviseur", nullable = false)
    private String superviseur;

    @NotNull
    @Column(name = "controleur", nullable = false)
    private String controleur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lot" }, allowSetters = true)
    private Parcelle parcelle;

    @ManyToOne
    @JsonIgnoreProperties(value = { "centre", "refannee" }, allowSetters = true)
    private Prevision prevision;

    @ManyToOne
    private NatureOuvrage natureouvrage;

    @ManyToOne
    private TypeHabitation typehabitation;

    @ManyToOne
    private SourceApprovEp sourceapprovep;

    @ManyToOne
    private ModeEvacuationEauUsee modeevacuationeauusee;

    @ManyToOne
    private ModeEvacExcreta modeevacexcreta;

    @ManyToOne
    private Macon macon;

    @ManyToOne
    private Prefabricant prefabricant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FicheSuiviOuvrage id(Long id) {
        this.id = id;
        return this;
    }

    public String getPrjAppuis() {
        return this.prjAppuis;
    }

    public FicheSuiviOuvrage prjAppuis(String prjAppuis) {
        this.prjAppuis = prjAppuis;
        return this;
    }

    public void setPrjAppuis(String prjAppuis) {
        this.prjAppuis = prjAppuis;
    }

    public String getNomBenef() {
        return this.nomBenef;
    }

    public FicheSuiviOuvrage nomBenef(String nomBenef) {
        this.nomBenef = nomBenef;
        return this;
    }

    public void setNomBenef(String nomBenef) {
        this.nomBenef = nomBenef;
    }

    public String getPrenomBenef() {
        return this.prenomBenef;
    }

    public FicheSuiviOuvrage prenomBenef(String prenomBenef) {
        this.prenomBenef = prenomBenef;
        return this;
    }

    public void setPrenomBenef(String prenomBenef) {
        this.prenomBenef = prenomBenef;
    }

    public String getProfessionBenef() {
        return this.professionBenef;
    }

    public FicheSuiviOuvrage professionBenef(String professionBenef) {
        this.professionBenef = professionBenef;
        return this;
    }

    public void setProfessionBenef(String professionBenef) {
        this.professionBenef = professionBenef;
    }

    public Long getNbUsagers() {
        return this.nbUsagers;
    }

    public FicheSuiviOuvrage nbUsagers(Long nbUsagers) {
        this.nbUsagers = nbUsagers;
        return this;
    }

    public void setNbUsagers(Long nbUsagers) {
        this.nbUsagers = nbUsagers;
    }

    public String getContacts() {
        return this.contacts;
    }

    public FicheSuiviOuvrage contacts(String contacts) {
        this.contacts = contacts;
        return this;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public FicheSuiviOuvrage longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public FicheSuiviOuvrage latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Instant getDateRemiseDevis() {
        return this.dateRemiseDevis;
    }

    public FicheSuiviOuvrage dateRemiseDevis(Instant dateRemiseDevis) {
        this.dateRemiseDevis = dateRemiseDevis;
        return this;
    }

    public void setDateRemiseDevis(Instant dateRemiseDevis) {
        this.dateRemiseDevis = dateRemiseDevis;
    }

    public Instant getDateDebutTravaux() {
        return this.dateDebutTravaux;
    }

    public FicheSuiviOuvrage dateDebutTravaux(Instant dateDebutTravaux) {
        this.dateDebutTravaux = dateDebutTravaux;
        return this;
    }

    public void setDateDebutTravaux(Instant dateDebutTravaux) {
        this.dateDebutTravaux = dateDebutTravaux;
    }

    public Instant getDateFinTravaux() {
        return this.dateFinTravaux;
    }

    public FicheSuiviOuvrage dateFinTravaux(Instant dateFinTravaux) {
        this.dateFinTravaux = dateFinTravaux;
        return this;
    }

    public void setDateFinTravaux(Instant dateFinTravaux) {
        this.dateFinTravaux = dateFinTravaux;
    }

    public String getRue() {
        return this.rue;
    }

    public FicheSuiviOuvrage rue(String rue) {
        this.rue = rue;
        return this;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getPorte() {
        return this.porte;
    }

    public FicheSuiviOuvrage porte(String porte) {
        this.porte = porte;
        return this;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getCoutMenage() {
        return this.coutMenage;
    }

    public FicheSuiviOuvrage coutMenage(String coutMenage) {
        this.coutMenage = coutMenage;
        return this;
    }

    public void setCoutMenage(String coutMenage) {
        this.coutMenage = coutMenage;
    }

    public Integer getSubvOnea() {
        return this.subvOnea;
    }

    public FicheSuiviOuvrage subvOnea(Integer subvOnea) {
        this.subvOnea = subvOnea;
        return this;
    }

    public void setSubvOnea(Integer subvOnea) {
        this.subvOnea = subvOnea;
    }

    public Integer getSubvProjet() {
        return this.subvProjet;
    }

    public FicheSuiviOuvrage subvProjet(Integer subvProjet) {
        this.subvProjet = subvProjet;
        return this;
    }

    public void setSubvProjet(Integer subvProjet) {
        this.subvProjet = subvProjet;
    }

    public Integer getAutreSubv() {
        return this.autreSubv;
    }

    public FicheSuiviOuvrage autreSubv(Integer autreSubv) {
        this.autreSubv = autreSubv;
        return this;
    }

    public void setAutreSubv(Integer autreSubv) {
        this.autreSubv = autreSubv;
    }

    public Integer getToles() {
        return this.toles;
    }

    public FicheSuiviOuvrage toles(Integer toles) {
        this.toles = toles;
        return this;
    }

    public void setToles(Integer toles) {
        this.toles = toles;
    }

    public String getAnimateur() {
        return this.animateur;
    }

    public FicheSuiviOuvrage animateur(String animateur) {
        this.animateur = animateur;
        return this;
    }

    public void setAnimateur(String animateur) {
        this.animateur = animateur;
    }

    public String getSuperviseur() {
        return this.superviseur;
    }

    public FicheSuiviOuvrage superviseur(String superviseur) {
        this.superviseur = superviseur;
        return this;
    }

    public void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }

    public String getControleur() {
        return this.controleur;
    }

    public FicheSuiviOuvrage controleur(String controleur) {
        this.controleur = controleur;
        return this;
    }

    public void setControleur(String controleur) {
        this.controleur = controleur;
    }

    public Parcelle getParcelle() {
        return this.parcelle;
    }

    public FicheSuiviOuvrage parcelle(Parcelle parcelle) {
        this.setParcelle(parcelle);
        return this;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
    }

    public Prevision getPrevision() {
        return this.prevision;
    }

    public FicheSuiviOuvrage prevision(Prevision prevision) {
        this.setPrevision(prevision);
        return this;
    }

    public void setPrevision(Prevision prevision) {
        this.prevision = prevision;
    }

    public NatureOuvrage getNatureouvrage() {
        return this.natureouvrage;
    }

    public FicheSuiviOuvrage natureouvrage(NatureOuvrage natureOuvrage) {
        this.setNatureouvrage(natureOuvrage);
        return this;
    }

    public void setNatureouvrage(NatureOuvrage natureOuvrage) {
        this.natureouvrage = natureOuvrage;
    }

    public TypeHabitation getTypehabitation() {
        return this.typehabitation;
    }

    public FicheSuiviOuvrage typehabitation(TypeHabitation typeHabitation) {
        this.setTypehabitation(typeHabitation);
        return this;
    }

    public void setTypehabitation(TypeHabitation typeHabitation) {
        this.typehabitation = typeHabitation;
    }

    public SourceApprovEp getSourceapprovep() {
        return this.sourceapprovep;
    }

    public FicheSuiviOuvrage sourceapprovep(SourceApprovEp sourceApprovEp) {
        this.setSourceapprovep(sourceApprovEp);
        return this;
    }

    public void setSourceapprovep(SourceApprovEp sourceApprovEp) {
        this.sourceapprovep = sourceApprovEp;
    }

    public ModeEvacuationEauUsee getModeevacuationeauusee() {
        return this.modeevacuationeauusee;
    }

    public FicheSuiviOuvrage modeevacuationeauusee(ModeEvacuationEauUsee modeEvacuationEauUsee) {
        this.setModeevacuationeauusee(modeEvacuationEauUsee);
        return this;
    }

    public void setModeevacuationeauusee(ModeEvacuationEauUsee modeEvacuationEauUsee) {
        this.modeevacuationeauusee = modeEvacuationEauUsee;
    }

    public ModeEvacExcreta getModeevacexcreta() {
        return this.modeevacexcreta;
    }

    public FicheSuiviOuvrage modeevacexcreta(ModeEvacExcreta modeEvacExcreta) {
        this.setModeevacexcreta(modeEvacExcreta);
        return this;
    }

    public void setModeevacexcreta(ModeEvacExcreta modeEvacExcreta) {
        this.modeevacexcreta = modeEvacExcreta;
    }

    public Macon getMacon() {
        return this.macon;
    }

    public FicheSuiviOuvrage macon(Macon macon) {
        this.setMacon(macon);
        return this;
    }

    public void setMacon(Macon macon) {
        this.macon = macon;
    }

    public Prefabricant getPrefabricant() {
        return this.prefabricant;
    }

    public FicheSuiviOuvrage prefabricant(Prefabricant prefabricant) {
        this.setPrefabricant(prefabricant);
        return this;
    }

    public void setPrefabricant(Prefabricant prefabricant) {
        this.prefabricant = prefabricant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FicheSuiviOuvrage)) {
            return false;
        }
        return id != null && id.equals(((FicheSuiviOuvrage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FicheSuiviOuvrage{" +
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
            "}";
    }
}
