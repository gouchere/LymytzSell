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
@Table(name = "yvs_com_creneau_point")
@NamedQueries({
    @NamedQuery(name = "YvsComCreneauPoint.findAll", query = "SELECT y FROM YvsComCreneauPoint y"),
    @NamedQuery(name = "YvsComCreneauPoint.findById", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauPoint.findByActif", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComCreneauPoint.findByPermanent", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauPoint.findByDateUpdate", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCreneauPoint.findByDateSave", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.dateSave = :dateSave")})
public class YvsComCreneauPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente point;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhTrancheHoraire tranche;

    public YvsComCreneauPoint() {
    }

    public YvsComCreneauPoint(Long id) {
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

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
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

    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
    }

    public YvsGrhTrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(YvsGrhTrancheHoraire tranche) {
        this.tranche = tranche;
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
        if (!(object instanceof YvsComCreneauPoint)) {
            return false;
        }
        YvsComCreneauPoint other = (YvsComCreneauPoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCreneauPoint[ id=" + id + " ]";
    }

}
