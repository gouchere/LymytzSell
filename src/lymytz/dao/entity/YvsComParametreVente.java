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

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_parametre_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComParametreVente.findAll", query = "SELECT y FROM YvsComParametreVente y"),
    @NamedQuery(name = "YvsComParametreVente.findByAgence", query = "SELECT y FROM YvsComParametreVente y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComParametreVente.findNJMAxByAgence", query = "SELECT y.nbFicheMax FROM YvsComParametreVente y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComParametreVente.findById", query = "SELECT y FROM YvsComParametreVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComParametreVente.findByJourAnterieur", query = "SELECT y FROM YvsComParametreVente y WHERE y.jourAnterieur = :jourAnterieur"),
    @NamedQuery(name = "YvsComParametreVente.findByComptabilisationAuto", query = "SELECT y FROM YvsComParametreVente y WHERE y.comptabilisationAuto = :comptabilisationAuto"),
    @NamedQuery(name = "YvsComParametreVente.findByComptabilisationMode", query = "SELECT y FROM YvsComParametreVente y WHERE y.comptabilisationMode = :comptabilisationMode"),
    @NamedQuery(name = "YvsComParametreVente.findByDateUpdate", query = "SELECT y FROM YvsComParametreVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComParametreVente.findByDateSave", query = "SELECT y FROM YvsComParametreVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComParametreVente.findByPaieWithoutValide", query = "SELECT y FROM YvsComParametreVente y WHERE y.paieWithoutValide = :paieWithoutValide"),
    @NamedQuery(name = "YvsComParametreVente.findByNbFicheMax", query = "SELECT y FROM YvsComParametreVente y WHERE y.nbFicheMax = :nbFicheMax"),
    @NamedQuery(name = "YvsComParametreVente.findByGenererFactureAuto", query = "SELECT y FROM YvsComParametreVente y WHERE y.genererFactureAuto = :genererFactureAuto"),
    @NamedQuery(name = "YvsComParametreVente.findByModelFactureVente", query = "SELECT y FROM YvsComParametreVente y WHERE y.modelFactureVente = :modelFactureVente"),
    @NamedQuery(name = "YvsComParametreVente.findBySellLowerPr", query = "SELECT y FROM YvsComParametreVente y WHERE y.sellLowerPr = :sellLowerPr")})
public class YvsComParametreVente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "jour_anterieur")
    private Integer jourAnterieur;
    @Column(name = "comptabilisation_auto")
    private Boolean comptabilisationAuto;
    @Column(name = "comptabilisation_mode")
    private String comptabilisationMode;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "paie_without_valide")
    private Boolean paieWithoutValide;
    @Column(name = "nb_fiche_max")
    private Integer nbFicheMax;
    @Column(name = "generer_facture_auto")
    private Boolean genererFactureAuto;
    @Column(name = "model_facture_vente")
    private String modelFactureVente;
    @Column(name = "sell_lower_pr")
    private Boolean sellLowerPr;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComParametreVente() {
    }

    public YvsComParametreVente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJourAnterieur() {
        return jourAnterieur;
    }

    public void setJourAnterieur(Integer jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public Boolean getComptabilisationAuto() {
        return comptabilisationAuto;
    }

    public void setComptabilisationAuto(Boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
    }

    public String getComptabilisationMode() {
        return comptabilisationMode;
    }

    public void setComptabilisationMode(String comptabilisationMode) {
        this.comptabilisationMode = comptabilisationMode;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getPaieWithoutValide() {
        return paieWithoutValide;
    }

    public void setPaieWithoutValide(Boolean paieWithoutValide) {
        this.paieWithoutValide = paieWithoutValide;
    }

    public Integer getNbFicheMax() {
        return nbFicheMax;
    }

    public void setNbFicheMax(Integer nbFicheMax) {
        this.nbFicheMax = nbFicheMax;
    }

    public Boolean getGenererFactureAuto() {
        return genererFactureAuto;
    }

    public void setGenererFactureAuto(Boolean genererFactureAuto) {
        this.genererFactureAuto = genererFactureAuto;
    }

    public String getModelFactureVente() {
        return modelFactureVente;
    }

    public void setModelFactureVente(String modelFactureVente) {
        this.modelFactureVente = modelFactureVente;
    }

    public Boolean getSellLowerPr() {
        return sellLowerPr;
    }

    public void setSellLowerPr(Boolean sellLowerPr) {
        this.sellLowerPr = sellLowerPr;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsComParametreVente)) {
            return false;
        }
        YvsComParametreVente other = (YvsComParametreVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComParametreVente[ id=" + id + " ]";
    }
    
}
