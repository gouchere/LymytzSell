/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_base_point_vente")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointVente.findAll", query = "SELECT y FROM YvsBasePointVente y"),
    @NamedQuery(name = "YvsBasePointVente.findById", query = "SELECT y FROM YvsBasePointVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePointVente.findByCode", query = "SELECT y FROM YvsBasePointVente y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBasePointVente.findByLibelle", query = "SELECT y FROM YvsBasePointVente y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBasePointVente.findByAdresse", query = "SELECT y FROM YvsBasePointVente y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsBasePointVente.findByReglementAuto", query = "SELECT y FROM YvsBasePointVente y WHERE y.reglementAuto = :reglementAuto"),
    @NamedQuery(name = "YvsBasePointVente.findByActif", query = "SELECT y FROM YvsBasePointVente y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBasePointVente.findByLivraisonOn", query = "SELECT y FROM YvsBasePointVente y WHERE y.livraisonOn = :livraisonOn"),
    @NamedQuery(name = "YvsBasePointVente.findByDateUpdate", query = "SELECT y FROM YvsBasePointVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBasePointVente.findByCommissionFor", query = "SELECT y FROM YvsBasePointVente y WHERE y.commissionFor = :commissionFor"),
    @NamedQuery(name = "YvsBasePointVente.findByDateSave", query = "SELECT y FROM YvsBasePointVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBasePointVente.findByPrixMinStrict", query = "SELECT y FROM YvsBasePointVente y WHERE y.prixMinStrict = :prixMinStrict"),
    @NamedQuery(name = "YvsBasePointVente.findByVenteOnline", query = "SELECT y FROM YvsBasePointVente y WHERE y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsBasePointVente.findByAcceptClientNoName", query = "SELECT y FROM YvsBasePointVente y WHERE y.acceptClientNoName = :acceptClientNoName"),
    @NamedQuery(name = "YvsBasePointVente.findByValidationReglement", query = "SELECT y FROM YvsBasePointVente y WHERE y.validationReglement = :validationReglement"),
    @NamedQuery(name = "YvsBasePointVente.findByTelephone", query = "SELECT y FROM YvsBasePointVente y WHERE y.telephone = :telephone")})
public class YvsBasePointVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "reglement_auto")
    private Boolean reglementAuto;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "livraison_on")
    private Character livraisonOn;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "commission_for")
    private Character commissionFor;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "prix_min_strict")
    private Boolean prixMinStrict;
    @Column(name = "vente_online")
    private Boolean venteOnline;
    @Column(name = "accept_client_no_name")
    private Boolean acceptClientNoName;
    @Column(name = "validation_reglement")
    private Boolean validationReglement;
    @Column(name = "telephone")
    private String telephone;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "point")
    private List<YvsComCommercialPoint> commerciaux;

    public YvsBasePointVente() {
    }

    public YvsBasePointVente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Boolean getReglementAuto() {
        return reglementAuto;
    }

    public void setReglementAuto(Boolean reglementAuto) {
        this.reglementAuto = reglementAuto;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Character getLivraisonOn() {
        return livraisonOn;
    }

    public void setLivraisonOn(Character livraisonOn) {
        this.livraisonOn = livraisonOn;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Character getCommissionFor() {
        return commissionFor;
    }

    public void setCommissionFor(Character commissionFor) {
        this.commissionFor = commissionFor;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getPrixMinStrict() {
        return prixMinStrict;
    }

    public void setPrixMinStrict(Boolean prixMinStrict) {
        this.prixMinStrict = prixMinStrict;
    }

    public Boolean getVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public Boolean getAcceptClientNoName() {
        return acceptClientNoName;
    }

    public void setAcceptClientNoName(Boolean acceptClientNoName) {
        this.acceptClientNoName = acceptClientNoName;
    }

    public Boolean getValidationReglement() {
        return validationReglement;
    }

    public void setValidationReglement(Boolean validationReglement) {
        this.validationReglement = validationReglement;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    @XmlTransient
    public List<YvsComCommercialPoint> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComCommercialPoint> commerciaux) {
        this.commerciaux = commerciaux;
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
        if (!(object instanceof YvsBasePointVente)) {
            return false;
        }
        YvsBasePointVente other = (YvsBasePointVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePointVente[ id=" + id + " ]";
    }

}
