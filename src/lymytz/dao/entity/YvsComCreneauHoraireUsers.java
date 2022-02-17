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
@Table(name = "yvs_com_creneau_horaire_users")
@NamedQueries({
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findAll", query = "SELECT y FROM YvsComCreneauHoraireUsers y"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findById", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByActif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByDateTravail", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.dateTravail = :dateTravail"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByPermanent", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findOne", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint=:creneauPoint AND y.dateTravail=:date"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findCByUsersOnPV", query = "SELECT COUNT(y) FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND y.creneauDepot IS NOT NULL AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersOnPV", query = "SELECT y FROM YvsComCreneauHoraireUsers y JOIN FETCH y.creneauPoint JOIN FETCH y.creneauPoint.point JOIN FETCH y.creneauPoint.point.agence WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND y.creneauDepot IS NOT NULL AND y.actif=true "
            + " AND (y.permanent=true OR y.dateTravail BETWEEN :date1 AND :date2) ORDER BY y.dateTravail DESC "),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findPointPermanentByUser", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND y.permanent=TRUE AND y.actif=TRUE AND y.creneauPoint.actif=TRUE AND y.creneauPoint.point.actif=TRUE"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDates", query = "SELECT y FROM YvsComCreneauHoraireUsers y LEFT JOIN FETCH y.creneauPoint LEFT JOIN FETCH y.creneauPoint.point LEFT JOIN FETCH y.creneauPoint.tranche "
            + "WHERE y.actif=true AND y.users = :users AND (y.permanent=TRUE OR y.dateTravail BETWEEN :dateDebut AND :dateFin) ORDER BY y.dateTravail DESC"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByDateUpdate", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByDateSave", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.dateSave = :dateSave")})
public class YvsComCreneauHoraireUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_travail")
    @Temporal(TemporalType.DATE)
    private Date dateTravail;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "type")
    private String type;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "creneau_depot", referencedColumnName = "id")
    @ManyToOne
    private YvsComCreneauDepot creneauDepot;
    @JoinColumn(name = "creneau_point", referencedColumnName = "id")
    @ManyToOne
    private YvsComCreneauPoint creneauPoint;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers users;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComCreneauHoraireUsers() {
    }

    public YvsComCreneauHoraireUsers(Long id) {
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

    public Date getDateTravail() {
        return dateTravail;
    }

    public void setDateTravail(Date dateTravail) {
        this.dateTravail = dateTravail;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public YvsComCreneauDepot getCreneauDepot() {
        return creneauDepot;
    }

    public void setCreneauDepot(YvsComCreneauDepot creneauDepot) {
        this.creneauDepot = creneauDepot;
    }

    public YvsComCreneauPoint getCreneauPoint() {
        return creneauPoint;
    }

    public void setCreneauPoint(YvsComCreneauPoint creneauPoint) {
        this.creneauPoint = creneauPoint;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
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
        if (!(object instanceof YvsComCreneauHoraireUsers)) {
            return false;
        }
        YvsComCreneauHoraireUsers other = (YvsComCreneauHoraireUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCreneauHoraireUsers[ id=" + id + " ]";
    }

}
