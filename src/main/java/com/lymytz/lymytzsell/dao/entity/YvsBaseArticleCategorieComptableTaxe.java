/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.dao.entity;

import lombok.EqualsAndHashCode;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_article_categorie_comptable_taxe")
@NamedQueries({
        @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findAll", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y"),
        @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findById", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByAppRemise", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.appRemise = :appRemise"),
        @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByActif", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByDateSave", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseArticleCategorieComptableTaxe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "app_remise")
    private Boolean appRemise;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article_categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticleCategorieComptable articleCategorie;
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTaxes taxe;

    public YvsBaseArticleCategorieComptableTaxe() {
    }

    public YvsBaseArticleCategorieComptableTaxe(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleCategorieComptableTaxe[ id=" + id + " ]";
    }

}
