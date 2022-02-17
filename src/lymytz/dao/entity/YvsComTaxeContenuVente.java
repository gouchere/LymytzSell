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
@Table(name = "yvs_com_taxe_contenu_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComTaxeContenuVente.findAll", query = "SELECT y FROM YvsComTaxeContenuVente y"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findById", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findByMontant", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findByDateUpdate", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComTaxeContenuVente.findByDateSave", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.dateSave = :dateSave")})
public class YvsComTaxeContenuVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTaxes taxe;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne
    private YvsComContenuDocVente contenu;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComTaxeContenuVente() {
    }

    public YvsComTaxeContenuVente(Long id) {
        this.id = id;
    }

    public YvsComTaxeContenuVente(YvsComTaxeContenuVente t) {
        this(t.id);
        adresseServeur = t.adresseServeur;
        author = t.author;
        contenu = t.contenu;
        dateSave = t.dateSave;
        dateUpdate = t.dateUpdate;
        montant = t.montant;
        taxe = t.taxe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
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

    public YvsBaseTaxes getTaxe() {
        return taxe;
    }

    public void setTaxe(YvsBaseTaxes taxe) {
        this.taxe = taxe;
    }

    public YvsComContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(YvsComContenuDocVente contenu) {
        this.contenu = contenu;
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
        if (!(object instanceof YvsComTaxeContenuVente)) {
            return false;
        }
        YvsComTaxeContenuVente other = (YvsComTaxeContenuVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComTaxeContenuVente[ id=" + id + " ]";
    }

}
