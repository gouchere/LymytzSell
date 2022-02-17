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
 * @author Admin
 */
@Entity
@Table(name = "yvs_agences")
@NamedQueries({
    @NamedQuery(name = "YvsAgences.findAll", query = "SELECT y FROM YvsAgences y"),
    @NamedQuery(name = "YvsAgences.findById", query = "SELECT y FROM YvsAgences y WHERE y.id = :id"),
    @NamedQuery(name = "YvsAgences.findBySociete", query = "SELECT y FROM YvsAgences y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsAgences.findByAdresse", query = "SELECT y FROM YvsAgences y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsAgences.findByCodeagence", query = "SELECT y FROM YvsAgences y WHERE y.codeagence = :codeagence"),
    @NamedQuery(name = "YvsAgences.findByDesignation", query = "SELECT y FROM YvsAgences y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsAgences.findByRegion", query = "SELECT y FROM YvsAgences y WHERE y.region = :region"),
    @NamedQuery(name = "YvsAgences.findByActif", query = "SELECT y FROM YvsAgences y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsAgences.findByEmail", query = "SELECT y FROM YvsAgences y WHERE y.email = :email"),
    @NamedQuery(name = "YvsAgences.findByTelephone", query = "SELECT y FROM YvsAgences y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsAgences.findByAdresseIp", query = "SELECT y FROM YvsAgences y WHERE y.adresseIp = :adresseIp"),
    @NamedQuery(name = "YvsAgences.findByCodePostal", query = "SELECT y FROM YvsAgences y WHERE y.codePostal = :codePostal"),
    @NamedQuery(name = "YvsAgences.findByDateSave", query = "SELECT y FROM YvsAgences y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsAgences.findByDateUpdate", query = "SELECT y FROM YvsAgences y WHERE y.dateUpdate = :dateUpdate")})
public class YvsAgences implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "codeagence")
    private String codeagence;
    @Column(name = "designation")
    private String designation;
    @Column(name = "region")
    private String region;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "email")
    private String email;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Column(name = "code_postal")
    private String codePostal;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsAgences() {
    }

    public YvsAgences(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodeagence() {
        return codeagence;
    }

    public void setCodeagence(String codeagence) {
        this.codeagence = codeagence;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsAgences)) {
            return false;
        }
        YvsAgences other = (YvsAgences) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsAgences[ id=" + id + " ]";
    }   

}
