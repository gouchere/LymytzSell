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
@Table(name = "yvs_base_users_acces")
@NamedQueries({
    @NamedQuery(name = "YvsBaseUsersAcces.findAll", query = "SELECT y FROM YvsBaseUsersAcces y"),
    @NamedQuery(name = "YvsBaseUsersAcces.findById", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseUsersAcces.findByDateSave", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseUsersAcces.findByDateUpdate", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseUsersAcces.findByExecuteTrigger", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.executeTrigger = :executeTrigger")})
public class YvsBaseUsersAcces implements Serializable {
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
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "code", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCodeAcces code;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers users;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseUsersAcces() {
    }

    public YvsBaseUsersAcces(Long id) {
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

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public YvsBaseCodeAcces getCode() {
        return code;
    }

    public void setCode(YvsBaseCodeAcces code) {
        this.code = code;
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
        if (!(object instanceof YvsBaseUsersAcces)) {
            return false;
        }
        YvsBaseUsersAcces other = (YvsBaseUsersAcces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseUsersAcces[ id=" + id + " ]";
    }
    
}
