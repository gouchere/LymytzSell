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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_societes")
@NamedQueries({
        @NamedQuery(name = "YvsSocietes.findAll", query = "SELECT y FROM YvsSocietes y WHERE y.actif=true"),
        @NamedQuery(name = "YvsSocietes.findById", query = "SELECT y FROM YvsSocietes y WHERE y.id = :id"),
        @NamedQuery(name = "YvsSocietes.findByAdressSiege", query = "SELECT y FROM YvsSocietes y WHERE y.adressSiege = :adressSiege"),
        @NamedQuery(name = "YvsSocietes.findByCodeAbreviation", query = "SELECT y FROM YvsSocietes y WHERE y.codeAbreviation = :codeAbreviation"),
        @NamedQuery(name = "YvsSocietes.findByCodePostal", query = "SELECT y FROM YvsSocietes y WHERE y.codePostal = :codePostal"),
        @NamedQuery(name = "YvsSocietes.findByDevise", query = "SELECT y FROM YvsSocietes y WHERE y.devise = :devise"),
        @NamedQuery(name = "YvsSocietes.findByEmail", query = "SELECT y FROM YvsSocietes y WHERE y.email = :email"),
        @NamedQuery(name = "YvsSocietes.findByFormeJuridique", query = "SELECT y FROM YvsSocietes y WHERE y.formeJuridique = :formeJuridique"),
        @NamedQuery(name = "YvsSocietes.findByGestva", query = "SELECT y FROM YvsSocietes y WHERE y.gestva = :gestva"),
        @NamedQuery(name = "YvsSocietes.findByLastAuthor", query = "SELECT y FROM YvsSocietes y WHERE y.lastAuthor = :lastAuthor"),
        @NamedQuery(name = "YvsSocietes.findByLogo", query = "SELECT y FROM YvsSocietes y WHERE y.logo = :logo"),
        @NamedQuery(name = "YvsSocietes.findByName", query = "SELECT y FROM YvsSocietes y WHERE y.name = :name"),
        @NamedQuery(name = "YvsSocietes.findByNumeroRegistreComerce", query = "SELECT y FROM YvsSocietes y WHERE y.numeroRegistreComerce = :numeroRegistreComerce"),
        @NamedQuery(name = "YvsSocietes.findBySiege", query = "SELECT y FROM YvsSocietes y WHERE y.siege = :siege"),
        @NamedQuery(name = "YvsSocietes.findBySiteWeb", query = "SELECT y FROM YvsSocietes y WHERE y.siteWeb = :siteWeb"),
        @NamedQuery(name = "YvsSocietes.findByTel", query = "SELECT y FROM YvsSocietes y WHERE y.tel = :tel"),
        @NamedQuery(name = "YvsSocietes.findByActif", query = "SELECT y FROM YvsSocietes y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsSocietes.findByRegimeCnps", query = "SELECT y FROM YvsSocietes y WHERE y.regimeCnps = :regimeCnps"),
        @NamedQuery(name = "YvsSocietes.findByDateSave", query = "SELECT y FROM YvsSocietes y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsSocietes.findByDateUpdate", query = "SELECT y FROM YvsSocietes y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsSocietes.findByDescription", query = "SELECT y FROM YvsSocietes y WHERE y.description = :description"),
        @NamedQuery(name = "YvsSocietes.findByEcartDocument", query = "SELECT y FROM YvsSocietes y WHERE y.ecartDocument = :ecartDocument"),
        @NamedQuery(name = "YvsSocietes.findByAPropos", query = "SELECT y FROM YvsSocietes y WHERE y.aPropos = :aPropos")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsSocietes extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "adress_siege")
    private String adressSiege;
    @Column(name = "code_abreviation")
    private String codeAbreviation;
    @Column(name = "code_postal")
    private String codePostal;
    @Column(name = "devise")
    private String devise;
    @Column(name = "email")
    private String email;
    @Column(name = "forme_juridique")
    private String formeJuridique;
    @Column(name = "gestva")
    private Boolean gestva;
    @Column(name = "last_author")
    private String lastAuthor;
    @Column(name = "logo")
    private String logo;
    @Column(name = "name")
    private String name;
    @Column(name = "numero_registre_comerce")
    private String numeroRegistreComerce;
    @Column(name = "numero_contribuable")
    private String numeroContribuable;
    @Column(name = "siege")
    private String siege;
    @Column(name = "site_web")
    private String siteWeb;
    @Column(name = "tel")
    private String tel;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "regime_cnps")
    private String regimeCnps;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "description")
    private String description;
    @Column(name = "ecart_document")
    private Integer ecartDocument;
    @Column(name = "a_propos")
    private String aPropos;

    public YvsSocietes(Long id) {
        this.id = id;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return null;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsSocietes[ id=" + id + " ]";
    }
}
