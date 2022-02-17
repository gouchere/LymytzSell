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
import javax.persistence.CascadeType;
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

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_users")
@NamedQueries({
    @NamedQuery(name = "YvsUsers.findAll", query = "SELECT y FROM YvsUsers y"),
    @NamedQuery(name = "YvsUsers.findById", query = "SELECT y FROM YvsUsers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsUsers.findByCodeUsers_", query = "SELECT y FROM YvsUsers y WHERE y.codeUsers = :codeUsers AND y.actif = true "),
    @NamedQuery(name = "YvsUsers.findByCodeUsers", query = "SELECT y FROM YvsUsers y WHERE y.codeUsers = :codeUsers"),
    @NamedQuery(name = "YvsUsers.findByPasswordUser", query = "SELECT y FROM YvsUsers y WHERE y.passwordUser = :passwordUser"),
    @NamedQuery(name = "YvsUsers.findByAleaMdp", query = "SELECT y FROM YvsUsers y WHERE y.aleaMdp = :aleaMdp"),
    @NamedQuery(name = "YvsUsers.findByNomUsers", query = "SELECT y FROM YvsUsers y WHERE y.nomUsers = :nomUsers"),
    @NamedQuery(name = "YvsUsers.findByPhoto", query = "SELECT y FROM YvsUsers y WHERE y.photo = :photo"),
    @NamedQuery(name = "YvsUsers.findByCivilite", query = "SELECT y FROM YvsUsers y WHERE y.civilite = :civilite"),
    @NamedQuery(name = "YvsUsers.findByDateUpdate", query = "SELECT y FROM YvsUsers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsUsers.findByDateSave", query = "SELECT y FROM YvsUsers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsUsers.findByAbbreviation", query = "SELECT y FROM YvsUsers y WHERE y.abbreviation = :abbreviation"),
    @NamedQuery(name = "YvsUsers.findByActif", query = "SELECT y FROM YvsUsers y WHERE y.actif = :actif")})
public class YvsUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_users")
    private String codeUsers;
    @Column(name = "password_user")
    private String passwordUser;
    @Column(name = "alea_mdp")
    private String aleaMdp;
    @Column(name = "nom_users")
    private String nomUsers;
    @Column(name = "photo")
    private String photo;
    @Column(name = "civilite")
    private String civilite;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;

    public YvsUsers() {
    }

    public YvsUsers(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeUsers() {
        return codeUsers;
    }

    public void setCodeUsers(String codeUsers) {
        this.codeUsers = codeUsers;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getAleaMdp() {
        return aleaMdp;
    }

    public void setAleaMdp(String aleaMdp) {
        this.aleaMdp = aleaMdp;
    }

    public String getNomUsers() {
        return nomUsers;
    }

    public void setNomUsers(String nomUsers) {
        this.nomUsers = nomUsers;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
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
        if (!(object instanceof YvsUsers)) {
            return false;
        }
        YvsUsers other = (YvsUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsUsers[ id=" + id + " ]";
    }

}
