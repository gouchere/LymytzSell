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
@Table(name = "yvs_base_caisse_user")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCaisseUser.findAll", query = "SELECT y FROM YvsBaseCaisseUser y"),
    @NamedQuery(name = "YvsBaseCaisseUser.findById", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByUser", query = "SELECT y.idCaisse FROM YvsBaseCaisseUser y WHERE y.idUser = :user"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByDateSave", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByDateUpdate", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByActif", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.actif = :actif")})
public class YvsBaseCaisseUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "id_caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse idCaisse;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers idUser;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseCaisseUser() {
    }

    public YvsBaseCaisseUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseCaisse getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(YvsBaseCaisse idCaisse) {
        this.idCaisse = idCaisse;
    }

    public YvsUsers getIdUser() {
        return idUser;
    }

    public void setIdUser(YvsUsers idUser) {
        this.idUser = idUser;
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
        if (!(object instanceof YvsBaseCaisseUser)) {
            return false;
        }
        YvsBaseCaisseUser other = (YvsBaseCaisseUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCaisseUser[ id=" + id + " ]";
    }
    
}
