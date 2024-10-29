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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_comerciale")
@NamedQueries({
        @NamedQuery(name = "YvsComComerciale.findAll", query = "SELECT y FROM YvsComComerciale y"),
        @NamedQuery(name = "YvsComComerciale.findByUser", query = "SELECT y FROM YvsComComerciale y WHERE y.utilisateur = :user"),
        @NamedQuery(name = "YvsComComerciale.findById", query = "SELECT y FROM YvsComComerciale y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComComerciale.findByCodeRef", query = "SELECT y FROM YvsComComerciale y WHERE y.codeRef = :codeRef"),
        @NamedQuery(name = "YvsComComerciale.findByNom", query = "SELECT y FROM YvsComComerciale y WHERE y.nom = :nom"),
        @NamedQuery(name = "YvsComComerciale.findByPrenom", query = "SELECT y FROM YvsComComerciale y WHERE y.prenom = :prenom"),
        @NamedQuery(name = "YvsComComerciale.findByTelephone", query = "SELECT y FROM YvsComComerciale y WHERE y.telephone = :telephone"),
        @NamedQuery(name = "YvsComComerciale.findByDateSave", query = "SELECT y FROM YvsComComerciale y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComComerciale.findByDateUpdate", query = "SELECT y FROM YvsComComerciale y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComComerciale.findByActif", query = "SELECT y FROM YvsComComerciale y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsComComerciale.findByDefaut", query = "SELECT y FROM YvsComComerciale y WHERE y.defaut = :defaut")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComComerciale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_ref")
    private String codeRef;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "defaut")
    private Boolean defaut;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTiers tiers;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers utilisateur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComComerciale(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComComerciale[ id=" + id + " ]";
    }

}
