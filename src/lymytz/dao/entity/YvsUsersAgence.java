/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

//import com.fasterxml.jackson.annotation.JsonFormat;
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
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//import lymytz.dao.DateTimeAdapter;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_users_agence")
@NamedQueries({
    @NamedQuery(name = "YvsUsersAgence.findAll", query = "SELECT y FROM YvsUsersAgence y"),
    @NamedQuery(name = "YvsUsersAgence.findById", query = "SELECT y FROM YvsUsersAgence y WHERE y.id = :id"),
    @NamedQuery(name = "YvsUsersAgence.findByDateUpdate", query = "SELECT y FROM YvsUsersAgence y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsUsersAgence.findByUsersAgence", query = "SELECT y FROM YvsUsersAgence y JOIN FETCH y.agence JOIN FETCH y.users WHERE y.agence=:agence AND y.users=:user"),
    @NamedQuery(name = "YvsUsersAgence.findByDateSave", query = "SELECT y FROM YvsUsersAgence y WHERE y.dateSave = :dateSave")})
public class YvsUsersAgence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private YvsAgences agence;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private YvsUsers users;

    public YvsUsersAgence() {
    }

    public YvsUsersAgence(Long id) {
        this.id = id;
    }

    public YvsUsersAgence(Long id, YvsUsers user) {
        this(id);
        this.users = user;
    }

    public YvsUsersAgence(Long id, YvsUsers user, YvsAgences agence) {
        this(id, user);
        this.agence = agence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
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
        if (!(object instanceof YvsUsersAgence)) {
            return false;
        }
        YvsUsersAgence other = (YvsUsersAgence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsUsersAgence[ id=" + id + " ]";
    }
}
