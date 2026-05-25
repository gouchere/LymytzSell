/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.entity;

import lombok.Getter;
import lombok.Setter;

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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_agences")
@NamedQuery(name = "YvsAgences.findAll", query = "SELECT y FROM YvsAgences y")
@NamedQuery(name = "YvsAgences.findById", query = "SELECT y FROM YvsAgences y WHERE y.id = :id")
@NamedQuery(name = "YvsAgences.findBySociete", query = "SELECT y FROM YvsAgences y WHERE y.societe = :societe")
@NamedQuery(name = "YvsAgences.findByAdresse", query = "SELECT y FROM YvsAgences y WHERE y.adresse = :adresse")
@NamedQuery(name = "YvsAgences.findByCodeagence", query = "SELECT y FROM YvsAgences y WHERE y.codeagence = :codeagence")
@NamedQuery(name = "YvsAgences.findByDesignation", query = "SELECT y FROM YvsAgences y WHERE y.designation = :designation")
@NamedQuery(name = "YvsAgences.findByRegion", query = "SELECT y FROM YvsAgences y WHERE y.region = :region")
@NamedQuery(name = "YvsAgences.findByActif", query = "SELECT y FROM YvsAgences y WHERE y.actif = :actif")
@NamedQuery(name = "YvsAgences.findByEmail", query = "SELECT y FROM YvsAgences y WHERE y.email = :email")
@NamedQuery(name = "YvsAgences.findByTelephone", query = "SELECT y FROM YvsAgences y WHERE y.telephone = :telephone")
@NamedQuery(name = "YvsAgences.findByAdresseIp", query = "SELECT y FROM YvsAgences y WHERE y.adresseIp = :adresseIp")
@NamedQuery(name = "YvsAgences.findByCodePostal", query = "SELECT y FROM YvsAgences y WHERE y.codePostal = :codePostal")
@NamedQuery(name = "YvsAgences.findByDateSave", query = "SELECT y FROM YvsAgences y WHERE y.dateSave = :dateSave")
@NamedQuery(name = "YvsAgences.findByDateUpdate", query = "SELECT y FROM YvsAgences y WHERE y.dateUpdate = :dateUpdate")
@Getter
@Setter
public class YvsAgences extends YvsEntity implements Serializable {


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

    @Override
    public YvsUsersAgence getAuthor() {
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof YvsAgences other)) {
            return false;
        }
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsAgences[ id=" + id + " ]";
    }

}
