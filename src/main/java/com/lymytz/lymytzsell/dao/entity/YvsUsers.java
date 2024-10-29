/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import java.io.Serializable;
import java.util.Date;

/**
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
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
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

    public YvsUsers(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsUsers[ id=" + id + " ]";
    }

}
