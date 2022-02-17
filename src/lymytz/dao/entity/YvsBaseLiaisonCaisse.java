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
@Table(name = "yvs_base_liaison_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findAll", query = "SELECT y FROM YvsBaseLiaisonCaisse y"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findById", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findBySource", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.caisseSource=:source "),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findByActif", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findByDateUpdate", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findByDateSave", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.dateSave = :dateSave")})
public class YvsBaseLiaisonCaisse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "caisse_cible", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisseCible;
    @JoinColumn(name = "caisse_source", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisseSource;

    public YvsBaseLiaisonCaisse() {
    }

    public YvsBaseLiaisonCaisse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsBaseCaisse getCaisseCible() {
        return caisseCible;
    }

    public void setCaisseCible(YvsBaseCaisse caisseCible) {
        this.caisseCible = caisseCible;
    }

    public YvsBaseCaisse getCaisseSource() {
        return caisseSource;
    }

    public void setCaisseSource(YvsBaseCaisse caisseSource) {
        this.caisseSource = caisseSource;
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
        if (!(object instanceof YvsBaseLiaisonCaisse)) {
            return false;
        }
        YvsBaseLiaisonCaisse other = (YvsBaseLiaisonCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseLiaisonCaisse[ id=" + id + " ]";
    }
    
}
