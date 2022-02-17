/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_client")
@NamedQueries({
    @NamedQuery(name = "YvsComClient.findAll", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findById", query = "SELECT y FROM YvsComClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComClient.findByDefaut", query = "SELECT y FROM YvsComClient y WHERE y.defaut = :defaut"),
    @NamedQuery(name = "YvsComClient.findByCodeClient", query = "SELECT y FROM YvsComClient y WHERE y.codeClient = :codeClient"),
    @NamedQuery(name = "YvsComClient.findByActif", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.actif = true ORDER BY y.nom, y.prenom"),
    @NamedQuery(name = "YvsComClient.findByNom", query = "SELECT y FROM YvsComClient y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsComClient.findByPrenom", query = "SELECT y FROM YvsComClient y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsComClient.findBySuiviComptable", query = "SELECT y FROM YvsComClient y WHERE y.suiviComptable = :suiviComptable"),
    @NamedQuery(name = "YvsComClient.findBySeuilSolde", query = "SELECT y FROM YvsComClient y WHERE y.seuilSolde = :seuilSolde"),
    @NamedQuery(name = "YvsComClient.findByDateCreation", query = "SELECT y FROM YvsComClient y WHERE y.dateCreation = :dateCreation"),
    @NamedQuery(name = "YvsComClient.findByDateUpdate", query = "SELECT y FROM YvsComClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComClient.findByConfirmer", query = "SELECT y FROM YvsComClient y WHERE y.confirmer = :confirmer")})
public class YvsComClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "code_client")
    private String codeClient;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "suivi_comptable")
    private Boolean suiviComptable;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "seuil_solde")
    private Double seuilSolde;
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "confirmer")
    private Boolean confirmer;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModelReglement model;
    @JoinColumn(name = "ligne", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointLivraison ligne;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTiers tiers;
    @JoinColumn(name = "plan_ristourne", referencedColumnName = "id")
    @ManyToOne
    private YvsComPlanRistourne planRistourne;
    @JoinColumn(name = "create_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers createBy;
    @Transient
    private String nom_prenom;

    public YvsComClient() {
    }

    public YvsComClient(Long id) {
        this.id = id;
    }
    public YvsComClient(YvsComClient c) {
        this.id = c.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Boolean getSuiviComptable() {
        return suiviComptable;
    }

    public void setSuiviComptable(Boolean suiviComptable) {
        this.suiviComptable = suiviComptable;
    }

    public Double getSeuilSolde() {
        return seuilSolde;
    }

    public void setSeuilSolde(Double seuilSolde) {
        this.seuilSolde = seuilSolde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Boolean getConfirmer() {
        return confirmer;
    }

    public void setConfirmer(Boolean confirmer) {
        this.confirmer = confirmer;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public YvsBaseModelReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModelReglement model) {
        this.model = model;
    }

    public YvsBasePointLivraison getLigne() {
        return ligne;
    }

    public void setLigne(YvsBasePointLivraison ligne) {
        this.ligne = ligne;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public YvsComPlanRistourne getPlanRistourne() {
        return planRistourne;
    }

    public void setPlanRistourne(YvsComPlanRistourne planRistourne) {
        this.planRistourne = planRistourne;
    }

    public YvsUsers getCreateBy() {
        return createBy;
    }

    public void setCreateBy(YvsUsers createBy) {
        this.createBy = createBy;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nom_prenom == null || nom_prenom.trim().equals("")) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
//        if (getTiers() != null ? getTiers().getId() > 0 : false) {
//            if (getTiers().getStSociete()) {
//                if (!(getTiers().getResponsable() == null || getTiers().getResponsable().trim().equals(""))) {
//                    if (nom_prenom == null || nom_prenom.trim().equals("")) {
//                        nom_prenom = getTiers().getResponsable();
//                    } else {
//                        nom_prenom += " / " + getTiers().getResponsable();
//                    }
//                }
//            }
//        }
        return nom_prenom;
    }
    
    public String getTextClient(){
        return getNom_prenom().concat("->".concat(getCodeClient()));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComClient)) {
            return false;
        }
        YvsComClient other = (YvsComClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComClient_1[ id=" + id + " ]";
    }

}
