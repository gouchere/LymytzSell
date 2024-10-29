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
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_article_categorie_comptable")
@NamedQuery(name = "YvsBaseArticleCategorieComptable.findAll", query = "SELECT y FROM YvsBaseArticleCategorieComptable y")
@NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.article = :article")
@NamedQuery(name = "YvsBaseArticleCategorieComptable.findById", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.id = :id")
@NamedQuery(name = "YvsBaseArticleCategorieComptable.findByActif", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.actif = :actif")
@NamedQuery(name = "YvsBaseArticleCategorieComptable.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.dateUpdate = :dateUpdate")
@NamedQuery(name = "YvsBaseArticleCategorieComptable.findByDateSave", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.dateSave = :dateSave")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseArticleCategorieComptable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @OneToMany(mappedBy = "articleCategorie")
    private List<YvsBaseArticleCategorieComptableTaxe> taxes;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieComptable categorie;

    public YvsBaseArticleCategorieComptable() {
    }

    public YvsBaseArticleCategorieComptable(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleCategorieComptable[ id=" + id + " ]";
    }

}
