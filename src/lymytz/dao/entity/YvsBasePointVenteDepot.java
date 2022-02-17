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
@Table(name = "yvs_base_point_vente_depot")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointVenteDepot.findAll", query = "SELECT y FROM YvsBasePointVenteDepot y"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findById", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findIdDepotByPoint", query = "SELECT y.depot.id FROM YvsBasePointVenteDepot y WHERE y.pointVente = :pointVente AND y.actif = TRUE AND y.depot.actif = TRUE"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByActif", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByPrincipal", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.principal = :principal"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByDateUpdate", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByDateSave", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.dateSave = :dateSave")})
public class YvsBasePointVenteDepot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "point_vente", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente pointVente;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depot;

    public YvsBasePointVenteDepot() {
    }

    public YvsBasePointVenteDepot(Long id) {
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

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBasePointVente getPointVente() {
        return pointVente;
    }

    public void setPointVente(YvsBasePointVente pointVente) {
        this.pointVente = pointVente;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
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
        if (!(object instanceof YvsBasePointVenteDepot)) {
            return false;
        }
        YvsBasePointVenteDepot other = (YvsBasePointVenteDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePointVenteDepot[ id=" + id + " ]";
    }
    
}
