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
@Table(name = "yvs_com_parametre")
@NamedQueries({
    @NamedQuery(name = "YvsComParametre.findAll", query = "SELECT y FROM YvsComParametre y"),
    @NamedQuery(name = "YvsComParametre.findById", query = "SELECT y FROM YvsComParametre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComParametre.findByReglementAuto", query = "SELECT y FROM YvsComParametre y WHERE y.reglementAuto = :reglementAuto"),
    @NamedQuery(name = "YvsComParametre.findByDocumentMouvAchat", query = "SELECT y FROM YvsComParametre y WHERE y.documentMouvAchat = :documentMouvAchat"),
    @NamedQuery(name = "YvsComParametre.findByDocumentMouvVente", query = "SELECT y FROM YvsComParametre y WHERE y.documentMouvVente = :documentMouvVente"),
    @NamedQuery(name = "YvsComParametre.findByModeInventaire", query = "SELECT y FROM YvsComParametre y WHERE y.modeInventaire = :modeInventaire"),
    @NamedQuery(name = "YvsComParametre.findBySeuilFsseur", query = "SELECT y FROM YvsComParametre y WHERE y.seuilFsseur = :seuilFsseur"),
    @NamedQuery(name = "YvsComParametre.findBySeuilClient", query = "SELECT y FROM YvsComParametre y WHERE y.seuilClient = :seuilClient"),
    @NamedQuery(name = "YvsComParametre.findByDureeInactiv", query = "SELECT y FROM YvsComParametre y WHERE y.dureeInactiv = :dureeInactiv"),
    @NamedQuery(name = "YvsComParametre.findByDateUpdate", query = "SELECT y FROM YvsComParametre y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComParametre.findByDateSave", query = "SELECT y FROM YvsComParametre y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComParametre.findByConverter", query = "SELECT y FROM YvsComParametre y WHERE y.converter = :converter"),
    @NamedQuery(name = "YvsComParametre.findByJourUsine", query = "SELECT y FROM YvsComParametre y WHERE y.jourUsine = :jourUsine"),
    @NamedQuery(name = "YvsComParametre.findByConverterCs", query = "SELECT y FROM YvsComParametre y WHERE y.converterCs = :converterCs"),
    @NamedQuery(name = "YvsComParametre.findByJourDebutMois", query = "SELECT y FROM YvsComParametre y WHERE y.jourDebutMois = :jourDebutMois"),
    @NamedQuery(name = "YvsComParametre.findByFactureOutsideSeuil", query = "SELECT y FROM YvsComParametre y WHERE y.factureOutsideSeuil = :factureOutsideSeuil"),
    @NamedQuery(name = "YvsComParametre.findByDocumentGenererFromEcart", query = "SELECT y FROM YvsComParametre y WHERE y.documentGenererFromEcart = :documentGenererFromEcart")})
public class YvsComParametre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reglement_auto")
    private Boolean reglementAuto;
    @Column(name = "document_mouv_achat")
    private String documentMouvAchat;
    @Column(name = "document_mouv_vente")
    private String documentMouvVente;
    @Column(name = "mode_inventaire")
    private String modeInventaire;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "seuil_fsseur")
    private Double seuilFsseur;
    @Column(name = "seuil_client")
    private Double seuilClient;
    @Column(name = "duree_inactiv")
    private Integer dureeInactiv;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "converter")
    private Integer converter;
    @Column(name = "jour_usine")
    private Integer jourUsine;
    @Column(name = "converter_cs")
    private Integer converterCs;
    @Column(name = "jour_debut_mois")
    private Integer jourDebutMois;
    @Column(name = "facture_outside_seuil")
    private Boolean factureOutsideSeuil;
    @Column(name = "document_generer_from_ecart")
    private String documentGenererFromEcart;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComParametre() {
    }

    public YvsComParametre(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getReglementAuto() {
        return reglementAuto;
    }

    public void setReglementAuto(Boolean reglementAuto) {
        this.reglementAuto = reglementAuto;
    }

    public String getDocumentMouvAchat() {
        return documentMouvAchat;
    }

    public void setDocumentMouvAchat(String documentMouvAchat) {
        this.documentMouvAchat = documentMouvAchat;
    }

    public String getDocumentMouvVente() {
        return documentMouvVente;
    }

    public void setDocumentMouvVente(String documentMouvVente) {
        this.documentMouvVente = documentMouvVente;
    }

    public String getModeInventaire() {
        return modeInventaire;
    }

    public void setModeInventaire(String modeInventaire) {
        this.modeInventaire = modeInventaire;
    }

    public Double getSeuilFsseur() {
        return seuilFsseur;
    }

    public void setSeuilFsseur(Double seuilFsseur) {
        this.seuilFsseur = seuilFsseur;
    }

    public Double getSeuilClient() {
        return seuilClient;
    }

    public void setSeuilClient(Double seuilClient) {
        this.seuilClient = seuilClient;
    }

    public Integer getDureeInactiv() {
        return dureeInactiv;
    }

    public void setDureeInactiv(Integer dureeInactiv) {
        this.dureeInactiv = dureeInactiv;
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

    public Integer getConverter() {
        return converter;
    }

    public void setConverter(Integer converter) {
        this.converter = converter;
    }

    public Integer getJourUsine() {
        return jourUsine;
    }

    public void setJourUsine(Integer jourUsine) {
        this.jourUsine = jourUsine;
    }

    public Integer getConverterCs() {
        return converterCs;
    }

    public void setConverterCs(Integer converterCs) {
        this.converterCs = converterCs;
    }

    public Integer getJourDebutMois() {
        return jourDebutMois;
    }

    public void setJourDebutMois(Integer jourDebutMois) {
        this.jourDebutMois = jourDebutMois;
    }

    public Boolean getFactureOutsideSeuil() {
        return factureOutsideSeuil;
    }

    public void setFactureOutsideSeuil(Boolean factureOutsideSeuil) {
        this.factureOutsideSeuil = factureOutsideSeuil;
    }

    public String getDocumentGenererFromEcart() {
        return documentGenererFromEcart;
    }

    public void setDocumentGenererFromEcart(String documentGenererFromEcart) {
        this.documentGenererFromEcart = documentGenererFromEcart;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsComParametre)) {
            return false;
        }
        YvsComParametre other = (YvsComParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComParametre[ id=" + id + " ]";
    }
    
}
