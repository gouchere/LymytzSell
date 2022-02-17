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
@Table(name = "yvs_com_commercial_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComCommercialVente.findAll", query = "SELECT y FROM YvsComCommercialVente y"),
    @NamedQuery(name = "YvsComCommercialVente.findById", query = "SELECT y FROM YvsComCommercialVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCommercialVente.findByTaux", query = "SELECT y FROM YvsComCommercialVente y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsComCommercialVente.findByDateSave", query = "SELECT y FROM YvsComCommercialVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComCommercialVente.findByDateUpdate", query = "SELECT y FROM YvsComCommercialVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCommercialVente.findByAuthor", query = "SELECT y FROM YvsComCommercialVente y WHERE y.author = :author"),
    @NamedQuery(name = "YvsComCommercialVente.findByResponsable", query = "SELECT y FROM YvsComCommercialVente y WHERE y.responsable = :responsable"),
    @NamedQuery(name = "YvsComCommercialVente.findByDiminueCa", query = "SELECT y FROM YvsComCommercialVente y WHERE y.diminueCa = :diminueCa")})
public class YvsComCommercialVente extends YvsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "responsable")
    private Boolean responsable;
    @Column(name = "diminue_ca")
    private Boolean diminueCa;
    @JoinColumn(name = "commercial", referencedColumnName = "id")
    @ManyToOne
    private YvsComComerciale commercial;
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocVentes facture;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComCommercialVente() {
    }

    public YvsComCommercialVente(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(Boolean responsable) {
        this.responsable = responsable;
    }

    public Boolean getDiminueCa() {
        return diminueCa;
    }

    public void setDiminueCa(Boolean diminueCa) {
        this.diminueCa = diminueCa;
    }

    public YvsComComerciale getCommercial() {
        return commercial;
    }

    public void setCommercial(YvsComComerciale commercial) {
        this.commercial = commercial;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
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
        if (!(object instanceof YvsComCommercialVente)) {
            return false;
        }
        YvsComCommercialVente other = (YvsComCommercialVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCommercialVente[ id=" + id + " ]";
    }
    
}
